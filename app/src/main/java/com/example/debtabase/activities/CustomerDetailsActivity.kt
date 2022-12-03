package com.example.debtabase.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.debtabase.model.CustomerModel
import com.example.debtabase.R
import com.example.debtabase.model.CustomerDebtModel
import com.google.firebase.database.FirebaseDatabase

class CustomerDetailsActivity: AppCompatActivity() {
    private lateinit var tvCusName: TextView
    private lateinit var tvDebtBalance: TextView
    private lateinit var tvDDate: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("customerFN").toString(),
                intent.getStringExtra("debtDueDate").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("customerFN").toString(),
                intent.getStringExtra("debtDueDate").toString()
            )
        }
        val actionbar = supportActionBar
        actionbar!!.title = "Customer Information"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }
    private fun openUpdateDialog(
        customerFN: String,
        debtDueDate: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val txtDebtBalance = mDialogView.findViewById<EditText>(R.id.txtDebtBalance)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        txtDebtBalance.setText(intent.getStringExtra("debtBalance").toString())

        mDialog.setTitle("Updating $customerFN Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                customerFN,
                debtDueDate,
                txtDebtBalance.text.toString()
            )

            Toast.makeText(applicationContext, "Customer Data Updated", Toast.LENGTH_LONG).show()

            tvDebtBalance.text = txtDebtBalance.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        customer_name: String,
        due_date: String,
        debt_balance: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerDebt").child(customer_name).child(due_date)
        val cusInfo = CustomerDebtModel(customer_name, due_date, debt_balance.toFloat())
        dbRef.setValue(cusInfo)
    }

    private fun initView() {
        tvCusName = findViewById(R.id.tvCusName)
        tvDebtBalance = findViewById(R.id.tvDebtBalance)
        tvDDate = findViewById(R.id.tvDDate)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvCusName.text = intent.getStringExtra("customerFN")
        tvDebtBalance.text = intent.getStringExtra("debtBalance")
        tvDDate.text = intent.getStringExtra("debtDueDate")

    }

    private fun deleteRecord(customer_name: String, debtDueDate: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerDebt/$customer_name").child(debtDueDate)
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
