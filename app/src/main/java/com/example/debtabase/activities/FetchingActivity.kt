package com.example.debtabase.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.debtabase.adapter.CusAdapter
import com.example.debtabase.model.CustomerDebtModel
import com.example.debtabase.model.CustomerModel
import com.example.debtabase.R
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var cusRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var cusList: ArrayList<CustomerDebtModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        cusRecyclerView = findViewById(R.id.rvCus)
        cusRecyclerView.layoutManager = LinearLayoutManager(this)
        cusRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        cusList = arrayListOf<CustomerDebtModel>()

        getCustomerData()
        val actionbar = supportActionBar
        actionbar!!.title = "Customers"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }


    private fun getCustomerData() {
        cusRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("CustomerDebt")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cusList.clear()
                if (snapshot.exists()){
                    for (cusSnap in snapshot.children){
                        for (item in cusSnap.children){
                            val cusData = item.getValue(CustomerDebtModel::class.java)
                            cusList.add(cusData!!)
                        }
                    }
                    val mAdapter = CusAdapter(cusList)
                    cusRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CusAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingActivity, CustomerDetailsActivity::class.java)
                            intent.putExtra("customerFN", cusList[position].customerFN)
                            intent.putExtra("debtBalance", cusList[position].debtBalance.toString())
                            intent.putExtra("debtDueDate", cusList[position].debtDueDate)
                            startActivity(intent)
                        }
                    })
                    cusRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
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