package com.vn.thehiveshop.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseFragment
import com.vn.thehiveshop.data.User
import com.vn.thehiveshop.data.request.CartRequest
import com.vn.thehiveshop.databinding.FragmentProductDetailBinding
import com.vn.thehiveshop.model.ItemInCart
import com.vn.thehiveshop.model.Product
import com.vn.thehiveshop.ui.cart.CartViewModel
import com.vn.thehiveshop.utils.ImageRequester
import com.vn.thehiveshop.utils.Resource

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(R.layout.fragment_product_detail) {


    // Lấy đối tượng 'SafeArgs' từ ProductDetailFragmentArgs
    private val args: ProductDetailFragmentArgs by navArgs()

    private val controller by lazy { findNavController() }

    private var productAmount: Int = 1
        set(value) {
            field = value
            binding.txtProductAmount.text = value.toString()
        }

    private val cartViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideCartViewModelFactory()
        )[CartViewModel::class.java]
    }
    private var product: Product? = null

    private val dialogLoading by lazy {
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
    }


    override fun initEvents() {
        binding.btnIncrease.setOnClickListener { productAmount++ }
        binding.btnReduce.setOnClickListener {
            if (productAmount > 1) productAmount--
        }

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(requireContext(), R.string.add_to_cart, Toast.LENGTH_SHORT).show()
            product?.let {
                val itemInCart = ItemInCart(it.id, it.title, it.url, it.price, it.description, productAmount)
                val request = CartRequest(User.email, itemInCart)
                cartViewModel.addProductToCart(request).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            dialogLoading.dismiss()
                            Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Error -> {
                            dialogLoading.dismiss()
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            dialogLoading.show()
                        }
                    }
                }
                controller.popBackStack()
            }
        }

        binding.toolbarDetail.setNavigationOnClickListener { controller.popBackStack() }
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        val product = args.product
        this.product = product
        setData(product)
    }


    @SuppressLint("SetTextI18n")
    private fun setData(product: Product) {
        with(binding) {
            ImageRequester.setImageFromUrl(imgProduct, product.url)
            txtProductTitle.text = product.title
            txtProductPrice.text = "$${product.price}"
            txtProductDescription.text = product.description
            txtProductAmount.text = productAmount.toString()
        }
    }
}