package cz.cvut.bekalim.app.data.review

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    viewModel: AddReviewViewModel,
    onBack:   () -> Unit,
    onDone:   () -> Unit
) {
    val ctx = LocalContext.current
    val focus = LocalFocusManager.current

    // делаем временный файл для камеры
    val photoFile = remember {
        File(ctx.cacheDir, "tmp_review.jpg").apply { createNewFile() }
    }
    val photoUri = remember {
        FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", photoFile)
    }

    // лаунчер для ActivityResult: взять фото
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.photoUri = photoUri
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Add Review") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) { padding ->
        Column(
            Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { viewModel.text = it },
                label = { Text("Your review") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
            // кнопку «сфотографировать»
            Button(onClick = {
                // запрашиваем разрешения CAMERA+WRITE if нужно...
                cameraLauncher.launch(photoUri)
            }) {
                Text("Add photo")
            }
            viewModel.photoUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Review photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            viewModel.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { focus.clearFocus(); viewModel.submit { if(it) onDone() } },
                enabled = !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Submit")
                }
            }
        }
    }
}