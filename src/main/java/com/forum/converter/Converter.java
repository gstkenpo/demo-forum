package com.forum.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Converter<E, D> {

    private final Function <D, E> convertToEntity;
    private final Function <E, D> convertToDto;

    /**
     * @param fromDto
     *            Function that converts given dto entity into the domain
     *            entity.
     * @param fromEntity
     *            Function that converts given domain entity into the dto
     *            entity.
     */
    public Converter(final Function <D, E> convertToEntity, final Function <E, D> convertToDto) {
        this.convertToEntity = convertToEntity;
        this.convertToDto = convertToDto;
    }

	/**
     * @param customerDto
     *            DTO entity
     * @return The domain representation - the result of the converting function
     *         application on dto entity.
     */
    public final E convertFromDto(final D dto) {
        return convertToEntity.apply(dto);
    }

    /**
     * @param customer
     *            domain entity
     * @return The DTO representation - the result of the converting function
     *         application on domain entity.
     */
    public final D convertFromEntity(final E entity) {
        return convertToDto.apply(entity);
    }

    /**
     * @param dtoCustomers
     *            collection of DTO entities
     * @return List of domain representation of provided entities retrieved by
     *         mapping each of them with the conversion function
     */
    public final List <E> createFromDtos(final Collection < D > dtos) {
        return dtos.stream().map(this::convertFromDto).collect(Collectors.toList());
    }

    /**
     * @param customers
     *            collection of domain entities
     * @return List of domain representation of provided entities retrieved by
     *         mapping each of them with the conversion function
     */
    public final List < D > createFromEntities(final Collection < E > entities) {
        return entities.stream().map(this::convertFromEntity).collect(Collectors.toList());
    }

}