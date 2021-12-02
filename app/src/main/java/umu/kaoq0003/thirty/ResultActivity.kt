package umu.kaoq0003.thirty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thirty.R
import umu.kaoq0003.thirty.adapter.ResultAdapter
import umu.kaoq0003.thirty.data.Result

/**
 * Author: Karl Öqvist
 *
 * Activity for displaying the results after the game.
 *   - Gets data through the intent that it displays through an adapter.
 *   - Restart button restarts with a new game if clicked
 */

class ResultActivity : AppCompatActivity() {
    private var doubleBackPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val listView = findViewById<ListView>(R.id.result_listview)

        val list = intent.getParcelableArrayListExtra<Result>("results")
        val points = intent.getIntExtra("totalPoints", 0)

        val pointsText = findViewById<TextView>(R.id.result_variable_points_textview)

        val restartButton = findViewById<Button>(R.id.result_button_restart)

        restartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        pointsText.text = points.toString()

        listView.adapter = ResultAdapter(this, list)
    }

    override fun onBackPressed() {

        if (doubleBackPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        this.doubleBackPressedOnce = true
        Toast.makeText(this, "Click back again to exit application", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackPressedOnce = false
        }, 2000)
    }
}