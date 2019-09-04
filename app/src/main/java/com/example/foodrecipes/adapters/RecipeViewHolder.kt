package com.example.foodrecipes.adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipes.R
import com.example.foodrecipes.models.Recipe

class RecipeViewHolder(
    view: View,
    onRecipeListener: OnRecipeListener
) : RecyclerView.ViewHolder(view) {

    private val recipeTitle by lazy { view.findViewById<TextView>(R.id.recipe_title) }
    private val recipePublisher by lazy { view.findViewById<TextView>(R.id.recipe_publisher) }
    private val recipeSocialScore by lazy { view.findViewById<TextView>(R.id.recipe_social_score) }
    private val recipeImage by lazy { view.findViewById<ImageView>(R.id.recipe_image) }

    init {
        view.setOnClickListener { onRecipeListener.onRecipeSelected(adapterPosition) }
    }

    fun bind(
        recipe: Recipe,
        context: Context
    ) {
        val requestOptions = RequestOptions()
        Glide.with(context).setDefaultRequestOptions(requestOptions)
            .load(recipe.imageUrl)
            .dontAnimate()
            .into(recipeImage)
        recipeTitle.text = recipe.title
        recipePublisher.text = recipe.publisher
        recipeSocialScore.text = recipe.socialRank.toString()
    }
}