package uk.ac.tees.mad.s3616168

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

sealed interface DetailState {
    object Loading : DetailState
    data class Success(val product: ProductDto) : DetailState
    object NotFound : DetailState
    object Error : DetailState
}

class ProductViewModel : ViewModel() {
    var state by mutableStateOf<DetailState>(DetailState.Loading)
        private set

    private var loadedBarcode: String? = null

    fun load(barcode: String) {
        if (barcode == loadedBarcode && state is DetailState.Success) return
        loadedBarcode = barcode
        state = DetailState.Loading
        viewModelScope.launch {
            state = try {
                val response = ApiClient.api.getProduct(barcode, FIELDS)
                val product = response.product
                if (response.status == 1 && product != null) {
                    DetailState.Success(product)
                } else {
                    DetailState.NotFound
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                if (e.code() == 404) DetailState.NotFound else DetailState.Error
            } catch (e: Exception) {
                DetailState.Error
            }
        }
    }

    companion object {
        private const val FIELDS =
            "product_name,brands,image_url,nutriscore_grade,ingredients_text,allergens_tags,nutriments"
    }
}