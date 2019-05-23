package com.example.originactivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Rogin_button.setOnClickListener(this)
        TaskList_button.setOnClickListener(this)
        Create_buttun.setOnClickListener(this)
        Acount_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v == Rogin_button){
            //ログイン画面に遷移
            //val intent = Intent(this, RoginActivity::class.java)
            //startActivity(intent)
        }else if(v == TaskList_button){
            //タスク管理画面に遷移
            //val intent = Intent(this, TaskListActivity::class.java)
            //startActivity(intent)
        }else if(v == Create_buttun){
            //タスク作成画面に遷移
            //val intent = Intent(this, CreateActivity::class.java)
            //startActivity(intent)
        }else{
            //アカウント設定画面に遷移
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
