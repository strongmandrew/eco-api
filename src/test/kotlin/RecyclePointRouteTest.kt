
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue
internal class RecyclePointRouteTest {

    companion object {
        private const val CORRECT_ID = 1
        private const val INCORRECT_ID = 99
    }

    @Test
    fun getRecyclePointsSuccess() = testApplication {

        val response = client.get("recycle_point/$CORRECT_ID")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }

    }

    @Test
    fun getRecyclePointsFailed() = testApplication {
        val response = client.get("/recycle_point/$INCORRECT_ID")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }

    @Test
    fun setRecyclePointPhoto() = testApplication {
        val imageSent = File("C:\\Users\\vad30\\Downloads\\police_dept_blur.jpg")

        val boundary = "WebAppBoundary"
        val response = client.patch("/recycle_point/$CORRECT_ID/photo") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", imageSent.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/jpg")
                            append(HttpHeaders.ContentDisposition, "filename=\"test_img.jpg\"")
                        })
                    },
                    boundary,
                )
            )
        }

        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }

    @Test
    fun changeRecyclePointApproval() = testApplication {

        val response = client.patch("/recycle_point/$CORRECT_ID/approve")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }

    @Test
    fun deleteRecyclePointById() = testApplication {

        val response = client.delete("/recycle_point/$CORRECT_ID")
        assertTrue { response.status in listOf(HttpStatusCode.OK, HttpStatusCode.Accepted) }
    }

}