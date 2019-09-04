package com.example.foodrecipes.requests.responses

import android.os.Parcelable
import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.example.foodrecipes.models.Recipe
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonObject
data class RecipeResponse(
    @JsonField(name = ["recipe"])
    var recipe: Recipe? = null
) : Parcelable