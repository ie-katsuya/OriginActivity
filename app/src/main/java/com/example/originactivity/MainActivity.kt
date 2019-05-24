package com.example.originactivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mDatabaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ログイン済みのユーザーを取得する
        val user = FirebaseAuth.getInstance().currentUser

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            Acount_button.setVisibility(View.INVISIBLE)
            Login_button.setVisibility(View.VISIBLE)
            Create_button.setVisibility(View.INVISIBLE)
            TaskList_button.setVisibility(View.INVISIBLE)
        }else{
            Acount_button.setVisibility(View.VISIBLE)
            Login_button.setVisibility(View.INVISIBLE)
            Create_button.setVisibility(View.VISIBLE)
            TaskList_button.setVisibility(View.VISIBLE)
        }

        Login_button.setOnClickListener(this)
        TaskList_button.setOnClickListener(this)
        Create_button.setOnClickListener(this)
        Acount_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v == Login_button){
            //ログイン画面に遷移
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else if(v == TaskList_button){
            //タスク管理画面に遷移
            //val intent = Intent(this, TaskListActivity::class.java)
            //startActivity(intent)
        }else if(v == Create_button){
            //タスク作成画面に遷移
            //val intent = Intent(this, CreateActivity::class.java)
            //startActivity(intent)
        }else if(v == Acount_button){
            //アカウント設定画面に遷移
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
