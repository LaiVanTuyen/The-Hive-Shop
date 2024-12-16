package com.vn.thehiveshop.ui.authentication.register

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseActivity
import com.vn.thehiveshop.databinding.ActivitySignUpBinding
import com.vn.thehiveshop.model.UserModel
import com.vn.thehiveshop.ui.authentication.AuthenticationViewModel
import com.vn.thehiveshop.utils.Resource

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_sign_up
    }

    private val dialogLoading by lazy {
        Dialog(this)
    }

    private val authenticationViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideAuthenViewModelFactory(this)
        )[AuthenticationViewModel::class.java]
    }

    override fun initControls(savedInstanceState: Bundle?) {
        dialogLoading.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
    }

    override fun initEvents() {
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            register()
        }



        onTextChanged()
    }

    private fun register() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val rePassword = binding.edtRePassword.text.toString()
        var isValid = true
        // Kiểm tra email
        when {
            email.isEmpty() -> {
                binding.textInputEmail.error = resources.getString(R.string.empty_error)
                isValid = false
            }

            !isValidEmail(email) -> {
                binding.textInputEmail.error = resources.getString(R.string.invalid_email)
                isValid = false
            }

            else -> binding.textInputEmail.error = null // Xóa lỗi nếu hợp lệ
        }

        // Kiểm tra mật khẩu
        when {
            password.isEmpty() -> {
                binding.textInputPassword.error = resources.getString(R.string.empty_error)
                isValid = false
            }

            password.length < 8 || password.length > 20 -> {
                binding.textInputPassword.error =
                    resources.getString(R.string.invalid_pass_condition)
                isValid = false
            }

            else -> binding.textInputPassword.error = null // Xóa lỗi nếu hợp lệ
        }

        // Kiểm tra mật khẩu nhập lại
        when {
            rePassword.isEmpty() -> {
                binding.textInputRetypePass.error = resources.getString(R.string.empty_error)
                isValid = false
            }

            rePassword != password -> {
                binding.textInputRetypePass.error = resources.getString(R.string.not_match_pass)
                isValid = false
            }

            else -> binding.textInputRetypePass.error = null // Xóa lỗi nếu hợp lệ
        }

        if (isValid) {
            val userModel = UserModel(
                email,
                password,
                "",
                true,
                "",
                "",
                false
            );

            authenticationViewModel.signUp(userModel).observe(this) {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { response ->
                            // Xử lý khi đăng ký thành công
                            if (response.isSuccessful) {
                                // Hiển thị thông báo đăng ký thành công
                                dialogLoading.dismiss()
                                finish()
                            } else {
                                // Xử lý khi response không thành công
                                dialogLoading.dismiss()
                                Toast.makeText(
                                    this,
                                    "Login failed: ${response.message()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    is Resource.Loading -> {
                        dialogLoading.show()
                    }

                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
                    binding.textInputPassword.boxStrokeColor =
                        checkStrengthPass(binding.edtPassword.text.toString())
                } else
                    binding.textInputPassword.boxStrokeColor =
                        resources.getColor(R.color.colorPrimaryDark)
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
                } else if (binding.edtRePassword.text.toString() != binding.edtPassword.text.toString()) {
                    binding.textInputRetypePass.error = "Mật khẩu không trùng khớp"
                } else {
                    binding.textInputRetypePass.error = null
                }
                hideKeyboard(binding.edtRePassword)
            }
        }

    }

    private fun checkStrengthPass(pass: String): Int {
        var chars = pass.toCharArray() // Chuyển chuỗi thành mảng ký tự
        var containDigit: Boolean = false // Biến kiểm tra có chứa số không
        var containUpperCase: Boolean = false // Biến kiểm tra có chứa chữ hoa không
        var containLowerCase: Boolean = false // Biến kiểm tra có chứa chữ thường không
        for (i in chars.indices) {
            if (chars[i].isDigit()) containDigit = true
            if (chars[i].isUpperCase()) containUpperCase = true
            if (chars[i].isLowerCase()) containLowerCase = true

        }

        return when {
            pass.length in 8..20 && !containDigit && containUpperCase && containLowerCase -> resources.getColor(
                R.color.medium_pass_color
            ) // Mật khẩu trung bình
            pass.length in 8..20 && containDigit && containUpperCase && containLowerCase -> resources.getColor(
                R.color.strong_pass_color
            )  // Mật khẩu mạnh
            else -> resources.getColor(R.color.weak_pass_color)  // Mật khẩu yếu
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