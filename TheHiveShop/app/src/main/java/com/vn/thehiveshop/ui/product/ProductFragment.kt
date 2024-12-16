package com.vn.thehiveshop.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseFragment
import com.vn.thehiveshop.databinding.FragmentProductBinding
import com.vn.thehiveshop.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductFragment : BaseFragment<FragmentProductBinding>(R.layout.fragment_product) {


    // Khởi tạo ViewModel với Factory để truyền ProductRepository
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideProductViewModelFactory()
        )[ProductViewModel::class.java]
    }

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(onProductItemClick)
    }

    private var job: Job? = null

    override fun initEvents() {
        binding.retryButton.setOnClickListener {
            hideErrorState()
            refreshData()
        }

        // Xử lý Pull-to-refresh nếu cần
        binding.sfProduct.setOnRefreshListener {
            refreshData()
        }

        binding.productToolBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_settingsFragment)
        }
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
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
            productViewModel.getAllProducts()
                .catch { e ->
                    binding.sfProduct.isRefreshing = false
                    // Xử lý trạng thái lỗi khi không có kết nối hoặc lỗi khác xảy ra
                    showErrorState("Lỗi: ${e.message}")
                    // Bạn có thể thêm kiểm tra cụ thể cho lỗi mạng ở đây:
                    if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
                        // Nếu lỗi là lỗi kết nối, bạn có thể hiển thị thông báo cụ thể
                        showErrorState("Không có kết nối internet. Vui lòng kiểm tra lại mạng.")
                    }
                    Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(
                            requireContext(),
                            "Data fetched successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    /*private fun refreshData() {
        productViewModel.getAllProduct().observe(viewLifecycleOwner) {
            binding.retryButton.isVisible = (it !is Resource.Loading && it !is Resource.Success)
            binding.emptyList.isVisible = (it !is Resource.Loading && it !is Resource.Success)
            binding.rvProduct.isVisible = it is Resource.Success

            when (it) {
                is Resource.Success -> {
                    // Use submitData to pass data to the adapter
                    val pagingData = PagingData.from(it.data)
                    productAdapter.submitData(lifecycle, pagingData)
                    binding.sfProduct.isRefreshing = false
                }

                is Resource.Error -> {
                    binding.sfProduct.isRefreshing = false
                    // You can handle showing the error message here if needed
                }

                is Resource.Loading -> {
                    binding.sfProduct.isRefreshing = true
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
    }

}