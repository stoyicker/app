package app.home

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.R

/**
 * This Activity is used as a fallback when there is no browser installed that supports
 * Chrome Custom Tabs
 */
class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(
                ContextCompat.getColor(this, R.color.product_toolbar_background)))
        findViewById<WebView>(R.id.webview).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }

    companion object {
        const val EXTRA_URL = "extra.url"
    }
}