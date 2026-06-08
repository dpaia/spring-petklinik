package petklinik.common

import kotlinx.html.*

/**
 * Renders a full UI page with an error message inside the common layout. This custom error page
 */
fun renderError(title: String = "Oops! Something went wrong", message: String? = null) =
        renderLayout(Menu.ERROR) {
            div(classes = "jumbotron") {
                div(classes = "container") {
                    h2(classes = "display-4") { +title }
                    p(classes = "lead") {
                        +"An unexpected error occurred. Please try again or contact support if the problem persists."
                    }
                    hr(classes = "my-4")
                    if (!message.isNullOrBlank()) {
                        h4 { +"Error Details:" }
                        div(classes = "alert alert-danger") {
                            attributes["role"] = "alert"
                            p { +message }
                        }
                    } else {
                        div(classes = "alert alert-warning") {
                            attributes["role"] = "alert"
                            p { +"No specific error details are available." }
                        }
                    }
                    div(classes = "mt-4") {
                        a(classes = "btn btn-primary btn-lg", href = "/") {
                            attributes["role"] = "button"
                            +"Return to Home"
                        }
                        a(
                                classes = "btn btn-secondary btn-lg ml-2",
                                href = "javascript:history.back()"
                        ) {
                            attributes["role"] = "button"
                            +"Go Back"
                        }
                    }
                }
            }

            div(classes = "container mt-4") {
                div(classes = "row") {
                    div(classes = "col-md-6") {
                        h3 { +"What can you do?" }
                        ul {
                            li { +"Try refreshing the page" }
                            li { +"Check if the URL is correct" }
                            li { +"Return to the home page and try again" }
                            li { +"Contact support if the problem persists" }
                        }
                    }
                }
            }
        }
