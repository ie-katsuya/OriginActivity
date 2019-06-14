package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Job
import com.example.originactivity.model.translater.JobTranslater
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

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(datasnapshot: DataSnapshot, p1: String?) {
            val job = JobTranslater.dataSnapshotToJob(datasnapshot)
            callback(job)
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }

    }
}