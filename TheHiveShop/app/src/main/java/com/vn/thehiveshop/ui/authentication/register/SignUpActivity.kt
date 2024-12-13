package com.vn.thehiveshop.ui.authentication.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseActivity
import com.vn.thehiveshop.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_sign_up
    }

    override fun initControls(savedInstanceState: Bundle?) {

    }

    override fun initEvents() {
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }



        onTextChanged()
    }

    private fun onTextChanged() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý trước khi văn bản thay đổi
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nếu người dùng nhập văn bản không rỗng
                if (!s.isNullOrEmpty()) {
                    binding.textInputEmail.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Không cần xử lý thêm sau khi thay đổi văn bản
            }
        })


        binding.edtEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // Khi người dùng rời khỏi EditText
                if (binding.edtEmail.text.isNullOrEmpty()) {
                    binding.textInputEmail.error = "Email không được để trống"
                } else if (!isValidEmail(binding.edtEmail.text)) {
                    binding.textInputEmail.error = "Email không hợp lệ"
                } else {
                    binding.textInputEmail.error = null
                }
                hideKeyboard(binding.edtEmail)
            }
        }

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý trước khi văn bản thay đổi
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.textInputPassword.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Không cần xử lý thêm sau khi thay đổi văn bản
            }
        })

        binding.edtPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // Khi người dùng rời khỏi EditText
                if (binding.edtPassword.text.isNullOrEmpty()) {
                    binding.textInputPassword.error = "Mật khẩu không được để trống"
                } else {
                    binding.textInputPassword.error = null
                }
                hideKeyboard(binding.edtPassword)
            }
        }

        binding.edtRePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý trước khi văn bản thay đổi
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.textInputRetypePass.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Không cần xử lý thêm sau khi thay đổi văn bản
            }
        })

        binding.edtRePassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // Khi người dùng rời khỏi EditText
                if (binding.edtRePassword.text.isNullOrEmpty()) {
                    binding.textInputRetypePass.error = "Mật khẩu không được để trống"
                } else {
                    binding.textInputRetypePass.error = null
                }
                hideKeyboard(binding.edtRePassword)
            }
        }

    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) false
        else Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    // Hàm ẩn bàn phím
    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }


}