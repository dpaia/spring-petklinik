package petklinik.backend

import jakarta.servlet.RequestDispatcher
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ErrorHandlingTests {

    private val mockMvc: MockMvc = MockMvcBuilders.routerFunctions(globalRouter()).build()

    @Test
    fun `oups endpoint should render custom error page with 500 and exception message`() {
        mockMvc.get("/oups").andExpect {
            status { isInternalServerError() }
            content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            content { string(org.hamcrest.Matchers.containsString("Runtime Error")) }
            content { string(org.hamcrest.Matchers.containsString("alert alert-danger")) }
            content {
                string(
                        org.hamcrest.Matchers.containsString(
                                "Expected: controller used to showcase what happens when an exception is thrown"
                        )
                )
            }
            // Verify it includes full UI layout
            content { string(org.hamcrest.Matchers.containsString("navbar navbar-default")) }
            content { string(org.hamcrest.Matchers.containsString("Return to Home")) }
        }
    }

    @Test
    fun `error endpoint should use provided status code and render full UI`() {
        mockMvc
                .get("/error") {
                    requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
                    requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/nonexistent")
                }
                .andExpect {
                    status { isNotFound() }
                    content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
                    // Should include enhanced 404 error message
                    content { string(org.hamcrest.Matchers.containsString("Page Not Found")) }
                    content {
                        string(
                                org.hamcrest.Matchers.containsString(
                                        "'/nonexistent' could not be found"
                                )
                        )
                    }
                    // Basic layout elements to ensure full UI shell
                    content {
                        string(org.hamcrest.Matchers.containsString("navbar navbar-default"))
                    }
                    content { string(org.hamcrest.Matchers.containsString("jumbotron")) }
                    content { string(org.hamcrest.Matchers.containsString("Return to Home")) }
                }
    }

    @Test
    fun `error endpoint should handle 500 errors with proper message`() {
        mockMvc.get("/error") { requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500) }.andExpect {
            status { isInternalServerError() }
            content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            content { string(org.hamcrest.Matchers.containsString("Internal Server Error")) }
            content { string(org.hamcrest.Matchers.containsString("navbar navbar-default")) }
            content { string(org.hamcrest.Matchers.containsString("jumbotron")) }
        }
    }

    @Test
    fun `error endpoint should handle 403 errors with proper message`() {
        mockMvc.get("/error") { requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403) }.andExpect {
            status { isForbidden() }
            content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            content { string(org.hamcrest.Matchers.containsString("Access Forbidden")) }
            content { string(org.hamcrest.Matchers.containsString("don't have permission")) }
            content { string(org.hamcrest.Matchers.containsString("navbar navbar-default")) }
        }
    }
}
