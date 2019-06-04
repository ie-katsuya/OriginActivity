package com.example.originactivity.model.api

import com.example.originactivity.Const
import java.util.*

class SetJobAPI : FirebaseAPI() {

    fun setJob(taskId: String, title: String, date: Long) {

        val jobRef = firebaseReference
            .child(Const.ContentsPATH)
            .child(taskId)
            .child(Const.JobPATH)

        val jobData = HashMap<String, Any>()

        jobData["title"] = title
        jobData["date"] = date

        jobRef.push().setValue(jobData)
    }


}