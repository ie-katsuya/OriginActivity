package com.example.originactivity.model

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskAPI {

    private val firebaseReference = FirebaseDatabase.getInstance().reference

    // ログイン済みのユーザーを取得する
    private val user = FirebaseAuth.getInstance().currentUser

    fun getTask(callback: (List<Task>) -> Unit) {
        val uid = user?.uid ?: return
        val contentRef = firebaseReference
            .child(Const.Favorite)
            .child(uid)

        contentRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getAllItem(datasnapshot, callback)
                    contentRef.removeEventListener(this)
                }
            }
        )
    }

    private fun getAllItem(datasnapshot: DataSnapshot, callback: (List<Task>) -> Unit) {
        val count = datasnapshot.childrenCount
        //関与しているタスクをリストに表示
        val taskIdList = mutableListOf<String>()
        datasnapshot.children.forEach { item ->
            var taskId = item.key ?: return@forEach
            taskIdList.add(taskId)
        }
        val totalCount = datasnapshot.childrenCount
        var currentCount = 0L
        val taskList = mutableListOf<Task>()

        taskIdList.forEach { taskId ->
            val taskDetabaseReference = firebaseReference
                .child(Const.ContentsPATH)
                .child(taskId)
            taskDetabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(data: DataSnapshot) {
                    taskDetabaseReference.removeEventListener(this)
                    currentCount++
                    val favoriteTask = data.toTask()
                    taskList.add(favoriteTask)
                    if (currentCount >= totalCount) {
                        callback(taskList)
                    }
                }
            })
        }
    }

    private fun DataSnapshot.toTask(): Task {
        val map = this.value as Map<String, Any>
        val title = map.get("title") ?: ""
        val pass = map.get("pass") ?: ""
        val goal = map.get("goal") ?: ""
        val date = map.get("date")?.let { it as? Long } ?: 0L
        val taskUid = this.key ?: ""
        return Task(title as String, pass as String, goal as String, date, taskUid)
    }
}