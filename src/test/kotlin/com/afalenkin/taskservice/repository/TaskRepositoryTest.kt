package com.afalenkin.taskservice.repository

import com.afalenkin.taskservice.model.Priority
import com.afalenkin.taskservice.model.Task
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@DataJpaTest(properties = ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TaskRepositoryTest {
    @Autowired
    private lateinit var repository: TaskRepository

    private val recordsCount = 5
    private val openRecordsCount = 3
    private val closedRecordsCount = 2

    @Test
    @Sql("/sql/tasks-test-data.sql")
    @Sql("/sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    fun `delete task by id test`() {
        repository.deleteById(1)
        assertThat(repository.findById(1)).isEmpty
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `get all tasks test`() {
        val all = repository.findAll()
        assertThat(all.size).isEqualTo(recordsCount)
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `get task by id test`() {
        val task = repository.findById(1)
        assertThat(task).isNotEmpty
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `get closed tasks test`() {
        val all = repository.findAllClosedTasks()
        assertThat(all.size).isEqualTo(closedRecordsCount)
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `get open tasks test`() {
        val all = repository.findAllOpenTasks()
        assertThat(all.size).isEqualTo(openRecordsCount)
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `description non unique test`() {
        assertThat(repository.descriptionIsNotUnique("first task")).isTrue()
    }

    @Test
    @Sql("/sql/tasks-test-data.sql")
    fun `description unique test`() {
        assertThat(repository.descriptionIsNotUnique("dummy task")).isFalse()
    }
}