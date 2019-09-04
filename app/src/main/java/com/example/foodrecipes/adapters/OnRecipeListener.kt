package com.example.foodrecipes.adapters

interface OnRecipeListener {

    fun onRecipeSelected(position: Int)
    fun onCategorySelected(category: String)
}