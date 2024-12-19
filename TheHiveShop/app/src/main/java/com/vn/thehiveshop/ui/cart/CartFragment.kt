package com.vn.thehiveshop.ui.cart

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseFragment
import com.vn.thehiveshop.data.request.CartRequest
import com.vn.thehiveshop.databinding.FragmentCartBinding
import com.vn.thehiveshop.model.ItemInCart
import com.vn.thehiveshop.utils.Resource


class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart) {

    private val cartViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideCartViewModelFactory()
        )[CartViewModel::class.java]
    }

    private val cartAdapter by lazy {
        CartAdapter(onItemClick, onItemLongClick, updateAmount)
    }

    private val dialogLoading by lazy {
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
    }

    private var cart: List<ItemInCart> = listOf()
        set(value) {
            field = value
            getTotalAmount()
        }

    private val controller by lazy {
        findNavController()
    }

    override fun initEvents() {
        binding.sfCart.setOnRefreshListener { refreshData() }
        binding.btnBuy.setOnClickListener { buy() }
        binding.toolbarCart.setNavigationOnClickListener { controller.popBackStack() }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            dialogLoading.show()
            dialogLoading.window?.decorView?.postDelayed({ dialogLoading.dismiss() }, 1000)
        } else {
            dialogLoading.dismiss()
        }
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {

        refreshData()
        setupRecyclerView()


    }

    // Hàm lấy dữ liệu giỏ hàng từ ViewModel và cập nhật UI
    private fun refreshData() {
        cartViewModel.getCart().observe(viewLifecycleOwner) { resource ->
            binding.sfCart.isRefreshing =
                resource is Resource.Loading // Hiển thị thanh refresh khi đang tải dữ liệu
            when (resource) {
                is Resource.Success -> {
                    showToast(R.string.success)
                    cart = resource.data // Lưu dữ liệu giỏ hàng
                    cartAdapter.cart = resource.data // Cập nhật lại adapter
                }

                is Resource.Error -> showToast(resource.message)
                else -> Unit
            }
        }
    }

    // Hàm mua hàng, xóa từng sản phẩm trong giỏ
    private fun buy() {
        var successCount = 0
        showLoading(true) // Hiển thị dialog loading
        cart.forEach { item ->
            cartViewModel.deleteItem(CartRequest(item = item))
                .observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            successCount++
                            if (successCount == cart.size) {
                                showLoading(false) // Tắt dialog loading khi tất cả sản phẩm đã được xóa
                                refreshData() // Làm mới dữ liệu giỏ hàng
                            }
                        }

                        is Resource.Error -> {
                            showLoading(false)
                            showToast(resource.message)
                        }

                        else -> Unit
                    }
                }
        }
    }


    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
    private val updateAmount: (ItemInCart, () -> Unit) -> Unit = { item, onError ->
        cartViewModel.updateItemAmount(CartRequest(item = item))
            .observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        showLoading(false)
                        getTotalAmount() // Cập nhật lại tổng tiền sau khi thay đổi số lượng
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(resource.message)
                        onError() // Xử lý khi có lỗi
                    }

                    is Resource.Loading -> showLoading(true) // Hiển thị loading khi đang xử lý
                }
            }
    }

    // Hàm tính tổng số tiền trong giỏ hàng
    private fun getTotalAmount() {
        var totalAmount = 0f
        cart.forEach {
            totalAmount += (it.price * it.amount)
        } // Tính tổng tiền
        binding.txtAmount.text = getString(R.string.total_amount, totalAmount) // Cập nhật UI
    }

    // Hàm xử lý sự kiện khi người dùng click vào một sản phẩm (hiện tại không làm gì)
    private val onItemClick: (ItemInCart) -> Unit = {}

    // Hàm xử lý sự kiện khi người dùng nhấn lâu vào một sản phẩm (hiển thị dialog xác nhận xóa)
    private val onItemLongClick: (ItemInCart) -> Boolean = { item ->
        showConfirmDialog(item) // Hiển thị dialog xác nhận
        false
    }

    // Hàm hiển thị dialog xác nhận xóa sản phẩm
    private fun showConfirmDialog(item: ItemInCart) {
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_confirm)
            findViewById<MaterialTextView>(R.id.txt_dialog_title).text =
                getString(R.string.delete) // Tiêu đề dialog
            findViewById<MaterialTextView>(R.id.txt_dialog_message).text =
                getString(R.string.delete_message) // Nội dung dialog
            findViewById<MaterialButton>(R.id.btn_confirm).setOnClickListener {
                dismiss()
                deleteCartItem(item) // Gọi hàm xóa sản phẩm
            }
            findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener { dismiss() } // Hủy bỏ
        }.show()
    }

    // Hàm xóa một sản phẩm khỏi giỏ hàng
    private fun deleteCartItem(item: ItemInCart) {
        cartViewModel.deleteItem(CartRequest(item = item)).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    showLoading(false)
                    refreshData() // Làm mới dữ liệu giỏ hàng sau khi xóa
                    getTotalAmount() // Cập nhật lại tổng tiền
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> showLoading(true)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    /**
     * Hiển thị một Toast với thông báo từ resource ID hoặc chuỗi message.
     * @param message Thông báo dạng chuỗi hoặc resource ID.
     */
    private fun showToast(message: Any) {
        val context = requireContext()
        val text = if (message is Int) context.getString(message) else message.toString()
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}

