package com.gopal.swipeassignment.product.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import com.gopal.servicelayer.product.model.response_model.ProductResponseModelItem
import com.gopal.swipeassignment.databinding.ProductCardBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val differCallback = object
        : DiffUtil.ItemCallback<ProductResponseModelItem>() {
        override fun areItemsTheSame(
            oldItem: ProductResponseModelItem,
            newItem: ProductResponseModelItem
        ): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(
            oldItem: ProductResponseModelItem,
            newItem: ProductResponseModelItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(product.image).into(holder.binding.productImage)
            holder.binding.productName.text = product.productName
            holder.binding.productType.text = product.productType
            holder.binding.productPrice.text = String.format("%s %s", "Rs. ", product.price)
            holder.binding.productTax.text = String.format("%s %s", "Tax: ", product.tax)
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ProductViewHolder(itemView: ProductCardBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
    }

}