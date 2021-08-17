package com.example.aida.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aida.R
import com.example.aida.activity.AddDrugInfo
import com.example.aida.activity.MainActivity
import com.example.aida.database.DatabaseHandler
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

    private var onClickListener: OnClickListener? = null

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
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestcode: Int){
        val intent = Intent(context, AddDrugInfo::class.java)
        intent.putExtra(MainActivity.EXTRA_DRUG_DETAILS, data[position])
        activity.startActivityForResult(
            intent,
            requestcode
        )
        notifyItemChanged(position)
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteAidaDrug(data[position])
        if (isDeleted > 0) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener {
        fun onClick(position: Int, model: DrugModel)
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