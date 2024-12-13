package com.vn.thehiveshop.ui.authentication.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseActivity
import com.vn.thehiveshop.databinding.ActivityLoginBinding
import com.vn.thehiveshop.ui.authentication.AuthenticationViewModel
import com.vn.thehiveshop.ui.authentication.register.SignUpActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("USER_EMAIL", Context.MODE_PRIVATE)
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
        binding.edtEmail.setText(sharedPreferences.getString("EMAIL", ""))

        dialogLoading.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
    }




    override fun initEvents() {
        binding.btnRegister.setOnClickListener {
            val intent: Intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            login()
        }

        onTextChanged()
    }

    private fun login() {
        var isValidEmail = true
        var isValidPassword = true
        if (binding.edtEmail.text.isNullOrEmpty()) {
            binding.textInputEmail.error = "Email không được để trống"
            isValidEmail = false
        } else if (!isValidEmail(binding.edtEmail.text)) {
            binding.textInputEmail.error = "Email không hợp lệ"
            isValidEmail = false
        } else {
            binding.textInputEmail.error = null
        }

        if (binding.edtPassword.text.isNullOrEmpty()) {
            binding.textInputPassword.error = "Mật khẩu không được để trống"
            isValidPassword = false
        } else {
            binding.textInputPassword.error = null
        }

        if (isValidEmail && isValidPassword) {
            // Xử lý đăng nhập
//            authenticationViewModel.s
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