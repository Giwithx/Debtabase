package com.example.debtabase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class CustomerDetailsActivity: AppCompatActivity() {
    private lateinit var tvCusId: TextView
    private lateinit var tvCusFN: TextView
    private lateinit var tvCusLN: TextView
    private lateinit var tvCusPN: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cusId").toString(),
                intent.getStringExtra("cusFN").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cusId").toString()
            )
        }
        val actionbar = supportActionBar
        actionbar!!.title = "Customer Information"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }
    private fun openUpdateDialog(
        cusId: String,
        cusFN: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val txtFN = mDialogView.findViewById<EditText>(R.id.txtFN)
        val txtLN = mDialogView.findViewById<EditText>(R.id.txtLN)
        val txtPhoneNum = mDialogView.findViewById<EditText>(R.id.txtPhoneNum)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        txtFN.setText(intent.getStringExtra("cusFN").toString())
        txtLN.setText(intent.getStringExtra("cusLN").toString())
        txtPhoneNum.setText(intent.getStringExtra("cusPN").toString())

        mDialog.setTitle("Updating $cusFN Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                cusId,
                txtFN.text.toString(),
                txtLN.text.toString(),
                txtPhoneNum.text.toString()
            )

            Toast.makeText(applicationContext, "Customer Data Updated", Toast.LENGTH_LONG).show()

            tvCusFN.text = txtFN.text.toString()
            tvCusLN.text = txtLN.text.toString()
            tvCusPN.text = txtPhoneNum.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        first_name: String,
        last_name: String,
        phone_num: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerRegistration").child(id)
        val cusInfo = CustomerModel(id, first_name, last_name, phone_num)
        dbRef.setValue(cusInfo)
    }

    private fun initView() {
        tvCusId = findViewById(R.id.tvCusId)
        tvCusFN = findViewById(R.id.tvCusFN)
        tvCusLN = findViewById(R.id.tvCusLN)
        tvCusPN = findViewById(R.id.tvCusPN)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvCusId.text = intent.getStringExtra("cusId")
        tvCusFN.text = intent.getStringExtra("cusFN")
        tvCusLN.text = intent.getStringExtra("cusLN")
        tvCusPN.text = intent.getStringExtra("cusPN")

    }

    private fun deleteRecord(id: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerRegistration").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Customer data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
