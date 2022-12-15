package com.example.debtabase.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.debtabase.model.CustomerDebtListModel
import com.example.debtabase.model.CustomerDebtModel
import com.example.debtabase.R
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class DebtActivity : AppCompatActivity() {

    private lateinit var DebtSpinner: AutoCompleteTextView
    private lateinit var DebtProduct: EditText
    private lateinit var DebtProdPrice: EditText
    private lateinit var tvDate: TextView
    private lateinit var btnDate: Button
    private lateinit var btnAdd: Button
    private lateinit var btnClear: Button
    private lateinit var spinnerList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var DebtContainer: String
    private lateinit var DateContainer: String

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

        DebtSpinner = findViewById(R.id.DebtDropDown)
        DebtProduct = findViewById(R.id.txtProdName)
        DebtProdPrice = findViewById(R.id.txtPricesInp)
        tvDate = findViewById(R.id.tvDatePicker)
        btnDate = findViewById(R.id.btnDatePicker)
        btnAdd = findViewById(R.id.btnAdd)
        btnClear = findViewById(R.id.btnClearData)

        spinnerList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)

        DebtSpinner.setAdapter(adapter)
        ShowData()

        btnDate.setOnClickListener {
            datepicker()
        }

        btnClear.setOnClickListener {
            if(::DebtContainer.isInitialized && ::DateContainer.isInitialized){
                clearDebtData()
            }
            else{
                Toast.makeText(this@DebtActivity, "Please input data", Toast.LENGTH_SHORT).show()
            }
        }
        btnAdd.setOnClickListener {
            if(::DebtContainer.isInitialized && ::DateContainer.isInitialized){
                addDebtData()
            }
            else{
                Toast.makeText(this@DebtActivity, "Please input data", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun datepicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dateText = tvDate
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
            dateText.text = "$myear-${mmonth+1}-$mday"
            DateContainer = dateText.text.toString()
        },year,month,day).show()
    }
    private fun addDebtData(){
        val DebtFN = DebtContainer
        var DebtPrice = DebtProdPrice.text.toString().toFloatOrNull()
        val DebtDate = DateContainer
        val DebtProdList = DebtProduct.text.toString()
        val Date = tvDate
        if(DebtProdList.isEmpty()){
            DebtProduct.error = "Please enter product."
        }
        if(DebtPrice==null){
            DebtProdPrice.error = "Please enter product price"
        }
        if (Date.text.isEmpty() || Date.text.equals("Date")){
            Toast.makeText(this@DebtActivity, "Please input Date", Toast.LENGTH_SHORT).show()
        }
        val debtlist = CustomerDebtModel(DebtFN,DebtDate,DebtPrice)
        val debtprod = CustomerDebtListModel(DebtProdList, DebtPrice)
        dbRefDebt.child(DebtFN).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.hasChild(DebtDate)){
                    if(DebtPrice!=null && DebtDate.isNotEmpty() && DebtProdList.isNotBlank()){
                        dbRefDebt.child(DebtFN).child(DebtDate).setValue(debtlist).addOnCompleteListener {
                            Toast.makeText(this@DebtActivity, "Data Successfully added.", Toast.LENGTH_LONG).show()
                            DebtProdPrice.text.clear()
                            if(DebtPrice!=null && DebtDate.isNotEmpty() && DebtProdList.isNotBlank()){
                                dbRefDebt.child(DebtFN).child(DebtDate).child("debtProd").push().setValue(debtprod).addOnCompleteListener {
                                    DebtProduct.text.clear()
                                    for(item in snapshot.children){
                                        DebtPrice += item.child("debtBalance").getValue(Float::class.java)!!
                                    }
                                    dbRefDebt.child(DebtFN).child(DebtDate).child("debtBalance").setValue(DebtPrice)
                                }.addOnFailureListener { err3 ->
                                    Toast.makeText(this@DebtActivity, "Error ${err3.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }.addOnFailureListener { err ->
                            Toast.makeText(this@DebtActivity, "Error ${err.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                if(snapshot.hasChild(DebtDate)){
                    if(DebtPrice!=null && DebtDate.isNotEmpty() && DebtProdList.isNotBlank()){
                        dbRefDebt.child(DebtFN).child(DebtDate).child("debtProd").push().setValue(debtprod).addOnCompleteListener {
                            DebtProduct.text.clear()
                            DebtProdPrice.text.clear()
                            for(item in snapshot.children){
                                DebtPrice += item.child("debtBalance").getValue(Float::class.java)!!
                            }
                            dbRefDebt.child(DebtFN).child(DebtDate).child("debtBalance").setValue(DebtPrice)
                        }.addOnFailureListener { err3 ->
                            Toast.makeText(this@DebtActivity, "Error ${err3.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun clearDebtData(){
        val Date = tvDate

        Date.setText("Date")
        DebtProdPrice.text.clear()
        DebtProduct.text.clear()
    }
    private fun ShowData() {
        dbRefReg!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    spinnerList.add(item.child("cusFN").value.toString() +" "+ item.child("cusLN").value.toString())
                    DebtSpinner.setOnItemClickListener { adapterView, view, i, l ->
                        DebtContainer = adapterView.getItemAtPosition(i).toString()
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
