package com.example.foodrecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.adapters.OnRecipeListener
import com.example.foodrecipes.adapters.ReciperRecylerAdapter
import com.example.foodrecipes.util.Constants.INTENT_RECIPE
import com.example.foodrecipes.util.VerticalSpacingItemDecorator
import com.example.foodrecipes.util.printRecipe
import com.example.foodrecipes.viewmodels.RecipeListVM
import kotlinx.android.synthetic.main.activity_recipe_list.*

class RecipeListActivity : BaseActivity(), OnRecipeListener, SearchView.OnQueryTextListener {

    private lateinit var mRecipeListVM: RecipeListVM
    private lateinit var mAdapter: ReciperRecylerAdapter

    companion object {
        const val TAG = "RecipeListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        init()
    }

    private fun init() {
        mRecipeListVM = ViewModelProvider(this).get(RecipeListVM::class.java)
        initRecyclerView()
        viewSearch.setOnQueryTextListener(this)
        subscribeObservers()
        setSupportActionBar(toolbar)
    }

    private fun initRecyclerView() {
        mAdapter = ReciperRecylerAdapter(this@RecipeListActivity)
        recipeList.adapter = mAdapter
        recipeList.layoutManager = LinearLayoutManager(this)
        val verticalSpacingItemDecorator = VerticalSpacingItemDecorator(30)
        recipeList.addItemDecoration(verticalSpacingItemDecorator)
    }

    private fun subscribeObservers() {
        mRecipeListVM.recipes.observe(this, Observer {
            Log.d(TAG, "onChanged: status: $it")
            it.data?.let { listRecipe ->
                printRecipe(listRecipe, TAG)
            }
        })

        mRecipeListVM.viewState.observe(this, Observer {
            when (it) {
                RecipeListVM.ViewState.RECIPES -> {
                    // Recipes will show automatically from another observer
                }
                RecipeListVM.ViewState.CATEGORIES -> {
                    displaySearchCategories()
                }
            }
        })
    }

    private fun searchRecipeApi(query: String) {
        mRecipeListVM.searchRecipeApi(query, 1)
    }

    private fun displaySearchCategories() {
        mAdapter.displaySearchCategories()
    }

    override fun onRecipeSelected(position: Int) {
        Intent(this, RecipeActivity::class.java).apply {
            putExtra(INTENT_RECIPE, mAdapter.getSelectedRecipe(position))
            startActivity(this)
        }
    }

    override fun onCategorySelected(category: String) {
        searchRecipeApi(category)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchRecipeApi(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionCategories) {
            displaySearchCategories()
        }
        return super.onOptionsItemSelected(item)
    }
}