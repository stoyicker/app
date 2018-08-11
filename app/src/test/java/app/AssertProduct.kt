package app

import org.junit.jupiter.api.Test

class AssertProduct {
    @Test
    fun appLabel() = assert(R.string.product_app_label != 0)

    @Test
    fun icLauncher() = assert(R.mipmap.ic_launcher != 0)
}
