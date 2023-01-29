package com.afalenkin.taskservice.repository

import com.afalenkin.taskservice.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    fun findTaskById(id: Long): Task

    @Query(value = "SELECT t FROM Task t where t.isOpen = TRUE")
    fun findAllOpenTasks(): List<Task>

    @Query(value = "SELECT t FROM Task t where t.isOpen = FALSE")
    fun findAllClosedTasks(): List<Task>

    @Query(value = "SELECT CASE WHEN COUNT(t > 0) THEN TRUE ELSE FALSE END FROM Task t WHERE t.description =: description")
    fun descriptionIsNotUnique(@Param("description") description: String): Boolean
}