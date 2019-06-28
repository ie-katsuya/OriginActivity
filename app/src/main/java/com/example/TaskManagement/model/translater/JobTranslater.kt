package com.example.TaskManagement.model.translater

import com.example.TaskManagement.model.entity.Job
import com.google.firebase.database.DataSnapshot

class JobTranslater {
    companion object{
        fun dataSnapshotToJob(dataSnapshot: DataSnapshot): Job{
            val map = dataSnapshot.value as? Map<String, Any>
            val title = map?.get("title") as? String ?: ""
            val date = map?.get("date")?.let { it as? Long } ?: 0L
            val jobUserName = map?.get("userName") as? String ?: ""
            val jobId = dataSnapshot.key ?: ""
            return Job(title, date, jobId, jobUserName)
        }

        fun dataSnapshotToJob2(dataSnapshot: DataSnapshot, callback: (Job) -> Unit) {
            val map = dataSnapshot.value as? Map<String, Any>
            val title = map?.get("title") as? String ?: ""
            val date = map?.get("date")?.let { it as? Long } ?: 0L
            val jobUserName = map?.get("userName") as? String ?: ""
            val jobId = dataSnapshot.key ?: ""
            callback(Job(title, date, jobId, jobUserName))
        }
    }
}