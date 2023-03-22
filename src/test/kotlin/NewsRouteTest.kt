import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class NewsRouteTest {

    @Test
    fun getNews() = testApplication {

        val response = client.get("/news")

        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }
}