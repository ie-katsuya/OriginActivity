package com.example.TaskManagement.model.api

import com.example.TaskManagement.Const
import com.example.TaskManagement.model.entity.Job
import com.example.TaskManagement.model.entity.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeleteAPI : FirebaseAPI() {

    fun deleteUser(task: Task, callback: () -> Unit) {

        deleteTaskUser(task){
            deleteFavoriteUser(task){
                callback()
            }
        }
    }

    fun deleteAllTask(task: Task, callback: () -> Unit) {
        deleteTask(task){
            deleteUser(task){
                deleteFavoriteTask(task){
                    callback()
                }
            }
        }

    }

    fun deleteJob(task: Task, job: Job, callback: () -> Unit){
        var deleteJobRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)
            .child(Const.JobPATH)
            .child(job.jobId)

        deleteJobRef.removeValue { databaseError, databaseReference ->
            if (databaseError != null) {
                return@removeValue
            }
            callback()
        }
    }

    private fun deleteFavoriteTask(task: Task, callback: () -> Unit) {
        var deleteFavoriteUserTask = FirebaseDatabase.getInstance().reference
            .child(Const.Favorite)

        deleteFavoriteUserTask.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getdeleteItem(task.taskId, datasnapshot, callback)
                    deleteFavoriteUserTask.removeEventListener(this)
                }
            }
        )
    }

    private fun getdeleteItem(taskId: String, dataSnapshot: DataSnapshot, callback: () -> Unit) {
        //関与しているタスクをリストに表示
        val taskIdList = mutableListOf<String>()
        dataSnapshot.children.forEach { item ->
            var userId = item.key ?: return@forEach
            taskIdList.add(userId)
        }

        taskIdList.forEach { userId ->
            val taskDetabaseReference = firebaseReference
                .child(Const.Favorite)
                .child(userId)
                .child(taskId)

            taskDetabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    taskDetabaseReference.removeValue()
                    taskDetabaseReference.removeEventListener(this)
                    callback()
                }

            })
        }
    }

    //タスクの削除
    private fun deleteTask(task: Task, callback: () -> Unit) {

        var deleteTaskRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)

        deleteTaskRef.removeValue(){ databaseError, databaseReference ->
            if (databaseError != null) {
                return@removeValue
            }
            callback()
        }
    }

    private fun deleteTaskUser(task: Task, callback: () -> Unit) {
        var deleteUsersRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)
            .child(Const.UsersPATH)
            .child(user!!.uid)

        deleteUsersRef.removeValue { databaseError, databaseReference ->
            if (databaseError != null) {
                return@removeValue
            }

            callback()
        }
    }

    private fun deleteFavoriteUser(task: Task, callback: () -> Unit) {
        var deleteFavoriteRef = FirebaseDatabase.getInstance().reference
            .child(Const.Favorite)
            .child(user!!.uid)
            .child(task.taskId)

        deleteFavoriteRef.removeValue { databaseError, databaseReference ->
            if (databaseError != null) {
                return@removeValue
            }
            callback()
        }
    }
}