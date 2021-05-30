package fr.theomarie.weatherapp

import android.util.Log
import fr.theomarie.weatherapp.utils.Functions.capitalizeWords
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        print("\u00e2".toLowerCase())
        print("une \u00e2est de mot".capitalizeWords())
        assertEquals(4, 2 + 2)
    }
}