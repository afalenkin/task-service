package com.afalenkin.taskservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 *@author Alenkin Andrew
 *oxqq@ya.ru
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(override val message: String?) : RuntimeException()