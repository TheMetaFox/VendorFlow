package com.example.vendorflow.ui.screens.collections

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.logic.DeleteCollectionUseCase
import com.example.vendorflow.logic.GetCollectionsOrderedByIdUseCase
import com.example.vendorflow.logic.UpsertCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsViewModel(
    private val upsertCollectionUseCase: UpsertCollectionUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
    private val getCollectionsOrderedByIdUseCase: GetCollectionsOrderedByIdUseCase,
): ViewModel() {
    private val _collectionList: StateFlow<List<Collection>> = getCollectionsOrderedByIdUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<CollectionsState> = MutableStateFlow(value = CollectionsState())
    val state: StateFlow<CollectionsState> = combine(
        flow = _state,
        flow2 = _collectionList,
        transform = { state, collectionList ->
            state.copy(
                collectionList = collectionList,
            )
        }
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollectionsState()
        )

    fun onEvent(collectionsEvent: CollectionsEvent) {
        when (collectionsEvent) {
            is CollectionsEvent.HideCollectionDialog -> {
                _state.update {
                    it.copy(
                        isShowingCollectionDialog = false,
                        collectionId = null
                    )
                }
            }
            is CollectionsEvent.ShowCollectionDialog -> {
                if (collectionsEvent.collection != null) {
                    _state.update { collectionState ->
                        collectionState.copy(
                            collectionId = collectionsEvent.collection.collectionId,
                            collectionNameField = collectionsEvent.collection.collectionName,
                        )
                    }
                } else {
                    _state.update { collectionState ->
                        collectionState.copy(
                            collectionNameField = "",
                        )
                    }
                }
                _state.update {
                    it.copy(
                        isShowingCollectionDialog = true
                    )
                }
            }
            is CollectionsEvent.UpdateCollectionNameField -> {
                _state.update { collectionState ->
                    collectionState.copy(collectionNameField = collectionsEvent.text)
                }
            }
            is CollectionsEvent.UpsertCollection -> {
                viewModelScope.launch {
                    val collectionName: String = _state.value.collectionNameField.trim()

                    if (collectionName.isBlank()) {
                        Log.i("CollectionsViewModel.kt","Name field is blank...")
                        return@launch
                    }

                    val collection = Collection(
                        collectionId = if (_state.value.collectionId == null) 0 else _state.value.collectionId!!,
                        collectionName = collectionName,
                    )

                    Log.i("CollectionsViewModel.kt","Upserting $collection to database...")
                    upsertCollectionUseCase(collection = collection)
                    _state.update {
                        it.copy(
                            collectionNameField = "",
                        )
                    }
                }
            }
            is CollectionsEvent.DeleteCollection -> {
                viewModelScope.launch {
//                    vendorRepository.deleteCollection(
//                        collection = collectionsEvent.collection
//                    )
                    deleteCollectionUseCase(collection = collectionsEvent.collection)
                }
            }
        }
    }
}