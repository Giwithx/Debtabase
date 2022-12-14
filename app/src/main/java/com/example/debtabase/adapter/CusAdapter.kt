package com.example.debtabase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.debtabase.R
import com.example.debtabase.model.CustomerDebtModel
import com.example.debtabase.model.CustomerModel

class CusAdapter(private val cusList: ArrayList<CustomerDebtModel>) :
    RecyclerView.Adapter<CusAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cus_list_item, parent, false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCus = cusList[position]
        holder.tvCusName.text = currentCus.customerFN
        holder.tvCusDebt.text = currentCus.debtBalance.toString()
        holder.tvCusDDate.text = currentCus.debtDueDate
    }

    override fun getItemCount(): Int {
        return cusList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvCusName : TextView = itemView.findViewById(R.id.tvCusName)
        val tvCusDebt : TextView = itemView.findViewById(R.id.tvCusDebt)
        val tvCusDDate : TextView = itemView.findViewById(R.id.tvCusDDate)
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}