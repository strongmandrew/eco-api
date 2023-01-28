import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertTrue

internal class ReviewRouteTest {

    companion object {
        const val CORRECT_ID = 2
        const val INCORRECT_ID = 101
    }

    @Test
    fun testGetReviewByIdSuccess() = testApplication {

        val response = client.get("/review/$CORRECT_ID")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }

    }

    @Test
    fun testGetReviewByIdFailed() = testApplication {

        val response = client.get("/review/$INCORRECT_ID")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }

}