import com.example.domain.Response
import com.example.entity.UserTakeOff
import com.example.routes.Endpoint
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserTakeOffRouteTest {

    private var testId: Int? = null

    companion object {

        private val testTakeoff = UserTakeOff(
            idUser = 9,
            idRecyclePoint = 19,
            idRubbishType = 1,
            amountInGrams = 300.0
        )
        private val TAKE_OFF = Endpoint.TAKE_OFF.path
    }

    @Test
    @BeforeAll
    @DisplayName("Posting new user take off")
    fun takeOffRubbish() = testApplication {
        assertDoesNotThrow {
            val response = provideClient.post("$API_ROUTE$TAKE_OFF") {
                contentType(ContentType.Application.Json)
                setBody(testTakeoff)
            }

            testId = (response.body() as Response<UserTakeOff>)
                .data!!.id

            assertEquals(
                expected = HttpStatusCode.Created,
                actual = response.status
            )

        }
    }

    @Test
    @DisplayName("Getting user take off entry by id")
    fun getTakeOffById() = testApplication {
            val response =
                provideClient.get("$API_ROUTE$TAKE_OFF/$testId")

            assertEquals(
                expected = HttpStatusCode.Accepted,
                actual = response.status
            )
    }

    @Test
    @AfterAll
    @DisplayName("Deleting user take off by id")
    fun deleteTakeoffRubbish() = testApplication {
        assertDoesNotThrow {
            val response =
                provideClient.delete("$API_ROUTE$TAKE_OFF/$testId")

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
        }
    }
}