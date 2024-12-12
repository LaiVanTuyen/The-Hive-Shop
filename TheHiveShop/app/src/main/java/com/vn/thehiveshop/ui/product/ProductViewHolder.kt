package com.vn.thehiveshop.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.thehiveshop.databinding.ItemProductBinding
import com.vn.thehiveshop.model.Product
import com.vn.thehiveshop.utils.ImageRequester

class ProductViewHolder(
    private val binding: ItemProductBinding,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var product: Product? = null

    init {
        binding.layoutItemProduct.setOnClickListener {
            product?.let {
                onItemClick(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onBind(product: Product) {
        this.product = product
        ImageRequester.setImageFromUrl(binding.productImage, product.url)
        binding.txtProductTitle.text = product.title
        binding.txtProductPrice.text = ("$${product.price}")
    }

    companion object{
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: (Product) -> Unit
        ): ProductViewHolder {
            val binding: ItemProductBinding = ItemProductBinding.inflate(inflater, parent, false)
            return ProductViewHolder(binding, onClick)
        }
    }

}