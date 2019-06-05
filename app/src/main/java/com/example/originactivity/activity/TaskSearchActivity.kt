package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.model.api.GetTaskAPI
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_search.*

class TaskSearchActivity : AppCompatActivity() , View.OnClickListener  {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TaskSearchActivity::class.java)
        }
    }

    private lateinit var mListView: ListView
    private lateinit var mAdapter: TasklistAdapter

    private val taskAPI = GetTaskAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_search)

        setupListView()

        //リストをタッチした処理
        ListTouch()

        searchButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val search = search_edit.text.toString()

        if (search.isEmpty()) {

        }
    }

    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView)
        mAdapter = TasklistAdapter(this)

        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mListView.adapter = mAdapter

        /*
        contentRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                if (!isChildEventEnabled) {
                    return
                }
            }

            override fun onChildMoved(datasnapshot: DataSnapshot, s: String?) {
                if (!isChildEventEnabled) {
                    return
                }
            }

            override fun onChildChanged(datasnapshot: DataSnapshot, s: String?) {
                if (!isChildEventEnabled) {
                    return
                }
                updateItem()
            }

            override fun onChildAdded(datasnapshot: DataSnapshot, s: String?) {
                if (!isChildEventEnabled) {
                    return
                }
                //追加する1件
                appendItem()
            }

            override fun onChildRemoved(datasnapshot: DataSnapshot) {
                if (!isChildEventEnabled) {
                    return
                }
            }
        })
*/

        taskAPI.getTask {
            mAdapter.setTaskList(it)
        }
    }

    private fun ListTouch() {
        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener { parent, view, position, id ->
            // Taskのインスタンスを渡して質問詳細画面を起動する
            startActivity(PassCheckActivity.createIntent(this, mAdapter.getTask(position)))
        }

    }
}
