package com.example.TaskManagement.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.example.TaskManagement.R
import com.example.TaskManagement.adapter.TasklistAdapter
import com.example.TaskManagement.model.api.DeleteAPI
import com.example.TaskManagement.model.api.GetTaskAPI
import com.example.TaskManagement.model.entity.Task
import kotlinx.android.synthetic.main.activity_main.*

class TaskMainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TaskMainActivity::class.java)
        }
    }

    private lateinit var mListView: ListView
    private lateinit var mAdapter: TasklistAdapter

    private val gettaskAPI = GetTaskAPI()
    private var isChildEventEnabled = false
    private val deleteAPI = DeleteAPI()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("タスク管理")
        setupListView()
        menu_view.setVisibility(View.INVISIBLE)

        //リストをタッチした処理
        ListTouch()

        fab.setOnClickListener { view ->
            //タスク作成画面に遷移
            //startActivity(TaskCreateActivity.createIntent(this))
            if(menu_view.visibility != View.VISIBLE) {
                menu_view.setVisibility(View.VISIBLE)
                add_fab.setVisibility(View.VISIBLE)
                search_fab.setVisibility(View.VISIBLE)
            }else{
                menu_view.setVisibility(View.GONE)
                add_fab.setVisibility(View.GONE)
                search_fab.setVisibility(View.GONE)
            }
        }

        add_fab.setOnClickListener{
            startActivity(TaskCreateActivity.createIntent(this))
        }

        search_fab.setOnClickListener{
            startActivity(TaskSearchActivity.createIntent(this))
        }
    }

    override fun onClick(v: View?) {
        //タスク検索画面に遷移
        startActivity(TaskSearchActivity.createIntent(this))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        //アカウント設定画面に遷移
        if (id == R.id.action_settings) {
            startActivity(SettingActivity.createIntent(this))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView)
        mAdapter = TasklistAdapter(this)

        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mListView.adapter = mAdapter

        gettaskAPI.getTask {
            mAdapter.setTaskList(it)
            isChildEventEnabled = true
        }
    }

    private fun ListTouch() {

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener { parent, view, position, id ->
            // Taskのインスタンスを渡して質問詳細画面を起動する
            startActivity(TaskDetailActivity.createIntent(this, mAdapter.getTask(position)))
        }

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener { parent, _, position, _ ->

            // タスクを削除する
            val task = parent
                .adapter
                .getItem(position) as Task

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@TaskMainActivity)
            val view = View.inflate(this, R.layout.list_task_menu, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            view.findViewById<View>(R.id.cancel).setOnClickListener {
                dialog.dismiss()
            }

            view.findViewById<View>(R.id.remove_task).setOnClickListener {
                deleteAllTask(mAdapter.getTask(position))
                dialog.dismiss()
            }

            view.findViewById<View>(R.id.remove_favorite).setOnClickListener {
                deleteTask(mAdapter.getTask(position))
                dialog.dismiss()
            }

            true
        }

    }


    private fun deleteTask(task: Task) {
        deleteAPI.deleteUser(task) {
            updateTaskList()
        }
    }

    private fun deleteAllTask(task: Task) {
        deleteAPI.deleteAllTask(task) {
            updateTaskList()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        menu_view.setVisibility(View.GONE)
        add_fab.setVisibility(View.GONE)
        search_fab.setVisibility(View.GONE)

        updateTaskList()
    }

    override fun onBackPressed() {
        // バックキーの無効化
        moveTaskToBack(true)
    }

    private fun updateTaskList() {
        gettaskAPI.getTask {
            mAdapter.setTaskList(it)
        }
    }
}
