package com.example.TaskManagement.model.entity

import java.io.Serializable

class Job (
    val title: String,
    val date: Long,
    val jobId: String,
    val userName: String
) : Serializable