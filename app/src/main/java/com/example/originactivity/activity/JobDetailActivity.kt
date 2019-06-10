package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.originactivity.R
import com.example.originactivity.model.entity.Job
import com.example.originactivity.model.entity.Task

class JobDetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_TASK = "KEY_TASK"

        fun createIntent(context: Context, job: Job): Intent {
            return Intent(context, JobDetailActivity::class.java).also {
                it.putExtra(KEY_TASK, job)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        val job = intent.getSerializableExtra(JobDetailActivity.KEY_TASK) as Job


        val titletextview: TextView = findViewById(R.id.job_textview)
        titletextview.text = job.title

        val datetextview: TextView = findViewById(R.id.date_textview)
        datetextview.text = job.date.toString()
    }
}
