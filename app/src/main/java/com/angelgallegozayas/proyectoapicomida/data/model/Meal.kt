package com.angelgallegozayas.proyectoapicomida.data.model

import android.R.attr.category
import android.R.attr.description
import android.R.attr.rating

data class Meal(
    val idMeal: String = "",
    val strMeal: String= "",
    val strMealDescription: String= "",
    val strMealThumb : String= "",
    val strInstructions: String= "",
    val strDescription2: String= "",
    val strDescription3: String= "",
    val strDescription4: String= "",
    val strIngredient1 : String= "",
    val strIngredient2 : String= "",
    val strIngredient3 : String= "",
    val strIngredient4 : String= "",
    val strIngredient5 : String= "",
    val strIngredient6 : String= "",
    val strIngredient7 : String= "",
    val strIngredient8 : String= "",
    val strIngredient9 : String= "",
    val strIngredient10 : String= "",
    val strIngredient11 : String= "",
    val strIngredient12 : String= "",
    val strIngredient13 : String= "",
    val strIngredient14 : String= "",
    val strIngredient15 : String= "",
    val strIngredient16 : String= "",
    val strIngredient17 : String= "",
    val strIngredient18 : String= "",
    val strIngredient19 : String= "",
    val strIngredient20 : String= ""


){
    fun toMealItem(): Meal {
        return Meal(
            idMeal = idMeal,
            strMeal = strMeal,
            strMealThumb = strMealThumb,
            strMealDescription = strMealDescription,
            strInstructions = strInstructions,
            strDescription2 = strDescription2,
            strDescription3 = strDescription3,
            strDescription4 = strDescription4,
            strIngredient1 = strIngredient1,
            strIngredient2 = strIngredient2,
            strIngredient3 = strIngredient3,
            strIngredient4 = strIngredient4,
            strIngredient5 = strIngredient5,
            strIngredient6 = strIngredient6,
            strIngredient7 = strIngredient7,
            strIngredient8 = strIngredient8,
            strIngredient9 = strIngredient9,
            strIngredient10 = strIngredient10,
            strIngredient11 = strIngredient11,
            strIngredient12 = strIngredient12,
            strIngredient13 = strIngredient13,
            strIngredient14 = strIngredient14,
            strIngredient15 = strIngredient15,
            strIngredient16 = strIngredient16,
            strIngredient17 = strIngredient17,
            strIngredient18 = strIngredient18,
            strIngredient19 = strIngredient19,
            strIngredient20 = strIngredient20

        )
    }
}