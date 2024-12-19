package com.vn.thehiveshop.ui.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.vn.thehiveshop.R
import com.vn.thehiveshop.base.BaseActivity
import com.vn.thehiveshop.databinding.ActivitySplashScreenBinding
import com.vn.thehiveshop.ui.authentication.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_splash_screen

    override fun initControls(savedInstanceState: Bundle?) {
        // Khởi chạy các animation
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_animation)
        binding.appLogo.startAnimation(logoAnimation)
        binding.appText.startAnimation(textAnimation)

        // Chuyển sang màn hình LoginActivity sau 3 giây
        navigateToLoginAfterDelay()
    }

    override fun initEvents() {
        // Không có sự kiện trong màn hình splash
    }

    /**
     * Điều hướng đến LoginActivity sau khoảng thời gian 3 giây
     */
    private fun navigateToLoginAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Đóng SplashScreenActivity
        }, 3000) // Delay 3000ms
    }

}