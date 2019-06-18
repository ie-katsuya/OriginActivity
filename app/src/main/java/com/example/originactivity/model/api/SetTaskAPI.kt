package com.example.originactivity.model.api

import com.example.originactivity.Const
import com.example.originactivity.model.entity.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SetTaskAPI : FirebaseAPI() {

    fun setTask(taskData: HashMap<String, Any>, callback: (Boolean) -> Unit) {

        val taskRef = firebaseReference
            .child(Const.ContentsPATH)
            .push()

        val taskIdkey: String = taskRef.getKey()!!

        taskRef.setValue(taskData) { error, detabaseReference ->
            if (error != null) {
                return@setValue
            }
            userSave(taskIdkey) { isResult ->
                if (!isResult) {
                    return@userSave
                }

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
    }

    fun reloadId(taskIdkey: String, title: Any, complete: (Boolean) -> Unit) {

        val fdata = HashMap<String, Any>()
        val favoriteRef = firebaseReference.child(Const.Favorite).child(user!!.uid)

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

    fun userSave(taskIdkey: String, complete: (Boolean) -> Unit) {
        val udata = HashMap<String, Any>()

        val userRef = firebaseReference
            .child(Const.ContentsPATH)
            .child(taskIdkey)
            .child(Const.UsersPATH)
            .child(user!!.uid)

        val userNameRef = firebaseReference
            .child(Const.UsersPATH)
            .child(user!!.uid)

        userNameRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //未使用
                }

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    getUserName(datasnapshot){user->
                        udata["name"] = user

                        userRef.setValue(udata) { error, detabaseReference ->
                            val isResult: Boolean = if (error != null) {
                                false
                            } else {
                                true
                            }
                            complete(isResult)
                        }
                    }
                    userNameRef.removeEventListener(this)
                }
            }
        )

    }

    fun favoriteSave(title: String, taskIdkey: String, complete: (Boolean) -> Unit) {
        val fdata = HashMap<String, Any>()
        fdata["title"] = title

        val favoriteRef = firebaseReference
            .child(Const.Favorite)
            .child(user!!.uid)
            .child(taskIdkey)

        favoriteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                favoriteRef.setValue(fdata) { error, detabaseReference ->
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

    private fun getUserName(dataSnapshot: DataSnapshot, complete: (String)-> Unit){
        val map = dataSnapshot.value as Map<String, Any>
        val userName = map.get("name") as String
        complete(userName)
    }
}