package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TaskDetailAdapter
import com.example.originactivity.model.api.GetJobAPI
import com.example.originactivity.model.api.SyncJobAPI
import com.example.originactivity.model.entity.Job
import com.example.originactivity.model.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_detail.*

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var task: Task

    companion object {
        const val KEY_TASK = "KEY_TASK"

        fun createIntent(context: Context, task: Task): Intent {
            return Intent(context, TaskDetailActivity::class.java).also {
                it.putExtra(KEY_TASK, task)
            }
        }
    }

    private lateinit var mListView: ListView
    private lateinit var mAdapter: TaskDetailAdapter

    private val jobAPI = GetJobAPI()

    private val syncJobAPI by lazy { SyncJobAPI(task.taskId)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        task = intent.getSerializableExtra(KEY_TASK) as Task

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()

        syncJobAPI.callback = {}
    }

    private fun initView() {
        syncJobAPI.callback = {job ->
            //
        }

        updateTitleLabel()
        updateGoalLabel()

        setupListView()

        setListClickListener()

        add_button.setOnClickListener {
            //ジョブ追加画面に遷移
            startActivity(JobCreateActivity.createIntent(this, task.taskId, null))
        }

        backmain_button.setOnClickListener {
            //メイン画面に遷移
            startActivity(TaskMainActivity.createIntent(this))
        }
    }

    private fun updateTitleLabel() {
        val titletextview: TextView = findViewById(R.id.title_textview)
        titletextview.text = task.title
    }

    private fun updateGoalLabel() {
        val goaltextview: TextView = findViewById(R.id.goal_textview)
        goaltextview.text = task.goal
    }


    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView_detail)
        mAdapter = TaskDetailAdapter(this)

        mListView.adapter = mAdapter

        mAdapter.setJobList(task.jobs)
    }

    // ListViewをタップしたときの処理
    private fun setListClickListener() {
        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener { parent, view, position, id ->
            // jobのインスタンスを渡して質問詳細画面を起動する
            startActivity(JobDetailActivity.createIntent(this, task.taskId, mAdapter.getJob(position)))
        }

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener { parent, _, position, _ ->

            // タスクを削除する
            val task = parent
                .adapter
                .getItem(position) as Task

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@TaskDetailActivity)

            builder.setTitle("削除")
            builder.setMessage(task.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                deleteJob(mAdapter.getJob(position))
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }

    }

    //TODO: 後ほどAPI化
    private fun deleteJob(job: Job) {
        // ログイン済みのユーザーを取得する
        val user = FirebaseAuth.getInstance().currentUser

        var deleteRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)
            .child(Const.JobPATH)

        deleteRef.removeValue()

        jobAPI.getJob(task.taskId) {
            mAdapter.setJobList(it)
        }
    }

    override fun onResume() {
        super.onResume()

        syncJobAPI.syncStart()
    }

    override fun onPause() {
        super.onPause()

        syncJobAPI.syncStop()
    }

}
