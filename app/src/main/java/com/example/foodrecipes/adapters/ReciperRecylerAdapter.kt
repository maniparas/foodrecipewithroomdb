package com.example.foodrecipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.R
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.util.Constants

class ReciperRecylerAdapter(private val onRecipeListener: OnRecipeListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val RECIPE_TYPE = 1
        const val LOADING_TYPE = 2
        const val CATEGORY_TYPE = 3
        const val EXHAUSTED_TYPE = 4
        const val TEXT_LOADING = "LOADING..."
        const val TEXT_EXHAUSTED = "EXHAUSTED..."
    }

    private var mRecipes = mutableListOf<Recipe>()

    fun setData(recipeList: MutableList<Recipe>) {
        mRecipes = recipeList
        notifyDataSetChanged()
    }

    private fun isLoading(): Boolean {
        if (mRecipes.isNotEmpty()) {
            if (mRecipes[mRecipes.size - 1].title == TEXT_LOADING) {
                return true
            }
        }
        return false
    }

    fun displayLoading() {
        if (!isLoading()) {
            val recipe = Recipe()
            recipe.title = TEXT_LOADING
            val loadingList = mutableListOf<Recipe>()
            loadingList.add(recipe)
            mRecipes = loadingList
            notifyDataSetChanged()
        }
    }

    fun displaySearchCategories() {
        val categories = arrayListOf<Recipe>()
        for (i in Constants.DEFAULT_SEARCH_CATEGORY.indices) {
            val recipe = Recipe()
            recipe.title = Constants.DEFAULT_SEARCH_CATEGORY[i]
            recipe.imageUrl = Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]
            recipe.socialRank = -1f
            categories.add(recipe)
        }
        mRecipes = categories
        notifyDataSetChanged()
    }

    fun setQueryExhausted() {
        hideLoading()
        val exhaustedRecipe = Recipe()
        exhaustedRecipe.title = TEXT_EXHAUSTED
        mRecipes.add(exhaustedRecipe)
        notifyDataSetChanged()
    }

    private fun hideLoading() {
        if (isLoading()) {
            for (recipe in mRecipes) {
                if (recipe.title == TEXT_LOADING) {
                    mRecipes.remove(recipe)
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recipe_list_item, parent, false)
                return RecipeViewHolder(view, onRecipeListener)
            }
            2 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_loading_list_item, parent, false)
                return LoadingViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_category_list_item, parent, false)
                return CategoryViewHolder(view, onRecipeListener)
            }
            4 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_search_exhausted, parent, false)
                return SearchExhaustedViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recipe_list_item, parent, false)
                return RecipeViewHolder(view, onRecipeListener)
            }
        }
    }

    private fun isCategoryType(position: Int) = mRecipes[position].socialRank == -1f
    private fun isLoadingType(position: Int) = mRecipes[position].title == TEXT_LOADING
    private fun isExhaustedType(position: Int) = mRecipes[position].title == TEXT_EXHAUSTED
    private fun isLastItem(position: Int) =
        position == mRecipes.size - 1
                && position != 0
                && mRecipes[position].title != TEXT_EXHAUSTED

    fun getSelectedRecipe(position: Int) = mRecipes[position]

    override fun getItemViewType(position: Int): Int {
        return when {
            isCategoryType(position) -> CATEGORY_TYPE
            isExhaustedType(position) -> EXHAUSTED_TYPE
            isLoadingType(position) or isLastItem(position) -> LOADING_TYPE
            else -> RECIPE_TYPE
        }
    }

    override fun getItemCount(): Int = mRecipes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            RECIPE_TYPE -> (holder as RecipeViewHolder).bind(
                mRecipes[position],
                holder.itemView.context
            )
            CATEGORY_TYPE -> (holder as CategoryViewHolder).bind(
                mRecipes[position],
                holder.itemView.context
            )
        }
    }
}
