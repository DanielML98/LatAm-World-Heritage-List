package com.danielml.latinamericaworldheritage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danielml.latinamericaworldheritage.databinding.ActivityAddBinding
import com.danielml.latinamericaworldheritage.db.DBHelper


class AddActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddBinding.inflate(layoutInflater)
    ArrayAdapter.createFromResource(
      this,
      R.array.countries_array,
      android.R.layout.simple_spinner_item
    ).also { adapter ->
      // Specify the layout to use when the list of choices appears
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      // Apply the adapter to the spinner
      binding.spinner.adapter = adapter
    }
    setContentView(binding.root)
  }

  fun addHeritageTapped(view: View) {
    if(allFieldsAreFilled()) {
      val dbHelper: DBHelper = DBHelper(this)
      val operationCode = dbHelper.addHeritage(binding.heritageNameEditText.text.toString(), binding.spinner.selectedItem.toString(), binding.ecosystemEditText.text.toString())
      if(operationCode > 0) {
        Toast.makeText(this, R.string.success_at_adding_heritage, Toast.LENGTH_SHORT).show()
        cleanForm()
        onBackPressed()
      }
    } else {
      Toast.makeText(this, R.string.incomplete_fields, Toast.LENGTH_SHORT).show()
    }
  }

  private fun allFieldsAreFilled(): Boolean {
    with(binding) {
      return !heritageNameEditText.text.isNullOrBlank() &&
              !ecosystemEditText.text.isNullOrBlank() &&
              spinner.selectedItemPosition != 0
    }
  }

  private fun cleanForm() {
    binding.heritageNameEditText.setText("")
    binding.ecosystemEditText.setText("")
    binding.spinner.setSelection(0)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    startActivity(Intent(this, MainActivity::class.java))
  }

}