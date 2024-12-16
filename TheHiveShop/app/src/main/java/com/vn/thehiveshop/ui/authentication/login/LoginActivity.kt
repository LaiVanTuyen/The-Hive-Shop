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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.MainActivity
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseActivity
import com.vn.thehiveshop.data.User
import com.vn.thehiveshop.databinding.ActivityLoginBinding
import com.vn.thehiveshop.model.UserModel
import com.vn.thehiveshop.ui.authentication.AuthenticationViewModel
import com.vn.thehiveshop.ui.authentication.register.SignUpActivity
import com.vn.thehiveshop.utils.Resource

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

    /*private fun login() {
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
            authenticationViewModel.signIn(
                UserModel(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString(),
                    "",
                    true,
                    "",
                    "",
                    false
                )
            ).observeForever {
                it?.let { resources ->
                    when (resources) {
                        is Resource.Success ->{
                            User.setNewUser(resources.data.body().email, resources.data.body().password)
                        }
                    }
                }
            }
        }
    }*/

    private fun login() {

        // Biến kiểm tra trạng thái hợp lệ của email và mật khẩu
        val emailInput = binding.edtEmail.text.toString().trim()
        val passwordInput = binding.edtPassword.text.toString().trim()
        var isValid = true

        // Kiểm tra email
        when {
            emailInput.isEmpty() -> {
                binding.textInputEmail.error = resources.getString(R.string.empty_error)
                isValid = false
            }
            !isValidEmail(emailInput) -> {
                binding.textInputEmail.error = resources.getString(R.string.invalid_email)
                isValid = false
            }
            else -> binding.textInputEmail.error = null // Xóa lỗi nếu hợp lệ
        }

        // Kiểm tra mật khẩu
        if (passwordInput.isEmpty()) {
            binding.textInputPassword.error = resources.getString(R.string.empty_error)
            isValid = false
        } else {
            binding.textInputPassword.error = null // Xóa lỗi nếu hợp lệ
        }

        // Nếu cả email và mật khẩu hợp lệ thì thực hiện đăng nhập
        if (isValid) {
            val userModel = UserModel(
                email = emailInput,
                password = passwordInput,
                dateOfBirth = "",
                isMale = true,
                phoneNumber = "",
                address = "",
                isAdmin = false
            )

            // Gọi ViewModel để xử lý đăng nhập
            authenticationViewModel.signIn(userModel).observe(this) { resource ->
                when (resource) {
                    // Xử lý khi đăng nhập thành công
                    is Resource.Success -> {
                        resource.data?.let { response ->
                            if (response.isSuccessful) {
                                response.body()?.let { user ->
                                    User.setNewUser(user)

                                    // Lưu email vào SharedPreferences
                                    sharedPreferences.edit().apply {
                                        putString("EMAIL", user.email)
                                        apply()
                                    }

                                    // Đóng loading và chuyển đến màn hình chính
                                    dialogLoading.dismiss()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            } else {
                                // Xử lý khi response không thành công
                                dialogLoading.dismiss()
                                Toast.makeText(this, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // Hiển thị trạng thái đang xử lý
                    is Resource.Loading -> {
                        dialogLoading.show()
                    }

                    // Xử lý khi có lỗi xảy ra
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
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