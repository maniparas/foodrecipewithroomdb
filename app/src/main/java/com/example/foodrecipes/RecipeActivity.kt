package com.example.foodrecipes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.viewmodels.RecipeVM
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : BaseActivity() {

    private lateinit var mRecipeVM: RecipeVM

    companion object {
        const val TAG = "RecipeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        init()
        getIncomingIntent()
    }

    private fun init() {
        mRecipeVM = ViewModelProvider(this).get(RecipeVM::class.java)
    }

    private fun getIncomingIntent() {
        intent.hasExtra("recipe").let {
            val recipe: Recipe = intent.getParcelableExtra("recipe")!!
            Log.d(TAG, "getIncomingIntent: ${recipe.title}")
        }
    }

    private fun showParent() {
        scrollParent.visibility = View.VISIBLE
    }
}