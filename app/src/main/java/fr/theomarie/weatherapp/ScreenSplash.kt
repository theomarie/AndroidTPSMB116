package fr.theomarie.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fr.theomarie.weatherapp.utils.Functions

class ScreenSplash : AppCompatActivity() {

    val MY_PERMISSIONS_REQUEST = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_splash)

        setupPermissions()
    }
    private fun setupPermissions() {
        val permissionFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
            Functions.logi("Permission ACCESS_FINE_LOCATION denied")
            makeRequestPermissions()
            return
        }
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            Functions.logi("Permission ACCESS_COARSE_LOCATION denied")
            makeRequestPermissions()
            return
        }
        Functions.logi("All permission be granted")
        nextPage()
    }
    private fun makeRequestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSIONS_REQUEST)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode){
            MY_PERMISSIONS_REQUEST ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    setupPermissions()
                } else {
                    nextPage()
                }
        }
    }

    private fun nextPage() {
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }
}