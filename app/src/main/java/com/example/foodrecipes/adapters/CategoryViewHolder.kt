package com.example.foodrecipes.adapters

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipes.R
import com.example.foodrecipes.models.Recipe
import de.hdodenhof.circleimageview.CircleImageView

class CategoryViewHolder(
    view: View,
    onRecipeListener: OnRecipeListener
) : RecyclerView.ViewHolder(view) {
    private val categoryImage by lazy { view.findViewById<CircleImageView>(R.id.imageCategory) }
    private val categoryTitle by lazy { view.findViewById<AppCompatTextView>(R.id.categoryTitle) }

    init {
        view.setOnClickListener { onRecipeListener.onCategorySelected(categoryTitle.text.toString()) }
    }

    fun bind(recipe: Recipe, context: Context) {
        val requestOptions = RequestOptions()
        Glide.with(context).setDefaultRequestOptions(requestOptions)
            .load(getImage(context, recipe.imageUrl))
            .dontAnimate()
            .into(categoryImage)
        categoryTitle.text = recipe.title
    }

    private fun getImage(context: Context, imageName: String): Int =
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
}