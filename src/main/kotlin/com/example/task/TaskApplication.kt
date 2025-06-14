package com.example.task

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TaskApplication

fun main(args: Array<String>) {
	runApplication<TaskApplication>(*args)
}
