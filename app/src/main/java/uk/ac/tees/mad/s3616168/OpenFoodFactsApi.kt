package uk.ac.tees.mad.s3616168

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {
    @Headers("User-Agent: FoodLabelScanner/0.2 (student project)")
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String
    ): ProductResponse
}

object ApiClient {
    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}