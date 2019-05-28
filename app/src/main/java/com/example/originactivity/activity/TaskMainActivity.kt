package com.example.originactivity.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import com.example.originactivity.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*

class TaskMainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Search_button.setOnClickListener(this)

        fab.setOnClickListener { view ->
            val intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        //タスク検索画面に遷移
        val intent = Intent(this, TaskSearchActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
