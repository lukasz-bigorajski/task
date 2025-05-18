package com.example.task

import com.example.task.utils.LoggerExtension.getLogger
import org.junit.jupiter.api.Test

class TaskApplicationTests: BaseSpringUnitTest() {

	@Test
	fun contextLoads() {
		log.info("Spring started successfully")
	}

	companion object {
		private val log = this.getLogger()
	}
}
