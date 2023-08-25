package com.amitdroid.pkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.amitdroid.pkart.activity.ProductDetailActivity
import com.amitdroid.pkart.databinding.LayoutProductItemBinding
import com.amitdroid.pkart.model.AddProductModel
import com.bumptech.glide.Glide

class ProductAdapter(val context: Context, val list: ArrayList<AddProductModel>)
    :RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(val binding:LayoutProductItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =LayoutProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data =list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView4)
        holder.binding.textView6.text=data.productName
        holder.binding.textView7.text=data.productCategory
        holder.binding.textView4.text=data.productMrp

        holder.binding.button7.text=data.productSp


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }
}