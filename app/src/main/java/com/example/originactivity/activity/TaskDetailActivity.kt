package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.example.originactivity.R
import com.example.originactivity.adapter.TaskDetailAdapter
import com.example.originactivity.model.api.GetJobAPI
import com.example.originactivity.model.entity.Task
import kotlinx.android.synthetic.main.activity_task_detail.*

class TaskDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var task: Task

    companion object {
        private const val KEY_TASK = "KEY_TASK"

        fun createIntent(context: Context, task: Task): Intent {
            return Intent(context, TaskDetailActivity::class.java).also {
                it.putExtra(KEY_TASK, task)
            }

        }
    }

    private lateinit var mListView: ListView
    private lateinit var mAdapter: TaskDetailAdapter

    private val jobAPI = GetJobAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        task = intent.getSerializableExtra(KEY_TASK) as Task

        setValue(task)

        setupListView()

        add_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //ジョブ追加画面に遷移
        startActivity(AddJobActivity.createIntent(this, task))
    }

    private fun setValue(task: Task) {
        val titletextview: TextView = findViewById(R.id.title_textview)
        titletextview.text = task.title

        val goaltextview: TextView = findViewById(R.id.goal_textview)
        goaltextview.text = task.goal
    }

    private fun setupListView() {
        // ListViewの準備
        mListView = this.findViewById(R.id.listView_detail)
        mAdapter = TaskDetailAdapter(this)

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

        jobAPI.getJob(task.taskId) {
            mAdapter.setJobList(it)
        }
    }


}
