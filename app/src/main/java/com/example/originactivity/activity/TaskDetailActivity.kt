package com.example.originactivity.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.originactivity.R
import com.example.originactivity.entity.FavoriteTask
import kotlinx.android.synthetic.main.activity_task_detail.*

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var mTaskArrayList: ArrayList<FavoriteTask>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // EXTRA_TASK から Task の id を取得して、 id から Task のインスタンスを取得する
        val intent = intent
        val taskposition = intent.getIntExtra(EXTRA_TASK, -1)


    }
}
