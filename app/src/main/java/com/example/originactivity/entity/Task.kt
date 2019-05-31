package com.example.originactivity.entity

import java.io.Serializable
import java.util.*

class Task(val title: String, val pass: String, val goal: String, val date: String, val fTask: ArrayList<FavoriteTask>) : Serializable {

}