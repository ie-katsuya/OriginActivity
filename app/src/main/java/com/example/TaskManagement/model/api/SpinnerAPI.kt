package com.example.TaskManagement.model.api

import com.example.TaskManagement.Const
import com.example.TaskManagement.model.entity.User
import com.example.TaskManagement.model.translater.SpinnerTranslater.Companion.dataSnapshotToSpinner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SpinnerAPI : FirebaseAPI() {

    fun setSpinner(taskId: String, callback: (MutableList<User>) -> Unit) {

        val userRef = firebaseReference
            .child(Const.ContentsPATH)
            .child(taskId)
            .child(Const.UsersPATH)

        userRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getSpinner(taskId, datasnapshot, callback)
                    userRef.removeEventListener(this)
                }
            }
        )
    }

    private fun getSpinner(taskId: String, datasnapshot: DataSnapshot, callback: (MutableList<User>) -> Unit) {
        //関与しているタスクをリストに表示
        val userIdList = mutableListOf<String>()
        datasnapshot.children.forEach { item ->
            var userId = item.key ?: return@forEach
            userIdList.add(userId)
        }

        val totalCount = datasnapshot.childrenCount
        var currentCount = 0L
        val userList = mutableListOf<User>()

        userIdList.forEach { userId ->
            val taskDetabaseReference = firebaseReference
                .child(Const.ContentsPATH)
                .child(taskId)
                .child(Const.UsersPATH)
                .child(userId)
            taskDetabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(data: DataSnapshot) {
                    taskDetabaseReference.removeEventListener(this)
                    currentCount++
                    val allTask = dataSnapshotToSpinner(data)
                    userList.add(allTask)
                    if (currentCount >= totalCount) {
                        callback(userList)
                    }
                }
            })
        }

    }

}