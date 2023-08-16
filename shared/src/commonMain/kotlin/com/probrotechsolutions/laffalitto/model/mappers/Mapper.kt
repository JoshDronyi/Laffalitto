package com.probrotechsolutions.laffalitto.model.mappers

interface Mapper<DTO, ENTITY> {
    operator fun invoke(dto: DTO): ENTITY

}