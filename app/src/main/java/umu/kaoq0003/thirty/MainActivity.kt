package umu.kaoq0003.thirty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.thirty.R

/**
 * Author: Karl Öqvist
 *
 * Starting activity, handles a welcome screen and start button
 *   - Starts the GameActivity when the button is clicked
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val beginButton = findViewById<Button>(R.id.begin_button)

        beginButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}

