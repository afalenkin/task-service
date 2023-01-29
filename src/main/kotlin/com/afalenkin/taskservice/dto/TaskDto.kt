package com.afalenkin.taskservice.dto

import com.afalenkin.taskservice.model.Priority
import java.time.LocalDateTime

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
data class TaskDto(
    val id: Long,
    val description: String,
    val hasReminder: Boolean,
    val isOpen: Boolean,
    val created: LocalDateTime,
    val priority: Priority,
)
