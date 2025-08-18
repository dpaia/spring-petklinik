package petklinik.backend

import jakarta.servlet.RequestDispatcher
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class ErrorHandlingTests(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `oups endpoint should render custom error page with 500 and exception message`() {
        mockMvc.get("/oups")
            .andExpect {
                status { isInternalServerError() }
                content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
                content { string(org.hamcrest.Matchers.containsString("Something went wrong")) }
                content { string(org.hamcrest.Matchers.containsString("alert alert-danger")) }
                content { string(org.hamcrest.Matchers.containsString("Expected: controller used to showcase what happens when an exception is thrown")) }
            }
    }

    @Test
    fun `error endpoint should use provided status code and render full UI`() {
        mockMvc.get("/error") {
            requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
        }.andExpect {
            status { isNotFound() }
            content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            // Should include our computed message prefix
            content { string(org.hamcrest.Matchers.containsString("HTTP 404: Not Found")) }
            // Basic layout elements to ensure full UI shell
            content { string(org.hamcrest.Matchers.containsString("navbar navbar-default")) }
        }
    }
}
