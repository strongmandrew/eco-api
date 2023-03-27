import com.example.routes.Endpoint
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json

val API_ROUTE = Endpoint.API.path

val ApplicationTestBuilder.provideClient: HttpClient
    get() = createClient {
        install(ContentNegotiation) {
            json(Json{
                encodeDefaults = false
                prettyPrint = true
            })
        }
    }