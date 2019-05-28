package com.example.originactivity.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ログイン済みのユーザーを取得する
        val user = FirebaseAuth.getInstance().currentUser

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            //ログイン画面に遷移
            startActivity(LoginActivity.createIntent(this, false))
        }else{
            //メイン画面に遷移
            val intent = Intent(this, TaskMainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}