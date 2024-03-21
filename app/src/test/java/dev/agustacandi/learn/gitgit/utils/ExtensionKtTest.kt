package dev.agustacandi.learn.gitgit.utils

import org.junit.Test
import org.junit.Assert.assertEquals

class ExtensionKtTest {

    @Test
    fun testDecimalFormatFunctionExtension() {
        val decimalTestResult = 1000.toDecimalFormat()
        assertEquals("1,000", decimalTestResult)
    }
}