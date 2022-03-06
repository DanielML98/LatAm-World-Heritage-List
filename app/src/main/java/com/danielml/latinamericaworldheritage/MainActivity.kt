package com.danielml.latinamericaworldheritage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.danielml.latinamericaworldheritage.Adapter.HeritageAdapter
import com.danielml.latinamericaworldheritage.Model.Heritage
import com.danielml.latinamericaworldheritage.databinding.ActivityMainBinding
import com.danielml.latinamericaworldheritage.db.DBHelper

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var heritageList: ArrayList<Heritage>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setUpBinding()
    setUpListView()
    setUpItemClickListener()
  }

  private fun setUpItemClickListener() {
    binding.heritageListView.setOnItemClickListener { _, _, position, id ->
      val intent = Intent(this, DetailActivity::class.java)
      intent.putExtra("ID", id.toInt())
      startActivity(intent)
      finish()
    }
  }

  fun setUpBinding() {
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  fun setUpListView() {
    heritageList = DBHelper(this).getHeritages()
    if (heritageList.size == 0) {
      binding.emptyStateTextView.visibility = View.VISIBLE
    }
    val adapter: HeritageAdapter = HeritageAdapter(this, heritageList)
    binding.heritageListView.adapter = adapter
  }

  fun didTapAddButton(view: View) {
    startActivity(Intent(this, AddActivity::class.java))
    finish()
  }
}