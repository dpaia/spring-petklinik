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
        val msg = code?.let { "HTTP $it: " + HttpStatus.valueOf(it).reasonPhrase + " " }
        ServerResponse.status(code?.let { HttpStatus.valueOf(it) } ?: HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_HTML)
            .body(renderError(message = msg))
    }

    // Demo endpoint that triggers an exception
    GET("/oups") {
        throw RuntimeException("Expected: controller used to showcase what happens when an exception is thrown")
    }

    // Global error handler to render the full UI with error content
    onError<Throwable> { exception, request ->
        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_HTML)
            .body(renderError(message = exception.message))
    }
}