package com.example.foodrecipes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.repositories.RecipeRepository
import com.example.foodrecipes.util.Resource

class RecipeListVM(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "RecipeListVM"
    }

    enum class ViewState {
        CATEGORIES, RECIPES
    }

    private var recipeRepository: RecipeRepository = RecipeRepository.getInstance(application)
    private var _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val viewState: LiveData<ViewState>
        get() = _viewState

    init {
        _viewState.value = ViewState.CATEGORIES
    }

    private var _recipes: MediatorLiveData<Resource<List<Recipe>>> = MediatorLiveData()
    val recipes: LiveData<Resource<List<Recipe>>>
        get() = _recipes

    fun searchRecipeApi(query: String, pageNumber: Int) {
        val repositorySource: LiveData<Resource<List<Recipe>>> =
            recipeRepository.searchRecipesApi(query, pageNumber)
        _recipes.addSource(repositorySource) {

            // react to data
            _recipes.value = it
        }
    }

}