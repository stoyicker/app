package app.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import app.R
import app.home.PackageOps.getPackageNameToUse

internal class HomeActivity : AppCompatActivity() {
    private val productHomeUrl by lazy {
        Uri.parse(getString(R.string.product_home_url))
    }
    private val customTabsServiceConnection by lazy {
        HomeCustomTabsServiceConnection(contextThatBindsTheServiceConnection())
    }

    // TODO Handle back button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch()
        CustomTabsClient.bindCustomTabsService(
                contextThatBindsTheServiceConnection(), packageName, customTabsServiceConnection)
    }

    override fun onDestroy() {
        contextThatBindsTheServiceConnection().unbindService(customTabsServiceConnection)
        super.onDestroy()
    }

    private fun launch() {
        CustomTabsIntent.Builder().apply {
            setStartAnimations(
                    this@HomeActivity,
                    R.anim.grow_fade_in_from_bottom,
                    R.anim.shrink_fade_out_from_bottom)
            setExitAnimations(
                    this@HomeActivity,
                    R.anim.grow_fade_in_from_bottom,
                    R.anim.shrink_fade_out_from_bottom)
            setShowTitle(true)
            setInstantAppsEnabled(false)
            enableUrlBarHiding()
            setToolbarColor(ContextCompat.getColor(
                    this@HomeActivity, R.color.product_toolbar_background))
        }.build().apply {
            intent.`package` = getPackageNameToUse(this@HomeActivity, productHomeUrl)
            if (intent.`package` == null) {
                startActivity(
                        Intent(this@HomeActivity, WebViewActivity::class.java)
                                .putExtra(WebViewActivity.EXTRA_URL, productHomeUrl.toString()))
                startActivity(intent)
                supportFinishAfterTransition()
            } else {
                launchUrl(this@HomeActivity, productHomeUrl)
            }
        }
    }

    private fun contextThatBindsTheServiceConnection() = this
}
