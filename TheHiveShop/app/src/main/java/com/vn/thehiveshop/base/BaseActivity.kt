package com.vn.thehiveshop.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding ?: throw IllegalStateException("ViewDataBinding is not initialized.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, getLayoutId())
        initControls(savedInstanceState)
        initEvents()
    }

    abstract fun getLayoutId(): Int

    /**
     * Khởi tạo giao diện và các thành phần liên quan.
     */
    protected abstract fun initControls(savedInstanceState: Bundle?)

    /**
     * Thiết lập các sự kiện (click listener, observers, etc.)
     */
    protected abstract fun initEvents()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Tránh rò rỉ bộ nhớ
    }

}
