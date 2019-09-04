package com.example.foodrecipes.util

object Constants {
    const val BASE_URL: String = "https://www.food2fork.com"

    // You need your own API key
//    const val API_KEY: String = "e421a990d9575e723a699d3738197753"
    const val API_KEY: String = "67aa2b5b64a915bca5e5c50bc6b19fc4"
    const val NETWORK_TIMEOUT: Long = 3000

    val DEFAULT_SEARCH_CATEGORY = arrayOf(
        "barbeque", "breakfast", "chicken", "beef", "brunch", "dinner", "wine", "italian"
    )

    val DEFAULT_SEARCH_CATEGORY_IMAGES = arrayOf(
        "barbeque",
        "breakfast",
        "chicken",
        "beef",
        "brunch",
        "dinner",
        "wine",
        "italian"
    )

    const val INTENT_RECIPE = "recipe"
}