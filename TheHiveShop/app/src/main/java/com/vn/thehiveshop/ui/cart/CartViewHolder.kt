package com.vn.thehiveshop.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vn.thehiveshop.R
import com.vn.thehiveshop.databinding.ItemInCartBinding
import com.vn.thehiveshop.model.ItemInCart
import com.vn.thehiveshop.utils.ImageRequester

class CartViewHolder(
    private val binding: ItemInCartBinding,
    private val onItemClick: (ItemInCart) -> Unit,
    private val onItemLongClick: (ItemInCart) -> Boolean,
    private val updateAmount: (ItemInCart, () -> Unit) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var itemInCart: ItemInCart? = null
    private var isReduce = false

    init {
        binding.btnIncrease.setOnClickListener { updateAmountForItem(isIncrease = true) }
        binding.btnReduce.setOnClickListener { updateAmountForItem(isIncrease = false) }

        binding.layoutItemInCart.setOnClickListener {
            itemInCart?.let(onItemClick)
        }

        binding.layoutItemInCart.setOnLongClickListener {
            itemInCart?.let { onItemLongClick(it) } ?: false
        }
    }

    private fun updateAmountForItem(isIncrease: Boolean) {
        itemInCart?.let {
            isReduce = !isIncrease
            it.amount = if (isIncrease) it.amount + 1 else maxOf(1, it.amount - 1)
            binding.txtProductAmount.text = it.amount.toString()
            updateAmount(it, onError)
        }
    }

    private val onError: () -> Unit = {
        itemInCart?.let {
            it.amount = if (isReduce) it.amount + 1 else it.amount - 1
            binding.txtProductAmount.text = it.amount.toString()
        }
    }

    fun onBind(itemInCart: ItemInCart) {
        this.itemInCart = itemInCart
        binding.txtProductTitle.text = itemInCart.title
        binding.txtProductAmount.text = itemInCart.amount.toString()
        binding.txtProductPrice.text = ("$${itemInCart.price}")
        ImageRequester.setImageFromUrl(binding.productImage, itemInCart.url)
    }

    companion object {
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onItemClick: (ItemInCart) -> Unit,
            onItemLongClick: (ItemInCart) -> Boolean,
            updateAmount: (ItemInCart, () -> Unit) -> Unit
        ): CartViewHolder {
            val binding: ItemInCartBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_in_cart, parent, false)
            return CartViewHolder(binding, onItemClick, onItemLongClick, updateAmount)
        }
    }
}
