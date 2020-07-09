package com.sonnetindianetworks.tokenapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycleview_dashboard.view.*

class AdapterIssueToken(var IssuedTokens: List<DashTokenIssueModal>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_dashboard, parent, false) as View
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return IssuedTokens.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.recycleView_DashBoard_tokenNo.text = (IssuedTokens[position]).tokenNo
        holder.itemView.recycleView_DashBoard_issuedBy!!.text = (IssuedTokens[position]).issuedBy
        holder.itemView.recycleView_DashBoard_date.text = (IssuedTokens[position]).date

        // (holder as DeskViewHolder).bind(IssuedTokens[position])
    }
}