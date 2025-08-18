package petklinik.common

import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.p

/**
 * Renders a full UI page with an error message inside the common layout.
 */
fun renderError(title: String = "Something went wrong", message: String? = null) =
    renderLayout(Menu.ERROR) {
        h2 { +title }
        p {
            +"An unexpected error occurred. Please try again or contact support if the problem persists."
            br
            +"Error message:"
        }
        div(classes = "alert alert-danger") {
            p {
                +(message.orEmpty().ifBlank { "No error message provided." })
            }
        }
    }
