package com.example.foodrecipes.models


import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonObject
@Entity(tableName = "recipes")
data class Recipe(
    @JsonField(name = ["f2f_url"])
    var f2fUrl: String = "",

    @ColumnInfo(name = "image_url")
    @JsonField(name = ["image_url"])
    var imageUrl: String = "",

    @ColumnInfo(name = "publisher")
    @JsonField(name = ["publisher"])
    var publisher: String = "",

    @ColumnInfo(name = "publisher_url")
    @JsonField(name = ["publisher_url"])
    var publisherUrl: String = "",

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_id")
    @JsonField(name = ["recipe_id"])
    var recipeId: String = "",

    @ColumnInfo(name = "social_rank")
    @JsonField(name = ["social_rank"])
    var socialRank: Float = 0f,

    @ColumnInfo(name = "source_url")
    @JsonField(name = ["source_url"])
    var sourceUrl: String = "",

    @ColumnInfo(name = "title")
    @JsonField(name = ["title"])
    var title: String = "",

    @ColumnInfo(name = "ingredients")
    @JsonField(name = ["ingredients"])
    var ingredients: List<String> = listOf(),

    @ColumnInfo(name = "timestamp")
    var timestamp: Int = 0

) : Parcelable