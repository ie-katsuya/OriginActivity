package com.example.TaskManagement.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, StartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ログイン済みのユーザーを取得する
        val user = FirebaseAuth.getInstance().currentUser

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            //ログイン画面に遷移
            startActivity(LoginActivity.createIntent(this, false))
        } else {
            //メイン画面に遷移
            startActivity(TaskMainActivity.createIntent(this))
        }
        finish()
    }
}