package com.littleapp.web.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.littleapp.web.Unit.DATA
import com.littleapp.web.Unit.THEME
import com.littleapp.web.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private val context: Context = this
    private var webName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webName = intent.getStringExtra(DATA.WEB_NAME)

        binding.webView.apply {
            webViewClient = WebViewClient()
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            settings.apply {
                loadsImagesAutomatically = true
                javaScriptEnabled = false
            }

            val urlToLoad = when (webName) {
                DATA.WEBSITE -> DATA.mySite
                DATA.INSTAGRAM -> DATA.myInstagram
                DATA.FACEBOOK -> DATA.myFacebook
                DATA.TWITTER -> DATA.myTwitter
                else -> DATA.mySite
            }
            loadUrl(urlToLoad)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}