package petklinik.common.visit

import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

val validateVisit = Validation { VisitDto::description { notBlank() } }