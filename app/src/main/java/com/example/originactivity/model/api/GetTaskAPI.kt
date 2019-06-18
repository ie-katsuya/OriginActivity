package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Job
import com.example.originactivity.model.entity.Task
import com.example.originactivity.model.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GetTaskAPI : FirebaseAPI() {

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
                    val allTask = data.toTask()
                    taskList.add(allTask)
                    if (currentCount >= totalCount) {
                        callback(taskList)
                    }
                }
            })
        }
    }

    fun getTaskSearch(callback: (List<Task>) -> Unit) {

        val contentRef = firebaseReference
            .child(Const.ContentsPATH)

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

    private fun DataSnapshot.toTask(): Task {
        val map = this.value as Map<String, Any>
        val title = map.get("title") as? String ?: ""
        val pass = map.get("pass") as? String ?: ""
        val goal = map.get("goal") as? String ?: ""
        val date = map.get("date") as? Long ?: 0L
        val taskUid = this.key ?: ""

        val job = map.get("job") as? HashMap<String, Any>
        val jobs = mutableListOf<Job>()
        job?.forEach {
            val value = it.value as? HashMap<String, Any> ?: return@forEach
            val jobTitle = value["title"] as? String ?: ""
            val jobDate = value["date"] as? Long ?: 0L
            val jobId = it.key ?: ""
            jobs.add(Job(jobTitle, jobDate, jobId))
        }

        val inputUsers = map.get("users") as? HashMap<String, String>
        val outputUsers = mutableListOf<User>()
        //Firebaseから取得した連想配列をユーザーリストに変換
        inputUsers?.forEach {user->
            val value = user.value as? HashMap<String, String> ?: return@forEach
            val userId = user.key
            val userName = value["name"]  ?: ""
            outputUsers.add(User(userId, userName))
        }

        return Task(
            title,
            pass,
            goal,
            date,
            taskUid,
            jobs,
            outputUsers
        )
    }


}