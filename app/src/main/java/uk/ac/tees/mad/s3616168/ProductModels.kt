package uk.ac.tees.mad.s3616168

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val status: Int = 0,
    val product: ProductDto? = null
)

data class ProductDto(
    @SerializedName("product_name") val name: String? = null,
    val brands: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("nutriscore_grade") val nutriScore: String? = null,
    @SerializedName("ingredients_text") val ingredients: String? = null,
    @SerializedName("allergens_tags") val allergens: List<String>? = null,
    val nutriments: NutrimentsDto? = null
)

data class NutrimentsDto(
    @SerializedName("energy-kcal_100g") val energyKcal: Double? = null,
    @SerializedName("fat_100g") val fat: Double? = null,
    @SerializedName("sugars_100g") val sugars: Double? = null,
    @SerializedName("salt_100g") val salt: Double? = null,
    @SerializedName("proteins_100g") val proteins: Double? = null
)