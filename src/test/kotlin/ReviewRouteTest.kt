import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

import kotlin.test.assertEquals

internal class ReviewRouteTest {

    companion object {
        const val CORRECT_ID = 4
        const val INCORRECT_ID = 101
    }

    @Test
    fun testGetReviewByIdSuccess() = testApplication {

        val response = client.get("$API_ROUTE/review/$CORRECT_ID")
        assertEquals(HttpStatusCode.OK, response.status)

    }

    @Test
    fun testGetReviewByIdFailed() = testApplication {

        val response = client.get("$API_ROUTE/review/$INCORRECT_ID")
        assertEquals(HttpStatusCode.Accepted, response.status)
    }

}