package com.afalenkin.taskservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@Entity
@Table(
    name = "task",
    uniqueConstraints = [UniqueConstraint(name = "uk_task_description", columnNames = ["description"])]
)
data class Task(
    @Id
    @GeneratedValue(generator = "task_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_sequence", allocationSize = 1)
    val id: Long? = null,

    @NotBlank
    @Column(name = "description", nullable = false, unique = true)
    var description: String,

    @Column(name = "has_reminder", nullable = false)
    var hasReminder: Boolean,

    @Column(name = "is_open", nullable = false)
    var isOpen: Boolean,

    @Column(name = "created", nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    var priority: Priority,
)