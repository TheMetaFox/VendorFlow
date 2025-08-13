package com.example.vendorflow.ui.screens.tags

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.logic.DeleteTagUseCase
import com.example.vendorflow.logic.GetTagsOrderedByOrdinalUseCase
import com.example.vendorflow.logic.UpdateTagOrdinalsUseCase
import com.example.vendorflow.logic.UpsertTagUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagsViewModel(
    private val upsertTagUseCase: UpsertTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
    getTagsOrderedByOrdinalUseCase: GetTagsOrderedByOrdinalUseCase,
    private val updateTagOrdinalsUseCase: UpdateTagOrdinalsUseCase
): ViewModel() {
    private val _tagList: StateFlow<List<Tag>> = getTagsOrderedByOrdinalUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<TagsState> = MutableStateFlow(value = TagsState())
    val state: StateFlow<TagsState> = combine(
        flow = _state,
        flow2 = _tagList,
        transform = { state, tagList ->
            state.copy(
                tagList = tagList,
            )
        }
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TagsState()
        )

    fun onEvent(tagsEvent: TagsEvent) {
        when (tagsEvent) {
            is TagsEvent.HideTagDialog -> {
                _state.update {
                    it.copy(
                        isShowingTagDialog = false,
                        tagId = null
                    )
                }
            }
            is TagsEvent.ShowTagDialog -> {
                if (tagsEvent.tag != null) {
                    _state.update { tagState ->
                        tagState.copy(
                            tagId = tagsEvent.tag.tagId,
                            tagNameField = tagsEvent.tag.tagName,
                        )
                    }
                } else {
                    _state.update { tagState ->
                        tagState.copy(
                            tagNameField = "",
                        )
                    }
                }
                _state.update {
                    it.copy(
                        isShowingTagDialog = true
                    )
                }
            }
            is TagsEvent.UpdateTagNameField -> {
                _state.update { tagState ->
                    tagState.copy(tagNameField = tagsEvent.text)
                }
            }
            is TagsEvent.UpsertTag -> {
                viewModelScope.launch {
                    val tagName: String = _state.value.tagNameField.trim()

                    if (tagName.isBlank()) {
                        Log.i("TagsViewModel.kt","Name field is blank...")
                        return@launch
                    }

                    val tag = Tag(
                        tagId = if (_state.value.tagId == null) 0 else _state.value.tagId!!,
                        tagName = tagName,
                        ordinal = state.value.tagList.size+1
                    )

                    Log.i("TagsViewModel.kt","Upserting $tag to database...")
                    upsertTagUseCase(tag = tag)
                    _state.update {
                        it.copy(
                            tagNameField = "",
                        )
                    }
                }
            }
            is TagsEvent.DeleteTag -> {
                viewModelScope.launch {
                    deleteTagUseCase(tag = tagsEvent.tag)
                }
            }
            is TagsEvent.UpdateOrdinals -> {
                viewModelScope.launch {
                    updateTagOrdinalsUseCase(tag = tagsEvent.tag, newOrdinal = tagsEvent.ordinal)
                }
            }
        }
    }
}