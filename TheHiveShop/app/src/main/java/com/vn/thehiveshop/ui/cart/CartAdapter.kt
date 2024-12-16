package com.vn.thehiveshop.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vn.thehiveshop.model.ItemInCart

class CartAdapter(
    private val onItemClick: (ItemInCart) -> Unit,
    private val onItemLongClick: (ItemInCart) -> Boolean,
    private val updateAmount: (ItemInCart, () -> Unit) -> Unit
) : RecyclerView.Adapter<CartViewHolder>() {

    private val cartItems = mutableListOf<ItemInCart>()

    fun updateCart(newCart: List<ItemInCart>) {
        cartItems.clear()
        cartItems.addAll(newCart)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CartViewHolder.create(inflater, parent, onItemClick, onItemLongClick, updateAmount)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.onBind(cartItems[position])
    }
}
