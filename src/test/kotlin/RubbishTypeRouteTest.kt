import com.example.domain.Response
import com.example.entity.RubbishType
import com.example.routes.Endpoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RubbishTypeRouteTest {

    private var incrementId: Int? = null
    companion object {
        val testRubbish = RubbishType(
            type = "Крышечки",
            description = "Крышки от пластиковых бутылок и пр."
        )
        const val testQuery = "крышечки"
        val RUBBISH_ROUTE = Endpoint.RUBBISH_TYPE.path
    }

    private val ApplicationTestBuilder.provideClient: HttpClient
        get() = createClient {
            install(ContentNegotiation) {
                json(Json{
                    encodeDefaults = false
                    prettyPrint = true
                })
            }
        }


    @Test
    @BeforeAll
    @DisplayName("Проверка добавления нового типа мусора")
    fun postRubbishType() = testApplication {
        assertDoesNotThrow {
            val response =
                provideClient.post("$API_ROUTE$RUBBISH_ROUTE") {
                    contentType(Json)
                    setBody(testRubbish)
                }
            incrementId = (response.body() as
                    Response<RubbishType>).data!!.id
            assertEquals(
                expected = HttpStatusCode.Created,
                actual = response.status
            )
        }
    }

    @Test
    @DisplayName("Получение типа мусора по id")
    fun getRubbishTypeById() = testApplication {
        assertDoesNotThrow {
            val response = provideClient
                .get("$API_ROUTE$RUBBISH_ROUTE/$incrementId")

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
        }
    }

    @Test
    @DisplayName("Получение кол-ва всего мусора заданного типа")
    fun getTotalOfRubbishType() = testApplication {
        assertDoesNotThrow {
            val response =
                provideClient
                    .get("$API_ROUTE$RUBBISH_ROUTE/total") {
                        parameter("name", testQuery)
                    }

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
        }
    }

    @Test
    @AfterAll
    @DisplayName("Удаление типа мусора по id")
    fun deleteRubbishType() = testApplication {
        assertDoesNotThrow {
            val response = provideClient
                .delete("$API_ROUTE$RUBBISH_ROUTE/$incrementId")

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
        }
    }
}