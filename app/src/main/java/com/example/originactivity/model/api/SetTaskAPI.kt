package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class SetTaskAPI : FirebaseAPI() {

    fun setTask(taskData: HashMap<String, Any>, callback: (Boolean) -> Unit) {

        val taskRef = firebaseReference
            .child(Const.ContentsPATH)
            .push()

        val taskIdkey: String = taskRef.getKey()!!

        val userRef = firebaseReference
            .child(Const.UsersPATH).child(user!!.uid)

        val udata = HashMap<String, String>()

        //udata["name"] = userRef.

        taskRef.setValue(taskData) { error, detabaseReference ->
            if (error != null) {
                return@setValue
            }
            //taskRef.child(taskIdkey).child(Const.TaskUsers).push().setValue()
            taskData["title"]?.let {
                reloadId(taskIdkey, it) { isResult ->
                    if (!isResult) {
                        return@reloadId
                    }
                    callback(true)
                }
            }
        }
    }

    fun reloadId(taskIdkey: String, title: Any, complete: (Boolean) -> Unit) {

        val fdata = HashMap<String, Any>()
        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val favoriteRef = dataBaseReference.child(Const.Favorite).child(user!!.uid)

        favoriteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ref = favoriteRef.child(taskIdkey)
                fdata["title"] = title
                ref.setValue(fdata) { error, detabaseReference ->
                    val isResult: Boolean = if (error != null) {
                        false
                    } else {
                        true
                    }
                    complete(isResult)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

}