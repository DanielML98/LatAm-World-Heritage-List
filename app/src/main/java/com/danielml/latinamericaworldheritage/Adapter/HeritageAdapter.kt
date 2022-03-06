package com.danielml.latinamericaworldheritage.Adapter

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.danielml.latinamericaworldheritage.Model.Heritage
import com.danielml.latinamericaworldheritage.R
import com.danielml.latinamericaworldheritage.databinding.HeritageCellBinding

class HeritageAdapter(context: Context, data: ArrayList<Heritage>): BaseAdapter() {

  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
  private val heritageData = data
  private val context = context

  override fun getCount(): Int {
    return heritageData.size
  }

  override fun getItem(position: Int): Any {
    return heritageData[position]
  }

  override fun getItemId(position: Int): Long {
    return heritageData[position].id.toLong()
  }

  override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
    val binding: HeritageCellBinding = HeritageCellBinding.inflate(layoutInflater)
    with(binding) {
      val countryName: String = heritageData[position].country
      heritageNameTextView.text = heritageData[position].name
      countryTextView.text = countryName
      ecosystemTextView.text = heritageData[position].ecosystem
      val resourceName = context.resources.getIdentifier(getFlagResourceGiven(countryName), "drawable", context.packageName)
      countryFlagImageView.setImageResource(resourceName)
    }
    return binding.root
  }

  private fun getFlagResourceGiven(countryName: String): String {
    return "flag_${countryName.lowercase().replace(" ", "_")}"
  }
}