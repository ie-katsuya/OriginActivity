package com.example.TaskManagement.model.api

import com.example.TaskManagement.Const
import com.example.TaskManagement.model.entity.Job
import com.example.TaskManagement.model.translater.JobTranslater
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class SyncJobAPI(taskId: String) : FirebaseAPI() {

    var callback: (Job) -> Unit = {}

    private val jobRef = firebaseReference
        .child(Const.ContentsPATH)
        .child(taskId)
        .child(Const.JobPATH)

    fun syncStart() {
        jobRef.addChildEventListener(mEventListener)
    }

    fun syncStop() {
        jobRef.removeEventListener(mEventListener)
    }

    private val mEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(datasnapshot: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(datasnapshot: DataSnapshot, p1: String?) {
            val job = JobTranslater.dataSnapshotToJob(datasnapshot)
            callback(job)
        }

        override fun onChildAdded(datasnapshot: DataSnapshot, p1: String?) {
            val job = JobTranslater.dataSnapshotToJob(datasnapshot)
            callback(job)
        }

        override fun onChildRemoved(datasnapshot: DataSnapshot) {
            val job = JobTranslater.dataSnapshotToJob(datasnapshot)
            callback(job)
        }

    }
}