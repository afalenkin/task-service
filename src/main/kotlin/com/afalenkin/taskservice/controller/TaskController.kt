package com.afalenkin.taskservice.controller

import com.afalenkin.taskservice.dto.TaskCreateRequest
import com.afalenkin.taskservice.dto.TaskDto
import com.afalenkin.taskservice.dto.TaskUpdateRequest
import com.afalenkin.taskservice.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@RestController
@RequestMapping("api/v1/tasks")
class TaskController(
    private val service: TaskService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllTasks(): List<TaskDto> = service.getAll()

    @GetMapping("/closed")
    @ResponseStatus(HttpStatus.OK)
    fun getAllClosedTasks(): List<TaskDto> = service.getAllClosed()

    @GetMapping("/opened")
    @ResponseStatus(HttpStatus.OK)
    fun getAllOpenedTasks(): List<TaskDto> = service.getAllOpen()

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long) = service.getById(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteById(@PathVariable id: Long) = service.delete(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody createRequest: TaskCreateRequest) = service.create(createRequest)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: Long, @Valid @RequestBody updateRequest: TaskUpdateRequest) =
        service.update(id, updateRequest)

}