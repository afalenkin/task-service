package com.afalenkin.taskservice.service

import com.afalenkin.taskservice.dto.TaskCreateRequest
import com.afalenkin.taskservice.dto.TaskUpdateRequest
import com.afalenkin.taskservice.exceptions.TaskNotFoundException
import com.afalenkin.taskservice.model.Priority
import com.afalenkin.taskservice.model.Task
import com.afalenkin.taskservice.repository.TaskRepository
import com.afalenkin.taskservice.testdata.createClosedTask
import com.afalenkin.taskservice.testdata.createDtoForTask
import com.afalenkin.taskservice.testdata.createOpenTask
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        val expectedList = listOf(createOpenTask(1), createOpenTask(2))
        every { mockRepo.findAll() } returns expectedList
        val actualList = service.getAll()
        assertThat(actualList.size).isEqualTo(expectedList.size)
    }

    @Test
    fun `create task`() {
        every { mockRepo.save(any()) } returns createOpenTask(1)
        every { mockRepo.descriptionIsNotUnique(any()) } returns false
        val actual = service.create(TaskCreateRequest("tst", true, true, Priority.HIGH))
        assertThat(actual).isEqualTo(createDtoForTask(1))
    }

    @Test
    fun `get All Open tasks`() {
        val expectedList = listOf(createOpenTask(1), createOpenTask(2))
        every { mockRepo.findAllOpenTasks() } returns expectedList
        val actualList = service.getAllOpen()
        assertThat(actualList.size).isEqualTo(expectedList.size)
    }

    @Test
    fun `get All Closed tasks`() {
        val expectedList = listOf(createClosedTask(1), createClosedTask(2))
        every { mockRepo.findAllClosedTasks() } returns expectedList
        val actualList = service.getAllClosed()
        assertThat(actualList.size).isEqualTo(expectedList.size)
    }

    @Test
    fun `update task`() {
        val taskSlot = slot<Task>()
        every { mockRepo.existsById(any()) } returns true
        every { mockRepo.save(capture(taskSlot)) } returns createOpenTask(1)
        every { mockRepo.descriptionIsNotUnique(any()) } returns false
        val actual = service.update(1L, TaskUpdateRequest("tst", true, true, Priority.HIGH))
        assertThat(actual).isEqualTo(createDtoForTask(1))
        verify { mockRepo.save(capture(taskSlot)) }
    }

    @Test
    fun `update not foundtask`() {
        every { mockRepo.existsById(any()) } returns false
        val ex = assertThrows<TaskNotFoundException> {
            service.update(
                1L,
                TaskUpdateRequest("tst", true, true, Priority.HIGH)
            )
        }
        assertThat(ex.message).isEqualTo("Task with id = 1 is not exists")
        verify { mockRepo.save(any()) wasNot called }
    }

    @Test
    fun `delete task`() {
        val idSlot = slot<Long>()
        every { mockRepo.existsById(any()) } returns true
        every { mockRepo.deleteById(capture(idSlot)) } returns Unit
        service.delete(1L)
        verify { mockRepo.deleteById(capture(idSlot)) }

        assertThat(idSlot.captured).isEqualTo(1L)
    }
}