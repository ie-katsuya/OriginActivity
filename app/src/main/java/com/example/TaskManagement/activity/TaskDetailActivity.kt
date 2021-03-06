package com.example.TaskManagement.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.example.TaskManagement.Const
import com.example.TaskManagement.R
import com.example.TaskManagement.adapter.TaskDetailAdapter
import com.example.TaskManagement.model.api.DeleteAPI
import com.example.TaskManagement.model.api.GetJobAPI
import com.example.TaskManagement.model.api.SyncJobAPI
import com.example.TaskManagement.model.entity.Job
import com.example.TaskManagement.model.entity.Task
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_detail.*
import java.text.SimpleDateFormat

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

    private val syncJobAPI by lazy { SyncJobAPI(task.taskId) }
    private val deleteAPI = DeleteAPI()
    private val getJobAPI = GetJobAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        task = intent.getSerializableExtra(KEY_TASK) as Task
        setTitle("タスク")

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()

        syncJobAPI.callback = {}
    }

    private fun initView() {
        syncJobAPI.callback = { job ->
            mAdapter.insertUpdateJob(job)
            listEmpty()
        }

        updateTitleLabel()
        updateDateLabel()

        setupListView()

        setListClickListener()

        add_button.setOnClickListener {
            //ジョブ追加画面に遷移
            startActivity(JobCreateActivity.createIntent(this, task.taskId, null))
        }
    }

    private fun updateTitleLabel() {
        val datetextview: TextView = findViewById(R.id.task_text)
        datetextview.text = task.title
    }

    private fun updateDateLabel() {
        //日付のフォーマット設定
        val sdf = SimpleDateFormat("yyyy年 M月 d日")
        val date = task.date

        val datetextview: TextView = findViewById(R.id.task_date)
        datetextview.text = sdf.format(date)
    }

    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView_detail)
        mAdapter = TaskDetailAdapter(this)

        mListView.adapter = mAdapter

        listEmpty()
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

            // ジョブを削除する
            val job = parent
                .adapter
                .getItem(position) as Job

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@TaskDetailActivity)

            builder.setTitle("削除")
            builder.setMessage(job.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                deleteJob(mAdapter.getJob(position))
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }

    }

    private fun deleteJob(job: Job) {
        deleteAPI.deleteJob(task, job){
            getJobAPI.getJob(task.taskId){
                mAdapter.setJobList(it)
            }
        }
        listEmpty()
    }

    override fun onResume() {
        super.onResume()

        syncJobAPI.syncStart()
        listEmpty()
    }

    override fun onPause() {
        super.onPause()

        syncJobAPI.syncStop()

    }

    override fun onBackPressed() {
        startActivity(TaskMainActivity.createIntent(this))
    }

    fun listEmpty() {
        if (mAdapter.isEmpty) {
            empty_message.setVisibility(View.VISIBLE)
        } else {
            empty_message.setVisibility(View.INVISIBLE)
        }
    }

    // アクションバーの戻る処理
    override fun onSupportNavigateUp(): Boolean {
        finish()
        startActivity(TaskMainActivity.createIntent(this))
        return super.onSupportNavigateUp()
    }
}
