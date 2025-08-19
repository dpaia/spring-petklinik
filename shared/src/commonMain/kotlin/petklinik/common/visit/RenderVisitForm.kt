package petklinik.common.visit

import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import petklinik.common.Menu
import petklinik.common.fragment.dateInputField
import petklinik.common.fragment.textInputField
import petklinik.common.owner.OwnerDto
import petklinik.common.pet.PetDto
import petklinik.common.renderLayout

fun renderVisitForm(owner: OwnerDto, pet: PetDto) = renderLayout(Menu.OWNERS) {
    h2 { +"New Visit" }

    // Form to add a new visit
    form(classes = "form-horizontal", method = FormMethod.post) {
        id = "visit-form"

        div(classes = "form-group has-feedback") {
            div(classes = "form-group") {
                label(classes = "col-sm-2 control-label") { +"Owner" }
                div("col-sm-10") { +"${owner.firstName} ${owner.lastName}" }
            }
            div(classes = "form-group") {
                label(classes = "col-sm-2 control-label") { +"Pet" }
                div("col-sm-10") { +pet.name }
            }
            dateInputField("Visit Date", "visitDate")
            textInputField("Description", "description")
        }

        div(classes = "form-group") {
            div(classes = "col-sm-offset-2 col-sm-10") {
                button(classes = "btn btn-default", type = ButtonType.submit) {
                    id = "add-visit-button"
                    +"Add Visit"
                }
                +" "
                a(href = "/owners/${owner.id}/detail", classes = "btn btn-default") { +"Back" }
            }
        }
    }

    // Previous visits table
    div(classes = "row") {
        div(classes = "col-sm-12") {
            table(classes = "table table-striped") {
                thead {
                    tr {
                        th { +"Visit Date" }
                        th { +"Description" }
                    }
                }
                for (visit in pet.visits ?: emptyList()) {
                    tr {
                        td { +visit.visitDate }
                        td { +visit.description }
                    }
                }
            }
        }
    }
}
