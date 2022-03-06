package com.danielml.latinamericaworldheritage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.danielml.latinamericaworldheritage.Model.Heritage
import com.danielml.latinamericaworldheritage.R
import com.danielml.latinamericaworldheritage.databinding.ActivityDetailBinding
import com.danielml.latinamericaworldheritage.db.DBHelper

class DetailActivity : AppCompatActivity() {

  private lateinit var dbHelper: DBHelper
  private lateinit var binding: ActivityDetailBinding
  private var detailedHeritage: Heritage? = null
  private var id: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if(savedInstanceState == null){
      val bundle = intent.extras
      if(bundle != null){
        id = bundle.getInt("ID", 0)
      }
    }else{
      id = savedInstanceState.getSerializable("ID") as Int
    }

    dbHelper = DBHelper(this)

    detailedHeritage = dbHelper.getHeritageWith(id)

    if(detailedHeritage != null){
      with(binding){
        nameTextView.text = detailedHeritage?.name
        countryNameTextView.text = detailedHeritage?.country
        ecosystemNameTextView.text = detailedHeritage?.ecosystem
        val resourceName = resources.getIdentifier(detailedHeritage?.country?.let {
          getFlagResourceGiven(it)
        }, "drawable", packageName)
        countryFlagImageview.setImageResource(resourceName)

      }
    }
  }


  private fun getFlagResourceGiven(countryName: String): String {
    return "flag_${countryName.lowercase().replace(" ", "_")}"
  }

  override fun onBackPressed() {
    super.onBackPressed()
    startActivity(Intent(this, MainActivity::class.java))
  }

  fun editHeritageTapped(view: View) {
    val intent = Intent(this, EditActivity::class.java)
    intent.putExtra("ID", id)
    startActivity(intent)
    finish()
  }
  fun deleteHeritageTapped(view: View) {
    AlertDialog.Builder(this)
      .setTitle(R.string.delete)
      .setMessage(R.string.deletion_confirmation)
      .setPositiveButton(R.string.sure_button) { dialogInterface, i ->
        if (dbHelper.deleteHeritage(id)) {
          Toast.makeText(this, R.string.successful_deletion, Toast.LENGTH_LONG).show()
          startActivity(Intent(this, MainActivity::class.java))
          finish()
        }
      }
      .setNegativeButton(R.string.unsure_button) { dialogInterface, i ->

      }
      .show()
  }
}