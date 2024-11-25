package com.example.akhleshkumar.homedootpartner.adaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.akhleshkumar.homedootpartner.models.Category

class CategorySpinnerAdapter(context: Context, private val categoryList: List<Category>) :
    ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, categoryList) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categoryList[position].category_name // Show the city name in the spinner
        return view
    }

    fun getCategory(position: Int): String {
        return categoryList[position].category_name
    }
}
