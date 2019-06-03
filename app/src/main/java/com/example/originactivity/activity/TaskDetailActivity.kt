package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.originactivity.R
import com.example.originactivity.model.entity.Task
import kotlinx.android.synthetic.main.activity_task_detail.*

class TaskDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val KEY_TASK = "KEY_TASK"

        fun createIntent(context: Context, task: Task): Intent {
            return Intent(context, TaskDetailActivity::class.java).also {
                it.putExtra(KEY_TASK, task)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // EXTRA_TASK から Task の id を取得して、 id から Task のインスタンスを取得する
        val task = intent.getSerializableExtra(KEY_TASK) as Task

        setValue(task)

        add_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //小タスク追加画面に遷移
        startActivity(AddContentActivity.createIntent(this))
    }

    private fun setValue(task: Task) {
        val titletextview: TextView = findViewById(R.id.title_textview)
        titletextview.text = task.title

        val goaltextview: TextView = findViewById(R.id.goal_textview)
        goaltextview.text = task.goal
    }

}
