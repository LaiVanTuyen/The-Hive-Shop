package com.vn.thehiveshop.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseFragment
import com.vn.thehiveshop.databinding.FragmentCartBinding
import com.vn.thehiveshop.model.ItemInCart


class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart) {

    private val cartViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideCartViewModelFactory()
        )[CartViewModel::class.java]
    }

    private val cartAdapter by lazy {
        CartAdapter(onItemClick, onItemLongClick,updateAmount)
    }
    override fun initEvents() {
        TODO("Not yet implemented")
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    private val onItemClick: (ItemInCart) -> Unit = {

    }

    private val onItemLongClick: (ItemInCart) -> Boolean = {
        false
    }

    private val updateAmount: (ItemInCart, () -> Unit) -> Unit = { item, updateAmount ->


    }


}