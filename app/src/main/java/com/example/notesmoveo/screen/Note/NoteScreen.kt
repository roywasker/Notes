package com.example.notesmoveo.screen.Note

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.notesmoveo.data.model.RequestState
import com.example.notesmoveo.screen.component.CustomTextField
import com.example.notesmoveo.screen.component.CustomButton
import com.example.notesmoveo.ui.theme.GrayDarker
import com.example.notesmoveo.ui.theme.Orange
import com.example.notesmoveo.utils.PhotoPicker
import com.example.notesmoveo.utils.Resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    id: String?,
    navController: NavHostController
){

    val viewModel = koinViewModel<NoteViewModel>()
    val isFormValid = viewModel.isFormValid
    val uploadImageState = viewModel.uploadImageState
    val noteState = viewModel.noteState
    val photoPicker = koinInject<PhotoPicker>()
    var snackMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            snackMessage = "You have not approved permission for location.\n" +
                    "You can create notes without a location."
        }
    }

    LaunchedEffect(Unit) {
        if (id != null) {
            viewModel.fetchNoteById(id)
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(snackMessage) {
        if (snackMessage != null) {
            delay(3000)
            snackMessage = null
        }
    }

    photoPicker.InitializePhotoPicker(
        onImageSelect = { uri ->
            viewModel.uploadImageToStorage(
                uri = uri,
                onSuccess = {
                    snackMessage = "Image uploaded successfully"
                }
            )
        }
    )

    if (isLoading) {
        Popup(
            alignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Orange)
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = Resources.Note.noteTitle,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = Orange
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            modifier = Modifier.size(size = 26.dp),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back icon",
                        )
                    }
                }
            )
        }
    ){ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 24.dp,
                    horizontal = 12.dp
                )
        ) {
            snackMessage?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 90.dp),
                    containerColor = Color.Gray,
                ) {
                    Text(text = snackMessage!!, color = Color.White)
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp
                )
                .padding(
                    top = 166.dp,
                    bottom = 24.dp
                )
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(size = 12.dp))
                        .border(
                            width = 1.dp,
                            color = GrayDarker,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    when (uploadImageState) {
                        is RequestState.Idle -> {
                            Icon(
                                modifier = Modifier.size(24.dp)
                                    .clickable(onClick = {
                                        photoPicker.open()
                                    }),
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add icon",
                            )
                        }
                        is RequestState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Orange,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        is RequestState.Success -> {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(noteState.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop
                            )
                        }
                        is RequestState.Error -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uploadImageState.message,
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                TextButton(
                                    onClick = {
                                        viewModel.updateImageUploaderState(RequestState.Idle)
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = Color.Transparent,
                                    )
                                ) {
                                    Text(
                                        text = Resources.Note.tryAgain,
                                        fontSize = 12.sp,
                                        color = Color.Black.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }

                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val formattedDate = formatter.format(Date(noteState.date))

                CustomTextField(
                    value = formattedDate,
                    onValueChange = {},
                    enabled = false,
                    placeholder = Resources.Note.date,
                )

                CustomTextField(
                    value = noteState.title,
                    onValueChange = viewModel::updateTitle,
                    placeholder = Resources.Note.title
                )

                CustomTextField(
                    modifier = Modifier
                        .height(170.dp),
                    value = noteState.body,
                    onValueChange = viewModel::updateBody,
                    placeholder = Resources.Note.body,
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                CustomButton(
                    modifier = Modifier
                        .weight(0.5f),
                    text = Resources.Note.saveButton,
                    enable = isFormValid,
                    onClick = {
                        isLoading = true
                        if (id == null) {
                            viewModel.createNote(
                                onSuccess = {
                                    isLoading = false
                                    snackMessage = "Note created successfully"
                                    navController.previousBackStackEntry?.savedStateHandle?.set("needToReFetch",true)
                                },
                                onError = { message ->
                                    isLoading = false
                                    snackMessage = message
                                }
                            )
                        } else{
                            viewModel.updateNote(
                                onSuccess = {
                                    isLoading = false
                                    snackMessage = "Note updated successfully"
                                    navController.previousBackStackEntry?.savedStateHandle?.set("needToReFetch",true)
                                },
                                onError = { message ->
                                    isLoading = false
                                    snackMessage = message
                                }
                            )
                        }
                    }
                )
                if (id != null) {
                    CustomButton(
                        modifier = Modifier
                            .weight(0.5f),
                        text = Resources.Note.deleteButton,
                        onClick = {
                            isLoading = true
                            viewModel.deleteNoteById(
                                id = id,
                                noteImagePath = noteState.imageUrl,
                                onSuccess = {
                                    isLoading = false
                                    snackMessage = "Note deleted successfully"
                                    coroutineScope.launch {
                                        delay(2000)
                                        navController.previousBackStackEntry?.savedStateHandle?.set("needToReFetch",true)
                                        navController.navigateUp()
                                    }
                                },
                                onError = { message ->
                                    isLoading = false
                                    snackMessage = message
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}