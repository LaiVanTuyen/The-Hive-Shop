package com.vn.thehiveshop.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.vn.thehiveshop.databinding.FragmentProductBinding
import com.vn.thehiveshop.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(
            this,
            ProductViewModel.ProductViewModelFactory()
        )[ProductViewModel::class.java]
    }

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(onProductItemClick)
    }

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        binding.retryButton.setOnClickListener {
            hideErrorState()
            refreshData()
        }

        // Xử lý Pull-to-refresh nếu cần
        binding.sfProduct.setOnRefreshListener {
            refreshData()
        }
    }


    private fun initControls() {
        binding.rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateViewAdapter { productAdapter.retry() },
                footer = LoadStateViewAdapter { productAdapter.retry() }
            )
        }

        refreshData()
    }

    private fun refreshData() {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.getAllProduct()
                .catch { e ->
                    binding.sfProduct.isRefreshing = false
                    showErrorState("Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                .collectLatest { pagingData ->
                    binding.sfProduct.isRefreshing = false
                    productAdapter.submitData(pagingData)
                    // Kiểm tra xem adapter có dữ liệu không sau khi submit
                    if (productAdapter.itemCount == 0) {
                        showErrorState("No results found")
                        Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        hideErrorState()
                        Toast.makeText(requireContext(), "Get data success", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }


    /*private fun getProductFromApi() {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.getAllProduct()
                .catch { e ->
                    binding.sfProduct.isRefreshing = false
                    showErrorState("Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                .collectLatest { pagingData ->
                    binding.sfProduct.isRefreshing = false
                    productAdapter.submitData(pagingData)
                    // Kiểm tra xem adapter có dữ liệu không sau khi submit
                    if (productAdapter.itemCount == 0) {
                        showErrorState("No results found")
                        Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        hideErrorState()
                        Toast.makeText(requireContext(), "Get data success", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }*/


    private fun showErrorState(message: String) {
        binding.rvProduct.visibility = View.GONE
        binding.emptyList.visibility = View.VISIBLE
        binding.retryButton.visibility = View.VISIBLE
        binding.emptyList.text = message
    }

    private fun hideErrorState() {
        binding.rvProduct.visibility = View.VISIBLE
        binding.emptyList.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
    }


    private val onProductItemClick: (Product) -> Unit = { product ->
        // Example: Show product detail
        Toast.makeText(requireContext(), "Clicked on: ${product.title}", Toast.LENGTH_SHORT).show()
        val action = ProductFragmentDirections.actionProductFragmentToProductDetailFragment(product)
        findNavController().navigate(action)
/*        val bundle = bundleOf("PRODUCT_DETAIL" to product)
        findNavController().navigate(
            com.vn.thehiveshop.R.id.action_productFragment_to_productDetailFragment,
            bundle
        )*/
    }

}