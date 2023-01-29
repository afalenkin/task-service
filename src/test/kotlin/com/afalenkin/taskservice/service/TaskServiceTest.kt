package com.afalenkin.taskservice.service

import com.afalenkin.taskservice.dto.TaskCreateRequest
import com.afalenkin.taskservice.model.Priority
import com.afalenkin.taskservice.model.Task
import com.afalenkin.taskservice.repository.TaskRepository
import com.afalenkin.taskservice.testdata.createDtoForTask
import com.afalenkin.taskservice.testdata.createTask
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@ExtendWith(MockKExtension::class)
internal class TaskServiceTest {

    @RelaxedMockK
    private lateinit var mockRepo: TaskRepository

    @InjectMockKs
    private lateinit var service: TaskService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `get By Id task`() {
    }

    @Test
    fun `get All tasks`() {
        val expectedList = listOf(createTask(1), createTask(2))
        every { mockRepo.findAll() } returns expectedList
        val actualList = service.getAll()
        assertThat(actualList.size).isEqualTo(expectedList.size)
    }

    @Test
    fun `create task`() {
        every { mockRepo.save(any()) } returns createTask(1)
        every { mockRepo.descriptionIsNotUnique(any()) } returns false
        val actual = service.create(TaskCreateRequest("tst", true, true, Priority.HIGH))
        assertThat(actual).isEqualTo(createDtoForTask(1))
    }

    @Test
    fun `get All Open tasks`() {
    }

    @Test
    fun `get All Closed tasks`() {
    }

    @Test
    fun `update task`() {
    }

    @Test
    fun `delete task`() {
    }
}