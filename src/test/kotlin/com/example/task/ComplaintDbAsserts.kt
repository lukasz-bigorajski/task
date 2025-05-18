package com.example.task

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.springframework.jdbc.core.ColumnMapRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class ComplaintDbAsserts(dataSource: DataSource) {
    private val jdbcTemplate = JdbcTemplate(dataSource, false)

    fun assertThatComplaint(reporterId: String, productId: String): ComplaintAsserts =
        ComplaintAsserts(fetchComplaint(reporterId, productId))

    private fun fetchComplaint(reporterId: String, productId: String): Map<String, Any>? =
        jdbcTemplate.queryForObject(
            "select * from complaint where reporter_id = ? and product_id = ?",
            ColumnMapRowMapper(),
            reporterId,
            productId
        )
}

class ComplaintAsserts(actual: Map<String, Any>?) :
    AbstractAssert<ComplaintAsserts, Map<String, Any>>(actual, ComplaintAsserts::class.java) {

    fun hasCounter(value: Int): ComplaintAsserts {
        assertThat(actual).hasEntrySatisfying("creation_counter") { v -> assertThat(v).isEqualTo(value) }
        return this
    }

    fun hasContent(value: String): ComplaintAsserts {
        assertThat(actual).hasEntrySatisfying("content") { v -> assertThat(v).isEqualTo(value) }
        return this
    }

    fun hasCountry(value: String?): ComplaintAsserts {
        assertThat(actual).hasEntrySatisfying("country_code") { v -> assertThat(v).isEqualTo(value) }
        return this
    }
}
