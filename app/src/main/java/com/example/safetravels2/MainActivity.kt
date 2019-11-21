package com.example.safetravels2

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // Initialize the buttons
    internal lateinit var sendDataButton: Button
    internal lateinit var receiveDataButton: Button
    internal lateinit var googleDriveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This finds your button on the main activity
        sendDataButton = findViewById(R.id.send_button) as Button
        receiveDataButton = findViewById(R.id.receive_button) as Button
        googleDriveButton = findViewById(R.id.google_drive_button) as Button


        // This creates a action when send button is pressed
        sendDataButton.setOnClickListener {
            // This intent moves you from one activity to the next one
            val goToSendActivity = Intent(applicationContext, SenderActivity::class.java)
            // Starts up the activity
            startActivity(goToSendActivity)
        }


        // This creates a action when receive button is pressed
        receiveDataButton.setOnClickListener {
            // This intent moves you from one activity to the next one
            val goToReceiveActivity = Intent(applicationContext, ReceiverActivity::class.java)
            // Starts up the activity
            startActivity(goToReceiveActivity)
        }


        // This creates a action when google drive button is pressed
        googleDriveButton.setOnClickListener {
            // This intent moves you from one activity to the next one
            val goToReceiveActivity = Intent(applicationContext, googledrive::class.java)
            // Starts up the activity
            startActivity(goToReceiveActivity)
        }

    }
}
