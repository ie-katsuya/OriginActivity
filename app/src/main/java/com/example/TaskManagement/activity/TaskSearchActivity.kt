package com.example.TaskManagement.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ListView
import android.widget.TextView
import com.example.TaskManagement.R
import com.example.TaskManagement.adapter.TasklistAdapter
import com.example.TaskManagement.model.api.GetTaskAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_task_search.*


class TaskSearchActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TaskSearchActivity::class.java)
        }
    }

    private lateinit var mListView: ListView
    private lateinit var mAdapter: TasklistAdapter

    private val gettaskAPI = GetTaskAPI()

    // ログイン済みのユーザーを取得する
    protected val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_search)
        setTitle("タスク検索")

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        setupListView()

        //リストをタッチした処理
        listTouch()

        searchButton.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        val keyWord = search_edit.text.toString()
        mAdapter.filterTask(keyWord)
    }

    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView)
        mAdapter = TasklistAdapter(this)

        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mListView.adapter = mAdapter

        gettaskAPI.getTaskSearch { taskList ->
            mAdapter.setTaskList(taskList)
        }

    }

    private fun listTouch() {
        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener { parent, view, position, id ->
           val sameUserflag = mAdapter.userFilter(user.uid, position)

            if (sameUserflag) {
                //Favoriteに登録していたら、詳細画面へ
                startActivity(TaskDetailActivity.createIntent(this, mAdapter.getTask(position)))
                finish()
            }else{
                //Favoriteになかったら、パスワードチェック画面へ
                startActivity(PassCheckActivity.createIntent(this, mAdapter.getTask(position)))
            }
        }
    }

    // 追加
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
