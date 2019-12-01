package com.example.safetravels2

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Spinner
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity

class ReceiverActivity : AppCompatActivity() {

    // Initialize the buttons
    internal lateinit var select: Button
    internal lateinit var enter_aes_r: TextView
    internal lateinit var AES_key_text_R: EditText
    internal lateinit var decrypt: Button
    internal lateinit var spinner1: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver1)

        // Request permission access
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
        }

        // This finds your button on the activity
        select = findViewById(R.id.select_button) as Button
        AES_key_text_R = findViewById(R.id.aes_key_r) as EditText
        enter_aes_r = findViewById(R.id.enter_aes_r) as TextView
        decrypt = findViewById(R.id.decrypt_button) as Button

        // function for when select is clicked
        select.setOnClickListener {
            MaterialFilePicker()
                .withActivity(this@ReceiverActivity)
                .withRequestCode(1000)
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
        }


        // function for when decrypt is clicked
        decrypt.setOnClickListener {
            // get the aes key from the text box
            val AES_key_r = AES_key_text_R.text.toString()

            try {
                AESDecrypt.Decrypt(extension_text, filename, AES_key_r, this@ReceiverActivity)
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
                    Toast.makeText(this@ReceiverActivity, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@ReceiverActivity,
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
        lateinit var extension_text: String
    }
}
