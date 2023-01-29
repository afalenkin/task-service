package com.afalenkin.taskservice.dto

import com.afalenkin.taskservice.model.Priority

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
data class TaskUpdateRequest(
    val id: Long?,
    val description: String?,
    val hasReminder: Boolean?,
    val isOpen: Boolean?,
    val priority: Priority?,
)
