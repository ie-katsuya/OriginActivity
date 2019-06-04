package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Job
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GetJobAPI : FirebaseAPI() {

    fun getJob(taskId: String, callback: (List<Job>) -> Unit) {
        val uid = user?.uid ?: return
        val contentRef = firebaseReference
            .child(Const.ContentsPATH)
            .child(taskId)
            .child(Const.JobPATH)
        contentRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getTaskItem(datasnapshot, callback)
                    contentRef.removeEventListener(this)
                }
            }
        )
    }

    private fun getTaskItem(datasnapshot: DataSnapshot, callback: (List<Job>) -> Unit) {
        //関与しているタスクをリストに表示
        val jobIdList = mutableListOf<String>()
        datasnapshot.children.forEach { item ->
            var jobId = item.key ?: return@forEach
            jobIdList.add(jobId)
        }
        val totalCount = datasnapshot.childrenCount
        var currentCount = 0L
        val jobList = mutableListOf<Job>()


    }

    private fun DataSnapshot.toJob(): Job {
        val map = this.value as Map<String, Any>
        val title = map.get("title") ?: ""
        val date = map.get("date")?.let { it as? Long } ?: 0L
        val jobUid = this.key ?: ""
        return Job(title as String, date, jobUid)
    }
}