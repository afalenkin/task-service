package com.afalenkin.taskservice.testdata

import com.afalenkin.taskservice.dto.TaskDto
import com.afalenkin.taskservice.model.Priority
import com.afalenkin.taskservice.model.Task
import java.time.LocalDate

fun createTask(id: Long) = Task(
    id = 1,
    description = "test data",
    hasReminder = true,
    isOpen = true,
    created = LocalDate.now().atStartOfDay(),
    priority = Priority.HIGH
)

fun createDtoForTask(id: Long) = TaskDto(
    id = 1,
    description = "test data",
    hasReminder = true,
    isOpen = true,
    created = LocalDate.now().atStartOfDay(),
    priority = Priority.HIGH
)