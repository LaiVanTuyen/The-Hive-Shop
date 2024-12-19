package com.vn.thehiveshop.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

// Lớp BaseFragment sử dụng Generics <B : ViewDataBinding> để hỗ trợ các lớp con dễ dàng binding layout
abstract class BaseFragment<B : ViewDataBinding>(
    private val layoutId: Int // Tham số layoutId được truyền trực tiếp khi khởi tạo
) : Fragment() {

    // Biến _binding được dùng để lưu trữ ViewDataBinding, nullable để tránh rò rỉ bộ nhớ
    private var _binding: B? = null
    protected val binding get() = _binding!! // Truy cập binding, đảm bảo không null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sử dụng DataBindingUtil để tạo instance của binding
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // Đặt lifecycle owner cho binding
        return binding.root // Trả về view root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls(view, savedInstanceState) // Khởi tạo các control
        initEvents() // Khởi tạo các sự kiện
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Giải phóng binding khi view bị hủy để tránh rò rỉ bộ nhớ
    }

    // Phương thức abstract để lớp con triển khai xử lý sự kiện
    abstract fun initEvents()

    // Phương thức abstract để lớp con triển khai xử lý khởi tạo control
    abstract fun initControls(view: View, savedInstanceState: Bundle?)
}
