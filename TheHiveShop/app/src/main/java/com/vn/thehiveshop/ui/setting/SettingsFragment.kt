package com.vn.thehiveshop.ui.setting

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.vn.thehiveshop.Injection
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseFragment
import com.vn.thehiveshop.data.User
import com.vn.thehiveshop.databinding.FragmentSettingsBinding
import com.vn.thehiveshop.model.UserModel
import com.vn.thehiveshop.ui.authentication.login.LoginActivity
import com.vn.thehiveshop.utils.Resource
import retrofit2.Response
import java.util.Calendar

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings),
    View.OnClickListener {

    private val controller by lazy {
        findNavController()
    }

    private val settingViewModel by lazy {
        ViewModelProvider(
            this,
            Injection.provideSettingViewModelFactory(requireContext())
        )[SettingViewModel::class.java]
    }

    private val dialogLoading by lazy {
        Dialog(requireContext())
    }

    override fun initEvents() {
        binding.itemDob.setOnClickListener(this)
        binding.itemGender.setOnClickListener(this)
        binding.itemPhoneNumber.setOnClickListener(this)
        binding.itemAddress.setOnClickListener(this)
        binding.itemChangePass.setOnClickListener(this)
        binding.itemLogout.setOnClickListener(this)
        binding.toolbarSetting.setNavigationOnClickListener {
            controller.popBackStack()
        }
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        dialogLoading.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }

        binding.txtEmail.text = User.email
        binding.txtDob.text = User.dateOfBirth
        binding.txtGender.text = if (User.isMale) "Male" else "Female"
        binding.txtPhoneNumber.text = User.phoneNumber
        binding.txtAddress.text = User.address
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.item_dob -> showDatePickerDialog()
            R.id.item_gender -> showGenderDialog()
            R.id.item_phone_number -> showTextInputDialog(R.string.phone_number, binding.txtPhoneNumber.text.toString(), InputType.TYPE_CLASS_PHONE) { edtDialog ->
                User.phoneNumber = edtDialog.text.toString()
                updateUserInfo( User.phoneNumber, binding.txtPhoneNumber)
            }
            R.id.item_address -> {
                showTextInputDialog(R.string.address, binding.txtAddress.text.toString(), InputType.TYPE_CLASS_TEXT) { edtDialog ->
                    User.address = edtDialog.text.toString()
                    updateUserInfo(User.address, binding.txtAddress)
                }
            }
            R.id.item_change_pass -> showChangePasswordDialog()
            R.id.item_logout -> {
                showLogoutDialog()
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.findViewById<MaterialTextView>(R.id.txt_dialog_title).text = getString(R.string.logout)
        dialog.findViewById<MaterialTextView>(R.id.txt_dialog_message).text = getString(R.string.logout_message)

        dialog.findViewById<MaterialButton>(R.id.btn_confirm).setOnClickListener {
            User.logOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }

        dialog.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showGenderDialog() {
        AlertDialog.Builder(requireContext(), R.style.DialogTheme).apply {
            setTitle(resources.getString(R.string.gender))
            setItems(R.array.gender) { dialog, which ->
                val gender = if (which == 0) {
                    User.isMale = true
                    "Male"
                } else {
                    User.isMale = false
                    "Female"
                }
                updateUserInfo(gender, binding.txtGender)
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        // Kiểm tra nếu có dữ liệu ngày sinh đã chọn trước đó
        if (binding.txtDob.text.isNotEmpty()) {
            val dobParts = binding.txtDob.text.split("-")
            if (dobParts.size == 3) {
                val day = dobParts[0].toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
                val month = (dobParts[1].toIntOrNull() ?: (calendar.get(Calendar.MONTH) + 1)) - 1
                val year = dobParts[2].toIntOrNull() ?: calendar.get(Calendar.YEAR)

                calendar.set(year, month, day)
            }
        }

        val (year, month, day) = Triple(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        DatePickerDialog(
            requireContext(), R.style.DialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                User.dateOfBirth = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                updateUserInfo(User.dateOfBirth, binding.txtDob)
            },
            year, month, day
        ).show()
    }

    private fun showTextInputDialog(dialogTitleRes: Int, currentText: String, inputType: Int, onConfirm: (TextInputEditText) -> Unit) {
        showDialog(R.layout.dialog_add_text, getString(dialogTitleRes), currentText, inputType, onConfirm)
    }

    private fun showChangePasswordDialog() {
        showDialog(R.layout.dialog_change_pass, getString(R.string.change_password), "", InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) { edtDialog ->
            User.password = edtDialog.text.toString()
            updateUserInfo(User.password, MaterialTextView(requireContext()))
        }
    }

    private fun showDialog(
        dialogId: Int,
        dialogTitle: String,
        content1: String,
        inputType1: Int,
        onConfirm: (edtDialog: TextInputEditText) -> Unit
    ) {
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(dialogId)
        }

        val textInputDialog1 = dialog.findViewById<TextInputLayout>(R.id.text_input_dialog_1)
        val edtDialog1 = dialog.findViewById<TextInputEditText>(R.id.edt_dialog_1)
        val title = dialog.findViewById<MaterialTextView>(R.id.txt_dialog_title)
        val confirm = dialog.findViewById<MaterialButton>(R.id.btn_confirm)
        val cancel = dialog.findViewById<MaterialButton>(R.id.btn_cancel)
        if (dialogId == R.layout.dialog_change_pass) {
            val textInputDialog2 = dialog.findViewById<TextInputLayout>(R.id.text_input_dialog_2)
            val edtDialog2 = dialog.findViewById<TextInputEditText>(R.id.edt_dialog_2)
            val textInputDialog3 = dialog.findViewById<TextInputLayout>(R.id.text_input_dialog_3)
            val edtDialog3 = dialog.findViewById<TextInputEditText>(R.id.edt_dialog_3)
            title.text = dialogTitle
            textInputDialog1.hint = resources.getString(R.string.password)
            textInputDialog2.hint = resources.getString(R.string.new_password)
            textInputDialog3.hint = resources.getString(R.string.retype_pasword)

            edtDialog1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edtDialog1.text.toString().isNotEmpty()) {
                        textInputDialog1.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            edtDialog2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edtDialog2.text.toString().isNotEmpty()) {
                        textInputDialog2.error = null
                        // Kiểm tra độ mạnh của mật khẩu
                        textInputDialog2.boxStrokeColor = checkStrengthPass(edtDialog2.text.toString())
                    } else {
                        textInputDialog2.boxStrokeColor = resources.getColor(R.color.colorPrimaryDark)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            edtDialog3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edtDialog3.text.toString().isNotEmpty()) {
                        textInputDialog3.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            confirm.setOnClickListener{
                var validPass = true

                if (edtDialog1.text.isNullOrEmpty()) {
                    textInputDialog1.error = resources.getString(R.string.empty_error)
                    validPass = false
                } else if (isValidEmail(edtDialog1.text.toString())) {
                    textInputDialog1.error = resources.getString(R.string.invalid_email)
                    validPass = false
                } else {
                    textInputDialog1.error = null
                }

                if (edtDialog2.text.isNullOrEmpty()) {
                    textInputDialog2.error = resources.getString(R.string.empty_error)
                    validPass = false
                } else if (edtDialog2.text.toString().length < 8 || edtDialog2.text.toString().length > 20) {
                    textInputDialog2.error = resources.getString(R.string.invalid_pass_condition)
                    validPass = false
                } else {
                    textInputDialog2.error = null
                }

                if (edtDialog3.text.isNullOrEmpty()) {
                    textInputDialog3.error = resources.getString(R.string.empty_error)
                    validPass = false
                } else if (edtDialog3.text.toString() != edtDialog2.text.toString()) {
                    textInputDialog3.error = resources.getString(R.string.not_match_pass)
                    validPass = false
                } else {
                    textInputDialog3.error = null
                }

                if (validPass) {
                    if (User.password == edtDialog1.text.toString()) {
                        onConfirm(edtDialog2)
                        dialog.dismiss()
                    } else {
                        showToast(R.string.old_pass_wrong)
                    }
                }
            }
        } else {
            title.text = dialogTitle
            textInputDialog1.hint = dialogTitle
            edtDialog1.setText(content1)
            edtDialog1.inputType = inputType1
            confirm.setOnClickListener{
                onConfirm(edtDialog1)
                dialog.dismiss()
            }
        }

        cancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()

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
            pass.length in 8..20 && !containDigit && containUpperCase && containLowerCase -> resources.getColor(R.color.medium_pass_color) // Mật khẩu trung bình
            pass.length in 8..20 && containDigit && containUpperCase && containLowerCase -> resources.getColor(R.color.strong_pass_color)  // Mật khẩu mạnh
            else -> resources.getColor(R.color.weak_pass_color)  // Mật khẩu yếu
        }

    }


    private fun updateUserInfo(data: String, textView: MaterialTextView) {
        settingViewModel.updateUser(User.getUserInfo()).observeForever {
            observeData(it, textView, data)
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) false
        else Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun observeData(
        resource: Resource<Response<UserModel>>?,
        textView: MaterialTextView,
        data: Any
    ) {
        resource ?: return // Trả về nếu resource là null để tránh xử lý không cần thiết

        when (resource) {
            is Resource.Success -> {
                dialogLoading.dismiss()
                textView.text = data.toString()
                showToast(R.string.done)
            }

            is Resource.Loading -> {
                dialogLoading.show()
            }

            is Resource.Error -> {
                dialogLoading.dismiss()
                showToast(resource.message)
            }
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