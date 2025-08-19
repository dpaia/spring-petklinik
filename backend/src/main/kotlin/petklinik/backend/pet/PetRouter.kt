package petklinik.backend.pet

import org.springframework.http.MediaType
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.function.router
import petklinik.backend.owner.OwnerRepository
import petklinik.backend.owner.toDto
import petklinik.backend.visit.Visit
import petklinik.backend.visit.VisitManagement
import petklinik.backend.visit.VisitRepository
import petklinik.common.pet.renderPetForm
import petklinik.common.visit.renderVisitForm
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun petRouter(
    ownerRepository: OwnerRepository,
    petRepository: PetRepository,
    visitRepository: VisitRepository,
    petManagement: PetManagement,
    visitManagement: VisitManagement,
) = router {

    GET("/owners/{id}/pets/new") {
        val id = it.pathVariable("id").toInt()
        val owner = ownerRepository.findById(id)
        ok().contentType(MediaType.TEXT_HTML).body(
            renderPetForm(
                owner
                    .toDto(petRepository, visitRepository), petRepository.findPetTypes().map { it.toDto() })
        )
    }

    POST("/owners/{id}/pets/new") {
        val id = it.pathVariable("id").toInt()
        val pet = Pet(
            it.paramOrNull("name")!!,
            LocalDate.parse(it.paramOrNull("birthDate")!!, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            it.paramOrNull("typeId")!!.toInt(),
            id
        )
        val imagePrompt = it.paramOrNull("imagePrompt")
        petManagement.create(pet, imagePrompt)
        seeOther(URI("/owners/$id/detail")).build()
    }

    GET("/owners/{ownerId}/pets/{petId}/visits/new") {
        val ownerId = it.pathVariable("ownerId").toInt()
        val petId = it.pathVariable("petId").toInt()
        val owner = ownerRepository.findById(ownerId)
        val pet = petRepository.findById(petId)
        ok().contentType(MediaType.TEXT_HTML).body(
            renderVisitForm(owner.toDto(petRepository, visitRepository), pet.toDto(petRepository, visitRepository))
        )
    }

    POST("/owners/{ownerId}/pets/{petId}/visits/new") {
        val ownerId = it.pathVariable("ownerId").toInt()
        val petId = it.pathVariable("petId").toInt()
        val visitDate = LocalDate.parse(it.paramOrNull("visitDate")!!, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val visit = Visit(visitDate, it.paramOrNull("description")!!, petId)
        visitManagement.create(visit)
        seeOther(URI("/owners/$ownerId/detail")).build()
    }

    GET("/pets/images/{id}") {
        val id = it.pathVariable("id").toInt()
        ok().contentType(MediaType.IMAGE_PNG).body(petManagement.findImage(id).image)
    }
}