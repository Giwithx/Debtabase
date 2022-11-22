package com.example.debtabase

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.collections.ArrayList


class DebtActivity : AppCompatActivity() {

    private lateinit var DebtSpinner: Spinner
    private lateinit var DebtProduct: EditText
    private lateinit var DebtProdPrice: EditText
    private lateinit var DebtDueDate: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSave: Button
    private lateinit var spinnerList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var DebtContainer: String


    private lateinit var dbRefReg: DatabaseReference
    private lateinit var dbRefDebt: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt)
        val actionbar = supportActionBar
        actionbar!!.title = "Debt"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        dbRefReg = FirebaseDatabase.getInstance().getReference("CustomerRegistration")
        dbRefDebt = FirebaseDatabase.getInstance().getReference("CustomerDebt")

        DebtSpinner = findViewById(R.id.textInputLayout2)
        DebtProduct = findViewById(R.id.txtProdName)
        DebtProdPrice = findViewById(R.id.txtPricesInp)
        DebtDueDate = findViewById(R.id.txtDateInp)
        btnAdd = findViewById(R.id.btnAdd)
        btnSave = findViewById(R.id.btnSaveProd)


        spinnerList = ArrayList()
        adapter = ArrayAdapter(this@DebtActivity, android.R.layout.simple_spinner_dropdown_item,
            spinnerList
        )

        DebtSpinner.adapter = adapter
        ShowData()

        btnSave.setOnClickListener {
            saveDebtData()
        }
        btnAdd.setOnClickListener {
            addDebtData()
        }

    }
    private fun saveDebtData(){
        val DebtFN = DebtContainer
        val DebtProd = DebtProduct.text.toString()
        val DebtPrice = DebtProdPrice.text.toString()
        val DebtDate = DebtDueDate.text.toString()
        val DebtId = dbRefDebt.push().key!!

        if(DebtProd.isEmpty()){
            DebtProduct.error = "Please enter product."
        }
        if(DebtPrice.isEmpty()){
            DebtProdPrice.error = "Please enter product price"
        }
        if(DebtDate.isEmpty()){
            DebtDueDate.error = "Please enter due date."
        }

        val debtlist = CustomerDebtModel(DebtId, DebtFN, DebtProd, DebtPrice, DebtDate)

        if(DebtProd.isNotEmpty() && DebtPrice.isNotEmpty() && DebtDate.isNotEmpty()){
            dbRefDebt.child(DebtId).setValue(debtlist).addOnCompleteListener {
                Toast.makeText(this, "Data Successfully added.", Toast.LENGTH_LONG).show()

                DebtProduct.text.clear()
                DebtProdPrice.text.clear()
                DebtDueDate.text.clear()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun addDebtData(){

    }
    private fun ShowData() {
        dbRefReg!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    spinnerList.add(item.child("cusFN").value.toString() +" "+ item.child("cusLN").value.toString())
                    DebtSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            DebtContainer = DebtSpinner.selectedItem.toString()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
