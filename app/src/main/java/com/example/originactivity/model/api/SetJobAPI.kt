package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Job
import java.util.*

class SetJobAPI : FirebaseAPI() {

    fun setJob(taskId: String, title: String, date: Long, callback: (Boolean) -> Unit) {

        val jobRef = firebaseReference
            .child(Const.ContentsPATH)
            .child(taskId)
            .child(Const.JobPATH)

        val jobData = HashMap<String, Any>()

        jobData["title"] = title
        jobData["date"] = date

        jobRef.push().setValue(jobData) { error, detabaseReference ->
            if (error != null) {
                return@setValue
            }
            callback(true)
        }
    }


}