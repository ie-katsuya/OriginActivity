package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.model.api.GetTaskAPI
import com.example.originactivity.model.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("管理画面")
        setupListView()

        //リストをタッチした処理
        ListTouch()

        fab.setOnClickListener { view ->
            //タスク作成画面に遷移
            startActivity(TaskCreateActivity.createIntent(this))
        }

        search_button.setOnClickListener(this)
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

            builder.setTitle("削除")
            builder.setMessage(task.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                deleteTask(mAdapter.getTask(position))
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }

    }

    //後ほどAPI化
    private fun deleteTask(task: Task) {
        // ログイン済みのユーザーを取得する
        val user = FirebaseAuth.getInstance().currentUser

        var deleteRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)

        var deleteFavoriteRef = FirebaseDatabase.getInstance().reference
            .child(Const.Favorite)
            .child(user!!.uid)
            .child(task.taskId)

        var deleteUsersRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)
            .child(Const.UsersPATH)

        deleteRef.removeValue()
        deleteFavoriteRef.removeValue()
        deleteUsersRef.removeValue()

        gettaskAPI.getTask {
            mAdapter.setTaskList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        gettaskAPI.getTask {
            mAdapter.setTaskList(it)
        }
    }

    override fun onBackPressed() {
        // バックキーの無効化
        moveTaskToBack (true)
    }
}
