package com.example.debtabase

import android.os.Bundle
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

    }
    private fun addDebtData(){

    }
    private fun ShowData() {
        dbRefReg!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    spinnerList.add(item.child("cusFN").value.toString() +" "+ item.child("cusLN").value.toString())
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
