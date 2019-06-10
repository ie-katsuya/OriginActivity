package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.originactivity.R
import com.example.originactivity.model.entity.Job

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
    }
}
