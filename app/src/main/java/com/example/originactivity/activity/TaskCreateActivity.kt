package com.example.originactivity.activity

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.list_tasks.*
import java.util.HashMap

class TaskCreateActivity : AppCompatActivity()  , View.OnClickListener {

    private lateinit var mTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)

        Buck_button.setOnClickListener(this)
        Decide_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        // キーボードが出てたら閉じる
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val TaskRef = dataBaseReference.child(Const.ContentsPATH)

        val data = HashMap<String, String>()

        if (v == Buck_button) {
            //タスク管理画面に遷移
            finish()
        }else if(v == Decide_button){
            val title = title_Edit.toString()
            val goal = goal_Edit.toString()
            val pass = pass_Edit.toString()

            if (title.isEmpty() == true) {
                // タイトルが入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "タイトルを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (goal.isEmpty() == true) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "目標を入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (pass.isEmpty() == true) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "パスワードを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            data["title"] = title
            data["goal"] = goal
            data["pass"] = pass

            TaskRef.push().setValue(data, this)
            finish()
        }
    }

}
