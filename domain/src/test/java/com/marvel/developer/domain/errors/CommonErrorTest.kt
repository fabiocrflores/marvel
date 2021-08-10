package com.marvel.developer.domain.errors

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CommonErrorTest {

    @Test
    fun `should be no results found`() {
        val error: CommonError = CommonError.NoResultsFound
        val message = "No results found"

        assertThat(error.toString()).isEqualTo(message)
    }
}