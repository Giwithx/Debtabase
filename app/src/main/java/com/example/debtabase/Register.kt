package com.example.debtabase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Register : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var txtFName: EditText
    private lateinit var txtLName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var btnSave: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtFName = findViewById(R.id.txtFN)
        txtLName = findViewById(R.id.txtLN)
        txtPhone = findViewById(R.id.txtPhoneNum)
        btnSave = findViewById(R.id.btnFirebaseReg)

        dbRef = FirebaseDatabase.getInstance().getReference("CustomerRegistration")

        btnSave.setOnClickListener {
            saveCustomerData()
        }
        val actionbar = supportActionBar
        actionbar!!.title = "Registration"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }
    private fun saveCustomerData(){
        val cusFN = txtFName.text.toString()
        val cusLN = txtLName.text.toString()
        val cusPN = txtPhone.text.toString()

        if(cusFN.isEmpty()){
            txtFName.error = "Please enter first name."
        }
        if(cusLN.isEmpty()){
            txtLName.error = "Please enter last name"
        }
        if(cusPN.isEmpty() || cusPN.length != 11){
            txtPhone.error = "Please enter phone number."
        }

        val cusId = dbRef.push().key!!

        val customerreg = CustomerModel(cusId, cusFN, cusLN, cusPN)

        if(cusFN.isNotEmpty() && cusLN.isNotEmpty() && cusPN.isNotEmpty() && cusPN.length == 11){
            dbRef.child(cusId).setValue(customerreg)
                .addOnCompleteListener{
                    Toast.makeText(this, "Registered Successfully.", Toast.LENGTH_LONG).show()

                    txtFName.text.clear()
                    txtLName.text.clear()
                    txtPhone.text.clear()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}