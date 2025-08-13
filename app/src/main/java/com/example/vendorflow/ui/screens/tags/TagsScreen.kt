package com.example.vendorflow.ui.screens.tags

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TagScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onTagsEvent: (TagsEvent) -> Unit,
    tagsState: TagsState,
) {
    if (tagsState.isShowingTagDialog) {
        TagDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onTagsEvent = onTagsEvent,
            tagsState = tagsState
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Tag") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onTagsEvent(TagsEvent.ShowTagDialog(tag = null))
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Tag"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.3f)
                ) {
                    Text(
                        text = "ID",
                        fontSize = 36.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Name",
                        fontSize = 36.sp
                    )
                }
            }
            var dragVerticalOffset: Dp by remember { mutableStateOf(value = 0.dp) }
            var draggedTag: Tag? by remember { mutableStateOf(value = null) }
            var dragOrdinalDisplacement: Int by remember { mutableIntStateOf(value = 0) }
            var dragOrdinal: Int by remember {
                mutableIntStateOf(value = if (draggedTag == null) 0 else min(
                    a = dragOrdinalDisplacement + draggedTag!!.ordinal,
                    b = tagsState.tagList.size
                ))
            }
            Log.i("TagsScreen.kt", "Drag Ordinal Displacement: $dragOrdinalDisplacement")
            Log.i("TagsScreen.kt", "Drag Ordinal: $dragOrdinal")
            LazyColumn (
                modifier = Modifier.
                fillMaxSize()
            ) {
                items(
                    count = tagsState.tagList.size
                ) {
                    val tag: Tag = tagsState.tagList[it]
                    var isDropDownMenuVisible: Boolean by remember { mutableStateOf(value = false) }
                    var longPressOffset: DpOffset by remember { mutableStateOf(value = DpOffset.Zero) }
                    val density: Density = LocalDensity.current
                    val rowHeight = 40.dp
                    val rowPadding = 10.dp
                    val animatedDragOffset: Dp by animateDpAsState(targetValue = if (draggedTag == tag) dragVerticalOffset else 0.dp)
                    val animatedTagOffset: Dp by animateDpAsState(
                        targetValue = if (draggedTag == null || draggedTag == tag) 0.dp else {
//                            Log.i("TagsScreen.kt", "${tag.tagName} Ordinal Offset: ${draggedTag!!.ordinal + (dragVerticalOffset / (rowHeight + (2 * rowPadding))).roundToInt() >= tag.ordinal && draggedTag!!.ordinal < tag.ordinal}")
                            if (draggedTag!!.ordinal + (dragVerticalOffset / (rowHeight + (2 * rowPadding))).roundToInt() >= tag.ordinal && draggedTag!!.ordinal < tag.ordinal) {
                                -(rowHeight + (2 * rowPadding))
                            } else if (draggedTag!!.ordinal + (dragVerticalOffset / (rowHeight + (2 * rowPadding))).roundToInt() <= tag.ordinal && draggedTag!!.ordinal > tag.ordinal) {
                                rowHeight + (2 * rowPadding)
                            } else 0.dp
                        }
                    )
                    val animatedSnapback: Dp by animateDpAsState(
                        targetValue = if (dragOrdinal == tag.ordinal) {
                            Log.i("TagsScreen.kt", tag.tagName)
                            animatedDragOffset }//- (dragOrdinalDisplacement *(rowHeight + (2 * rowPadding)))}
                            else animatedDragOffset
//                            if (draggedTag!!.ordinal + (dragVerticalOffset / (rowHeight + (2 * rowPadding))).roundToInt() > tag.ordinal && draggedTag!!.ordinal < tag.ordinal) {
//                            -(rowHeight + (2 * rowPadding))
                    )
                    Log.i("TagsScreen.kt", "Animated Snapback: $animatedSnapback")


//                    Log.i("TagsScreen.kt", "Drag Ordinal: $dragOrdinal        Tag Ordinal: ${tag.ordinal}}")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = rowPadding)
                            .height(height = rowHeight)
                            .pointerInput(true) {
                                detectTapGestures(
                                    onLongPress = { offset ->
                                        longPressOffset = DpOffset(x = (offset.x/density.density).toDp(), y = (offset.y/density.density).toDp())
                                    }
                                )
                            }
                            .combinedClickable(
                                onClick = {
                                    onTagsEvent(TagsEvent.ShowTagDialog(tag = tag))
                                },
                                onLongClick = {
                                    isDropDownMenuVisible = true
                                }
                            )
                            .offset(
                                y = if (draggedTag == null) 0.dp else animatedDragOffset + animatedTagOffset

                            )
                            .draggable(
                                state = rememberDraggableState { delta ->
                                    dragVerticalOffset += (delta / density.density).dp
                                    dragOrdinalDisplacement =
                                        (dragVerticalOffset / (rowHeight + (2 * rowPadding))).roundToInt()
//                                    Log.i("TagsScreen.kt", "Drag Ordinal Displacement: △$dragOrdinalDisplacement")
                                    dragOrdinal = if (draggedTag == null) 0 else min(
                                        a = dragOrdinalDisplacement + draggedTag!!.ordinal,
                                        b = tagsState.tagList.size
                                    )
                                },
                                orientation = Orientation.Vertical,
                                onDragStarted = { _ ->
                                    draggedTag = tag
                                },
                                onDragStopped = { _ ->
                                    draggedTag = null
//                                    Log.i("TagsScreen.kt", "Delta: $dragVerticalOffset")
                                    val ordinal: Int = min(
                                        a = tag.ordinal + dragOrdinalDisplacement,
                                        b = tagsState.tagList.size
                                    )
                                    dragOrdinalDisplacement = 0
                                    dragVerticalOffset = 0.dp//(ordinalDisplacement*rowHeight)
//                                    Log.i("TagsScreen.kt", "Delta Snap: ${dragOrdinalDisplacement * (rowHeight + (2 * rowPadding)) * density.density}")
                                    onTagsEvent(
                                        TagsEvent.UpdateOrdinals(
                                            tag = tag,
                                            ordinal = ordinal
                                        )
                                    )
                                }
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.3f)
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "%03d", tag.tagId),
                                fontSize = 26.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = tag.tagName,
                                fontSize = 26.sp
                            )
                        }
                        DropdownMenu(
                            expanded = isDropDownMenuVisible,
                            onDismissRequest = { isDropDownMenuVisible = false },
                            offset = longPressOffset
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Remove") },
                                onClick = {
                                    onTagsEvent(TagsEvent.DeleteTag(tag = tag))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagScreenPreview() {
    VendorFlowTheme {


        val tag1 = Tag(
            tagId = 1,
            tagName = "Sanrio",
            ordinal = 1
        )
        val tag2 = Tag(
            tagId = 2,
            tagName = "Astrology",
            ordinal = 2
        )
        TagScreen(
            navController = rememberNavController(),
            onTagsEvent = {  },
            tagsState = TagsState(
                tagList = listOf(
                    tag1, tag2
                )
            )
        )
    }
}