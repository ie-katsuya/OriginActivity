package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.originactivity.R
import com.example.originactivity.model.entity.Job
import kotlinx.android.synthetic.main.activity_job_detail.*
import java.text.SimpleDateFormat

class JobDetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val KEY_TASK_ID = "KEY_TASK_ID"
        const val KEY_JOB = "KEY_JOB"

        fun createIntent(context: Context, taskId: String, job: Job?): Intent {
            return Intent(context, JobDetailActivity::class.java).also {
                it.putExtra(KEY_TASK_ID, taskId)
                it.putExtra(KEY_JOB, job)
            }

        }
    }

    private lateinit var taskId: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        taskId = intent.getStringExtra(KEY_TASK_ID)
        job = intent.getSerializableExtra(KEY_JOB) as Job

        val titletextview: TextView = findViewById(R.id.job_textview)
        titletextview.text = job?.title

        //日付のフォーマット設定
        val sdf = SimpleDateFormat("yyyy年 M月 d日")
        val date = job?.date

        val datetextview: TextView = findViewById(R.id.date_textview)
        datetextview.text = "完了予定日： " + sdf.format(date)

        edit_button.setOnClickListener(this)
        back_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            back_button.id -> {
                finish()
            }
            else -> {
                //タスク作成画面に遷移
                startActivity(JobCreateActivity.createIntent(this, taskId, job))
            }
        }
    }

}