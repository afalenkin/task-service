package com.afalenkin.taskservice.dto

import com.afalenkin.taskservice.model.Priority
import jakarta.validation.constraints.NotBlank

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
data class TaskCreateRequest(
    @NotBlank(message = "missing updating task description")
    val description: String,
    val hasReminder: Boolean,
    val isOpen: Boolean,
    val priority: Priority,
)
