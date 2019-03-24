package com.guuguo.android.utils.extension

import com.guuguo.android.lib.extension.formatDecimal
import org.junit.Assert
import org.junit.Test

class NumberExtensionKtTest {

    @Test
    fun getFitSize() {
//        Assert.assertEquals(, )
    }

    @Test
    fun formatDecimal() {
        Assert.assertEquals("2.00",2.formatDecimal(2,false) )
        Assert.assertEquals("2",2.formatDecimal(2) )
        Assert.assertEquals("2.123",2.1232.formatDecimal(3))
    }
}