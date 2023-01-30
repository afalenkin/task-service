package com.afalenkin.taskservice.controller

import com.afalenkin.taskservice.dto.TaskCreateRequest
import com.afalenkin.taskservice.dto.TaskUpdateRequest
import com.afalenkin.taskservice.exceptions.TaskNotFoundException
import com.afalenkin.taskservice.model.Priority
import com.afalenkin.taskservice.service.TaskService
import com.afalenkin.taskservice.testdata.createDtoForTask
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

private const val BASE_PATH = "/api/v1/tasks"

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TaskController::class])
internal class TaskControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var service: TaskService

    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        mapper.registerModule(JavaTimeModule())
    }

    @Test
    fun `get All Tasks`() {
        val expected = listOf(createDtoForTask(1), createDtoForTask(2))
        `when`(service.getAll()).thenReturn(expected)
        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.get(BASE_PATH)
        )

        result.andExpect(status().`is`(200))
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(jsonPath("$.size()").value(2))
    }

    @Test
    fun `get By Id not found`() {
        val id = 1L
        `when`(service.getById(id)).thenThrow(TaskNotFoundException("Missing task with id = $id"))
        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.get("$BASE_PATH/$id")
        )
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `get By malformed Id is bad request`() {
        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.get("$BASE_PATH/3L")
        )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `delete By Id`() {
        val id = 1L
        doNothing().`when`(service).checkById(id)
        doNothing().`when`(service).delete(id)

        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.delete("$BASE_PATH/$id")
        )
        result.andExpect(status().isOk)
    }

    @Test
    fun `create`() {
        val createRequest = TaskCreateRequest("tst", true, true, Priority.HIGH)
        val expected = createDtoForTask(1)
        `when`(service.create(createRequest)).thenReturn(expected)

        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.post("$BASE_PATH")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest))
        )

        result.andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.description").value(expected.description))
            .andExpect(jsonPath("$.id").value(expected.id))
            .andExpect(jsonPath("$.hasReminder").value(expected.hasReminder))
            .andExpect(jsonPath("$.isOpen").value(expected.isOpen))
            .andExpect(jsonPath("$.priority").value(expected.priority.name))
    }

    @Test
    fun `update`() {
        val updateRequest = TaskUpdateRequest("tst", true, true, Priority.HIGH)
        val id = 1L
        val expected = createDtoForTask(1)
        `when`(service.update(id, updateRequest)).thenReturn(expected)
        val result: ResultActions = mvc.perform(
            MockMvcRequestBuilders.put("$BASE_PATH/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
        )

        result.andExpect(status().isOk)
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        result.andExpect(jsonPath("$.description").value(expected.description))
        result.andExpect(jsonPath("$.id").value(expected.id))
        result.andExpect(jsonPath("$.hasReminder").value(expected.hasReminder))
        result.andExpect(jsonPath("$.isOpen").value(expected.isOpen))
        result.andExpect(jsonPath("$.priority").value(expected.priority.name))
    }
}