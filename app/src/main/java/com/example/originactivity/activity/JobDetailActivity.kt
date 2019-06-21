package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
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
        setTitle("ジョブ詳細画面")

        taskId = intent.getStringExtra(KEY_TASK_ID)
        job = intent.getSerializableExtra(KEY_JOB) as Job

        updateContentLabel()
        updateDateLabel()
        updateUserNameLabel()

        edit_button.setOnClickListener(this)
        back_button.setOnClickListener(this)
    }

    private fun updateContentLabel() {
        val titleTextView = findViewById(R.id.job_textview) as TextView
        titleTextView.movementMethod = ScrollingMovementMethod.getInstance()

        titleTextView.text = job?.title
    }

    private fun updateDateLabel() {
        //日付のフォーマット設定
        val sdf = SimpleDateFormat("yyyy年 M月 d日")
        val date = job?.date

        val datetextview: TextView = findViewById(R.id.date_textview)
        datetextview.text = "完了予定日： " + sdf.format(date)
    }

    private fun updateUserNameLabel() {
        val userNameTextView = findViewById(R.id.user_name) as TextView

        userNameTextView.text = job?.userName
    }

    override fun onClick(v: View) {
        when (v.id) {
            back_button.id -> {
                finish()
            }
            else -> {
                finish()
                //タスク作成画面に遷移
                startActivity(JobCreateActivity.createIntent(this, taskId, job))
            }
        }
    }

    override fun onBackPressed() {
        // バックキーを押した際、タスク管理画面に移行
        startActivity(TaskMainActivity.createIntent(this))
    }
}
