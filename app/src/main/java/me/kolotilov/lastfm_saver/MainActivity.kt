package me.kolotilov.lastfm_saver

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}