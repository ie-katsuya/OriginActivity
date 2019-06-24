package com.example.TaskManagement.model.entity

import java.io.Serializable

data class Task(
    val title: String,
    val pass: String,
    val date: Long,
    val taskId: String,
    val jobs: List<Job>,
    var users: List<User>
) : Serializable
