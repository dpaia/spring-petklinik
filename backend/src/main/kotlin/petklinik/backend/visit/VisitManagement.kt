package petklinik.backend.visit

import org.springframework.transaction.annotation.Transactional
import petklinik.common.visit.validateVisit

@Transactional
class VisitManagement(
    private val visitRepository: VisitRepository,
) {
    fun create(visit: Visit): Visit {
        val visitDto = visit.toDto()
        val validation = validateVisit(visitDto)
        if (!validation.isValid) {
            throw IllegalStateException()
        }
        return visitRepository.save(visit)
    }
}