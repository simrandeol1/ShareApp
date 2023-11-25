package com.example.shareapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareapp.R
import com.example.shareapp.model.Venues

// adapter for new list
class VenuesAdapter(private val context: Context, val venuesData: MutableList<Venues>): RecyclerView.Adapter<VenuesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.venues_item, parent, false))
    }

    override fun getItemCount(): Int {
        return venuesData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = venuesData[position].name
        holder.city.text = venuesData[position].city
        holder.address.text = venuesData[position].address
        holder.itemView.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(venuesData[position].url))
            context.startActivity(browserIntent)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val city = itemView.findViewById<TextView>(R.id.city)
        val address = itemView.findViewById<TextView>(R.id.address)
    }
}