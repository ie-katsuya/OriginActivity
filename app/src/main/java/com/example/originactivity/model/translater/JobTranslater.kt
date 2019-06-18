package com.example.originactivity.model.translater

import com.example.originactivity.model.entity.Job
import com.google.firebase.database.DataSnapshot

class JobTranslater {
    companion object{
        fun dataSnapshotToJob(dataSnapshot: DataSnapshot): Job{
            val map = dataSnapshot.value as? Map<String, Any>
            val title = map?.get("title") as? String ?: ""
            val date = map?.get("date")?.let { it as? Long } ?: 0L
            val jobId = dataSnapshot.key ?: ""
            return Job(title, date, jobId)
        }
    }
}