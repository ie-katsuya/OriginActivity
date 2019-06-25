package com.example.TaskManagement.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.example.TaskManagement.R
import com.example.TaskManagement.model.entity.Job
import kotlinx.android.synthetic.main.activity_job_detail.*
import java.text.SimpleDateFormat

class JobDetailActivity : AppCompatActivity() {
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

        setTitle(job!!.title)

        updateContentLabel()
        updateDateLabel()
        updateUserNameLabel()

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        edit_button.setOnClickListener {
            finish()
            //ジョブ作成画面に遷移
            startActivity(JobCreateActivity.createIntent(this, taskId, job))
        }
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

        userNameTextView.text = "担当者： " + job?.userName
    }

    // アクションバーの戻る処理
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
