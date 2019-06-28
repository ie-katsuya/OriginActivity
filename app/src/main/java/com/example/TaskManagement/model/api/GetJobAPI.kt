package com.example.TaskManagement.model.api

import android.util.Log
import com.example.TaskManagement.Const
import com.example.TaskManagement.model.entity.Job
import com.example.TaskManagement.model.translater.JobTranslater
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GetJobAPI : FirebaseAPI() {

    fun getJob(taskId: String, callback: (List<Job>) -> Unit) {
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
                    getJobItem(datasnapshot, callback)
                    contentRef.removeEventListener(this)
                }
            }
        )
    }

    private fun getJobItem(datasnapshot: DataSnapshot, callback: (List<Job>) -> Unit) {
        //関与しているタスクをリストに表示
        val jobIdList = mutableListOf<String>()
        datasnapshot.children.forEach { item ->
            var jobId = item.key ?: return@forEach
            jobIdList.add(jobId)
        }
        val totalCount = datasnapshot.childrenCount
        var currentCount = 0L
        val jobList = mutableListOf<Job>()

        if(jobIdList.isEmpty()){
            callback(jobList)
            return
        }

        //val job = JobTranslater.dataSnapshotToJob(datasnapshot)

        JobTranslater.dataSnapshotToJob2(datasnapshot){
            jobList.add(it)

            if (currentCount >= totalCount) {
                callback(jobList)
            }
        }

    }


}