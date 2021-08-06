package com.example.aida.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aida.R
import com.example.aida.model.DrugModel

class DrugAdapter(
    private val context: Context,
    private var list: ArrayList<DrugModel>
    ): RecyclerView.Adapter<DrugAdapter.MyViewHolder>() {
    var data = list
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.aida_drug_items,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = data[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val drugName: TextView = view.findViewById(R.id.tv_drug_name)
        val drugPrice: TextView = view.findViewById(R.id.tv_drug_price)
        val drugImage: ImageView = view.findViewById(R.id.iv_list_image)
        val drugDate: TextView = view.findViewById(R.id.tv_drug_expiration)

        fun bind(model: DrugModel){
            drugName.text = model.drugName
            drugPrice.text = model.drugPrice
            drugDate.text = model.drugDate
            drugImage.setImageURI(Uri.parse(model.drugImage))

        }
    }


}