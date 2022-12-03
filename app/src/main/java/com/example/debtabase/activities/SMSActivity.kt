package com.example.debtabase.activities

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.debtabase.R
import com.google.firebase.database.*


class SMSActivity : AppCompatActivity() {

    private lateinit var SMSNames : AutoCompleteTextView
    private lateinit var spinnerList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var Send : Button
    private lateinit var Cancel : Button
    private lateinit var SMSContainer : String
    private lateinit var message : ArrayList<String>

    private lateinit var dbRefReg: DatabaseReference
    private lateinit var dbRefDebt: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_m_s)
        val actionbar = supportActionBar
        actionbar!!.title = "SMS Notification"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        spinnerList = ArrayList()
        adapter = ArrayAdapter(this@SMSActivity, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        message = ArrayList()

        dbRefReg = FirebaseDatabase.getInstance().getReference("CustomerRegistration")
        dbRefDebt = FirebaseDatabase.getInstance().getReference("CustomerDebt")

        SMSNames = findViewById(R.id.SMSDropDown)
        Send = findViewById(R.id.btnSendSMS)
        Cancel = findViewById(R.id.btnCancelSend)

        SMSNames.setAdapter(adapter)
        ShowSMSData()

        checkPermissions()

        Send.setOnClickListener {
            if(::SMSContainer.isInitialized){
                if(message.isNullOrEmpty()){
                    Toast.makeText(this, "No Balance", Toast.LENGTH_SHORT).show()
                }
                else{
                    sendSMS()
                }
            }
            else{
                Toast.makeText(this@SMSActivity, "Please input data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendSMS(){
        val Info = SMSContainer
        val SMSNumber = Info.subSequence(1,11).toString()
        val Balance = message.last()
        val Person = SMSContainer.subSequence(12, SMSContainer.length).toString()
        val Message = "Good day $Person! Your debt balance is Php $Balance"
        try{
            val smsManager:SmsManager
            if (Build.VERSION.SDK_INT>=23){
                smsManager = this.getSystemService(SmsManager::class.java)
            }
            else{
                smsManager = SmsManager.getDefault()
            }
            smsManager.sendTextMessage("+63$SMSNumber", null,Message,null,null)

            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(this, "Please enter all the data..", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkPermissions(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 101)
        }
    }
    private fun ShowSMSData(){
        dbRefReg!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children) {
                    spinnerList.add(item.child("cusPN").value.toString()+" "+item.child("cusFN").value.toString()+" "+item.child("cusLN").value.toString())
                    SMSNames.setOnItemClickListener { adapterView, view, i, l ->
                        SMSContainer = adapterView.getItemAtPosition(i).toString()
                        val size =  SMSContainer.length
                        val DebtFN = SMSContainer.subSequence(12, size).toString()
                        dbRefDebt!!.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(item in snapshot.child(DebtFN).children){
                                    message.add(item.child("debtBalance").value.toString())
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
