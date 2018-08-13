package app

import org.junit.jupiter.api.Test

class AssertProduct {
    @Test
    fun appLabel() = assert(R.string.product_app_label != 0)

    @Test
    fun icLauncher() = assert(R.mipmap.ic_launcher != 0)

    @Test
    fun icLauncherRound() = assert(R.mipmap.ic_launcher_round != 0)

    @Test
    fun productHomeUrl() = assert(R.string.product_home_url != 0)

    @Test
    fun productToolbarColor() = assert(R.color.product_toolbar_background != 0)
}
