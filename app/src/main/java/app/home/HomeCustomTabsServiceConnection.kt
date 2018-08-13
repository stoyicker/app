package app.home

import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection

internal class HomeCustomTabsServiceConnection(private val activity: AppCompatActivity)
    : CustomTabsServiceConnection() {

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        client.warmup(0)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        if (!activity.isFinishing) {
            activity.supportFinishAfterTransition()
        }
    }
}
