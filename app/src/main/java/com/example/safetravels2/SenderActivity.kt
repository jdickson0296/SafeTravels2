package com.example.safetravels2

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.widget.Toast
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity

class SenderActivity : AppCompatActivity() {

    // Initialize the buttons
    internal lateinit var enter_aes_s: TextView
    internal lateinit var AES_key_text_S: EditText
    internal lateinit var Encrypt: Button
    internal lateinit var select_s: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender1)

        // Request permission access
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
        }

        enter_aes_s = findViewById(R.id.enter_aes_s) as TextView
        AES_key_text_S = findViewById(R.id.aes_key_s) as EditText
        Encrypt = findViewById(R.id.encrypt_button) as Button
        select_s = findViewById(R.id.select_button_s) as Button

        // function for when select is clicked
        select_s.setOnClickListener {
            MaterialFilePicker()
                .withActivity(this@SenderActivity)
                .withRequestCode(1000)
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
        }

        // function for when encrypt is clicked
        Encrypt.setOnClickListener {
            // get the aes key from the text box
            val AES_key_s = AES_key_text_S.text.toString()

            try {
                AESEncrypt.Encrypt(filename, AES_key_s, this@SenderActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    // Navigating through phones files
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            filename = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            println(filename)

        }
    }

    // Sends alert for permission to go through files
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SenderActivity, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@SenderActivity,
                        "Permission not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    companion object {

        lateinit var filename: String
    }
}
