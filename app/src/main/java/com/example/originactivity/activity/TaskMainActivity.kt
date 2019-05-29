package com.example.originactivity.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.entity.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class TaskMainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mListView: ListView
    private lateinit var mTaskArrayList: ArrayList<Task>
    private lateinit var mAdapter: TasklistAdapter

    var dataBaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListView()



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
        mTaskArrayList = ArrayList<Task>()
        mAdapter.notifyDataSetChanged()

        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mTaskArrayList.clear()
        mAdapter.setTaskArrayList(mTaskArrayList)
        mListView.adapter = mAdapter

        var isChildEventEnabled = false
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

                override fun onDataChange(p0: DataSnapshot) {
                    val count = p0.childrenCount
                    appendAllItem()
                    contentRef.removeEventListener(this)
                    isChildEventEnabled = true
                }
            }
        )

    }

    private fun appendAllItem() {

    }

    private fun appendItem() {

    }

    private fun updateItem() {

    }


}
