package petklinik.common.visit

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import petklinik.common.owner.OwnerDto
import petklinik.common.pet.PetDto
import petklinik.common.pet.PetTypeDto

class VisitTests {

    @Test
    fun validate_ok() {
        val dto = VisitDto(
            visitDate = "2024-01-01",
            description = "regular checkup",
            id = null
        )
        assertTrue(validateVisit(dto).isValid)
    }

    @Test
    fun validate_description_must_not_be_blank() {
        val dto = VisitDto(
            visitDate = "2024-01-01",
            description = "",
            id = null
        )
        assertFalse(validateVisit(dto).isValid)
    }

    @Test
    fun render_visit_form_with_previous_visits() {
        val owner = OwnerDto(
            firstName = "George",
            lastName = "Franklin",
            address = "110 W. Liberty St.",
            city = "Madison",
            telephone = "6085551023",
            pets = emptyList(),
            id = 1
        )
        val pet = PetDto(
            name = "Leo",
            birthDate = "2000-09-07",
            type = PetTypeDto(name = "cat", id = 1),
            visits = listOf(
                VisitDto("2010-03-04", "rabies shot", 1),
                VisitDto("2011-05-07", "annual check", 2)
            ),
            imageId = null,
            id = 1
        )

        val html = renderVisitForm(owner, pet)

        // Headers
        assertContains(html, ">Visit Date<")
        assertContains(html, ">Description<")

        // Previous visits are listed
        assertContains(html, "<td>2010-03-04</td>")
        assertContains(html, "<td>rabies shot</td>")
        assertContains(html, "<td>2011-05-07</td>")
        assertContains(html, "<td>annual check</td>")

        // Owner and pet names appear
        assertContains(html, "George Franklin")
        assertContains(html, ">Leo<")

        // Form and button identifiers
        assertContains(html, "id=\"visit-form\"")
        assertContains(html, "id=\"add-visit-button\"")
    }
}
