package com.afalenkin.taskservice.service

import com.afalenkin.taskservice.dto.TaskCreateRequest
import com.afalenkin.taskservice.dto.TaskDto
import com.afalenkin.taskservice.dto.TaskUpdateRequest
import com.afalenkin.taskservice.exceptions.BadRequestException
import com.afalenkin.taskservice.exceptions.TaskNotFoundException
import com.afalenkin.taskservice.model.Task
import com.afalenkin.taskservice.repository.TaskRepository
import org.springframework.stereotype.Service

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@Service
class TaskService(
    private val repository: TaskRepository
) {
    fun getById(id: Long): TaskDto {
        checkById(id)
        return toDto(repository.findTaskById(id))
    }

    fun getAll(): List<TaskDto> = repository.findAll().map { toDto(it) }

    fun getAllOpen(): List<TaskDto> = repository.findAllOpenTasks().map { toDto(it) }

    fun getAllClosed(): List<TaskDto> = repository.findAllClosedTasks().map { toDto(it) }

    fun create(request: TaskCreateRequest): TaskDto {
        checkDescription(request.description)
        return toDto(repository.save(fromDto(request)))
    }

    fun update(id: Long, request: TaskUpdateRequest): TaskDto {
        checkById(id)
        val task = withUpdatedFields(repository.findTaskById(id), request)
        return toDto(repository.save(task))
    }

    fun delete(id: Long) {
        checkById(id)
        repository.deleteById(id)
    }

    fun checkById(id: Long) {
        if (!repository.existsById(id)) {
            throw TaskNotFoundException("Task with id = $id is not exists")
        }
    }

    fun toDto(task: Task): TaskDto = TaskDto(
        id = task.id!!,
        description = task.description,
        hasReminder = task.hasReminder,
        isOpen = task.isOpen,
        created = task.created,
        priority = task.priority,
    )

    fun fromDto(dto: TaskCreateRequest): Task = Task(
        description = dto.description,
        hasReminder = dto.hasReminder,
        isOpen = dto.isOpen,
        priority = dto.priority,
    )

    fun withUpdatedFields(task: Task, dto: TaskUpdateRequest): Task {
        return task.copy(
            id = task.id,
            description = checkDescription(dto, task),
            hasReminder = dto.hasReminder ?: task.hasReminder,
            isOpen = dto.isOpen ?: task.isOpen,
            created = task.created,
            priority = dto.priority ?: task.priority,
        )
    }

    private fun checkDescription(dto: TaskUpdateRequest, task: Task): String {
        val newDescription = dto.description
        return if (newDescription == null || newDescription == task.description) {
            task.description
        } else {
            checkDescription(newDescription)
            return newDescription
        }
    }

    private fun checkDescription(description: String) {
        if (!repository.descriptionIsNotUnique(description)) {
            throw BadRequestException("There is already exists task with description =  $description")
        }
    }

}