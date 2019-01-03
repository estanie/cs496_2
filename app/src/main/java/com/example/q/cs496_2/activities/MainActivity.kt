package com.example.q.cs496_2.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R



class MainActivity : AppCompatActivity() {

    var permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            sendIntent()
        }else {
            for (permission: String in permission_list) {
                var chk = checkCallingOrSelfPermission(permission)
                if (chk == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permission_list, 0)
                    break
                }
            }
            if(checkPermission()) sendIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(checkPermission()){
            val intent = Intent(this,FragmentActivity::class.java)
            startActivity(intent)
        }else {
            Toast.makeText(this, "앱 이용을 위해 모든 관리권환을 활성화 해주세요", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun sendIntent(){
        val intent = Intent(this,FragmentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkPermission() : Boolean{
        var check = true
        for (permission in permission_list){
            var chk = checkCallingOrSelfPermission(permission)
            if (chk == PackageManager.PERMISSION_DENIED) check = false
        }
        return check
    }
}
