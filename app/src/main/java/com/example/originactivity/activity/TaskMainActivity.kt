package com.example.originactivity.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.entity.FavoriteTask
import com.example.originactivity.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_TASK = "com.example.originalactivity.TASK"

class TaskMainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mListView: ListView
    private lateinit var mTaskArrayList: ArrayList<FavoriteTask>
    private lateinit var mAdapter: TasklistAdapter
    private lateinit var mTask: Task

    private var isChildEventEnabled = false

    var dataBaseReference = FirebaseDatabase.getInstance().reference

    // ログイン済みのユーザーを取得する
    var user = FirebaseAuth.getInstance().currentUser

    //お気に入り一覧のListView作成
    private val fEventListener = object : ChildEventListener {

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

            val map = dataSnapshot.value as Map<String, String>
            // タスクIDとタイトルと締め切りを取り出す
            val taskid = dataSnapshot.key ?: ""

            // その質問の詳細を取得してリストに表示する
            dataBaseReference.child(Const.ContentsPATH).child(taskid).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = dataSnapshot.value as Map<String, Any>
                    val title = map.get("title") ?: ""
                    val date = map.get("date") ?: ""
                    val favoritetask = FavoriteTask(title as String, date)
                    mTaskArrayList.add(favoritetask)
                    mAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
        }
        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
        }
        override fun onChildRemoved(p0: DataSnapshot) {
        }
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }
        override fun onCancelled(p0: DatabaseError) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        setupListView()

        //関与しているタスクをリストで表示
        favoriteList()

        fab.setOnClickListener { view ->
            //タスク作成画面に遷移
            val intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
        }

        Search_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //タスク検索画面に遷移
        val intent = Intent(this, TaskSearchActivity::class.java)
        startActivity(intent)
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
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupListView(){
        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        // ListViewの準備
        mListView = this.findViewById(R.id.listView)
        mAdapter = TasklistAdapter(this)
        mTaskArrayList = ArrayList<FavoriteTask>()
        mAdapter.notifyDataSetChanged()

        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mTaskArrayList.clear()
        mAdapter.setTaskArrayList(mTaskArrayList)
        mListView.adapter = mAdapter

        /*
        val contentRef = dataBaseReference.child("contents")

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

        contentRef.addListenerForSingleValueEvent(
            object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    val count = datasnapshot.childrenCount
                    //関与しているタスクをリストに表示
                    appendAllItem(datasnapshot)
                    contentRef.removeEventListener(this)
                    isChildEventEnabled = true

                }
            }
        )*/

    }

    private fun appendAllItem(dsp: DataSnapshot) {
        // 質問IDとジャンルを取り出す
        val TaskId = dsp.key ?: ""

        // 関与しているタスクのタイトルと日付を取得してリストに表示する
        dataBaseReference.child(Const.Favorite)
                         .child(TaskId)
                         .addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val TaskArrayList = ArrayList<FavoriteTask>()
                val map = dataSnapshot.value as Map<String, String>?
                if (map != null) {
                    for (key in map.keys) {
                        val temp = map[key] as Map<String, String>
                        val Tasktitle = temp["title"] ?: ""
                        val Taskdate = temp["date"] ?: ""
                        var favoritetask = FavoriteTask(Tasktitle, Taskdate)
                        TaskArrayList.add(favoritetask)
                        mAdapter.notifyDataSetChanged()
                    }
            }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }

    private fun appendItem() {

    }

    private fun updateItem() {

    }

    private fun favoriteList(){
        dataBaseReference.child(Const.Favorite).child(user!!.uid).addChildEventListener(fEventListener)

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener { parent, view, position, id ->
            // Taskのインスタンスを渡して質問詳細画面を起動する
            val intent = Intent(applicationContext, TaskDetailActivity::class.java)
            intent.putExtra(EXTRA_TASK, position)
            startActivity(intent)
        }

        /*
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
                deleteTask(task.id)
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }*/

    }

    /*
    private fun deleteTask(taskId: Int){
        val results = mRealm
            .where(Task::class.java)
            .equalTo("id", taskId)
            .findAll()

        mRealm.beginTransaction()
        results.deleteAllFromRealm()
        mRealm.commitTransaction()

        val resultIntent = Intent(applicationContext, TaskAlarmReceiver::class.java)
        val resultPendingIntent = PendingIntent.getBroadcast(
            this,
            taskId,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(resultPendingIntent)

        reloadListView(mCategoryId)
    }*/

}
