package uk.ac.tees.mad.s3616168

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Food Label Scanner", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun ScanScreen(onLookup: (String) -> Unit, onHistory: () -> Unit) {
    var barcode by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Scan a product", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Barcode") },
            singleLine = true
        )
        Button(onClick = { onLookup(barcode) }, enabled = barcode.length >= 8) {
            Text("Look up product")
        }
        TextButton(onClick = onHistory) { Text("View history") }
    }
}

@Composable
fun DetailScreen(
    barcode: String,
    onBack: () -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    LaunchedEffect(barcode) { viewModel.load(barcode) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Product details", style = MaterialTheme.typography.headlineMedium)
        Text("Barcode: $barcode")

        when (val s = viewModel.state) {
            is DetailState.Success -> ProductInfo(s.product)
            else -> Text("Loading...")
        }

        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun ProductInfo(product: ProductDto) {
    val imageUrl = product.imageUrl
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = product.name,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
    }
    Text(product.name ?: "Unnamed product", style = MaterialTheme.typography.headlineSmall)
    product.brands?.let { Text(it) }
    product.nutriScore?.let { Text("Nutri-Score: ${it.uppercase()}") }
}