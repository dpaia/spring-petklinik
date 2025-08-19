package petklinik.frontend

import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLFormElement
import org.w3c.xhr.FormData
import petklinik.common.visit.VisitDto
import petklinik.common.visit.validateVisit

fun addVisitForm() {
    document.getElementById("visit-form")?.addEventListener("submit") { event ->
        val formData = FormData(event.target as HTMLFormElement)
        val visit = VisitDto(
            formData.get("visitDate").toString(),
            formData.get("description").toString()
        )
        val validation = validateVisit(visit)
        if (!validation.isValid) {
            displayErrors(validation)
            event.preventDefault()
        }
        else {
            (document.getElementById("add-visit-button") as HTMLButtonElement).disabled = true
        }
    }
}