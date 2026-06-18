package petklinik.backend

import jakarta.servlet.RequestDispatcher
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import petklinik.common.renderError
import petklinik.common.renderWelcome

fun globalRouter() = router {
    GET("/") {
        ok().contentType(MediaType.TEXT_HTML).body(renderWelcome())
    }

    // Explicit error endpoint to replace Spring Boot Whitelabel page and render full UI
    GET("/error") { request ->
        val code = request.servletRequest().getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as? Int
        val exception = request.servletRequest().getAttribute(RequestDispatcher.ERROR_EXCEPTION) as? Throwable
        val requestUri = request.servletRequest().getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String
        
        val title = when (code) {
            404 -> "Page Not Found"
            403 -> "Access Forbidden"
            500 -> "Internal Server Error"
            else -> "Something went wrong"
        }
        
        val message = when (code) {
            404 -> "The page you're looking for at '$requestUri' could not be found. It may have been moved, deleted, or you entered the wrong URL."
            403 -> "You don't have permission to access this resource."
            500 -> exception?.message ?: "An internal server error occurred. Please try again later."
            else -> exception?.message ?: code?.let { "HTTP $it: " + HttpStatus.valueOf(it).reasonPhrase }
        }
        
        ServerResponse.status(code?.let { HttpStatus.valueOf(it) } ?: HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_HTML)
            .body(renderError(title = title, message = message))
    }

    // Demo endpoint that triggers an exception
    GET("/oups") {
        throw RuntimeException("Expected: controller used to showcase what happens when an exception is thrown")
    }

    // Global error handler to render the full UI with error content
    onError<Throwable> { exception, request ->
        val title = when (exception) {
            is IllegalArgumentException -> "Invalid Request"
            is SecurityException -> "Security Error"
            is RuntimeException -> "Runtime Error"
            else -> "Unexpected Error"
        }
        
        val message = exception.message ?: "An unexpected error occurred while processing your request."
        
        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_HTML)
            .body(renderError(title = title, message = message))
    }
}