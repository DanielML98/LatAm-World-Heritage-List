package com.danielml.latinamericaworldheritage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.danielml.latinamericaworldheritage.Model.Heritage
import com.danielml.latinamericaworldheritage.databinding.ActivityEditBinding
import com.danielml.latinamericaworldheritage.db.DBHelper

class EditActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEditBinding

  private lateinit var dbHelper: DBHelper
  var editableHeritage: Heritage? = null
  var id: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditBinding.inflate(layoutInflater)
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

    editableHeritage = dbHelper.getHeritageWith(id)

    if(editableHeritage != null){
      with(binding){
        heritageNameEditText.setText(editableHeritage?.name)
        ecosystemEditText.setText(editableHeritage?.ecosystem)
      }
    }
    setUpSpinner()
  }

  private fun setUpSpinner() {
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

  fun editHeritageTapped(view: View) {
    with(binding){
      if(allFieldsAreFilled()){
        if(dbHelper.editHeritage(id, heritageNameEditText.text.toString(), spinner.selectedItem.toString(), ecosystemEditText.text.toString())){
          Toast.makeText(this@EditActivity, R.string.successful_update, Toast.LENGTH_LONG).show()
          val intent = Intent(this@EditActivity, DetailActivity::class.java)
          intent.putExtra("ID", id)
          startActivity(intent)
          finish()
        }else{
          Toast.makeText(this@EditActivity, R.string.unsuccessful_update, Toast.LENGTH_LONG).show()
        }
      }else{
        Toast.makeText(this@EditActivity, R.string.incomplete_fields, Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun allFieldsAreFilled(): Boolean {
    with(binding) {
      return !heritageNameEditText.text.isNullOrBlank() &&
              !ecosystemEditText.text.isNullOrBlank() &&
              spinner.selectedItemPosition != 0
    }
  }
}

