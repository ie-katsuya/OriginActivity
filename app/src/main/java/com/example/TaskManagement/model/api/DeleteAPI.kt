package com.example.TaskManagement.model.api

import com.example.TaskManagement.Const
import com.example.TaskManagement.model.entity.Task
import com.google.firebase.database.*
import javax.security.auth.callback.Callback

class DeleteAPI : FirebaseAPI()  {

    fun deleteUser(task: Task) {

        var deleteFavoriteRef = FirebaseDatabase.getInstance().reference
            .child(Const.Favorite)
            .child(user!!.uid)
            .child(task.taskId)

        var deleteUsersRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)
            .child(Const.UsersPATH)
            .child(user!!.uid)

        deleteUsersRef.removeValue()
        deleteFavoriteRef.removeValue()
    }

    fun deleteFavoriteTask(task: Task) {
        var deleteFavoriteUserTask = FirebaseDatabase.getInstance().reference
            .child(Const.Favorite)

        deleteFavoriteUserTask.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getdeleteItem(task.taskId, datasnapshot)
                    deleteFavoriteUserTask.removeEventListener(this)
                }
            }
        )
    }

    private fun getdeleteItem(taskId: String, dataSnapshot: DataSnapshot){
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

            taskDetabaseReference.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    taskDetabaseReference.removeValue()
                    taskDetabaseReference.removeEventListener(this)
                }

            })
        }
    }

    fun deleteTask(task: Task) {

        var deleteTaskRef = FirebaseDatabase.getInstance().reference
            .child(Const.ContentsPATH)
            .child(task.taskId)

        deleteTaskRef.removeValue()
    }
}