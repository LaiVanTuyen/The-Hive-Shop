package com.vn.thehiveshop.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.vn.thehiveshop.databinding.FragmentProductDetailBinding
import com.vn.thehiveshop.utils.ImageRequester

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding

    // Lấy đối tượng 'SafeArgs' từ ProductDetailFragmentArgs
    private val args: ProductDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        //initEvents()
    }

//    private fun initEvents() {
//        TODO("Not yet implemented")
//    }


    private fun initControls() {
        val product = args.product
        ImageRequester.setImageFromUrl(binding.imgProduct, product.url)
        binding.txtProductTitle.text = product.title
        binding.txtProductPrice.text = ("$${product.price}")
        binding.txtProductDescription.text = product.description

    }
}