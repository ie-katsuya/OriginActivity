package com.example.originactivity.model.translater

import com.example.originactivity.model.entity.Job
import com.example.originactivity.model.entity.User
import com.google.firebase.database.DataSnapshot

class SpinnerTranslater {
    companion object{
        fun dataSnapshotToSpinner(dataSnapshot: DataSnapshot): User {
            val map = dataSnapshot.value as? Map<String, Any>
            val name = map?.get("name") as? String ?: ""
            val userId = dataSnapshot.key ?: ""
            return User(userId, name)
        }
    }
}