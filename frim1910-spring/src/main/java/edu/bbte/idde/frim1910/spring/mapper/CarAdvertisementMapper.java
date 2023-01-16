package edu.bbte.idde.frim1910.spring.mapper;

import edu.bbte.idde.frim1910.spring.dto.incoming.CarAdvertisementCreationDto;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarAdvertisementUpdateDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarAdvertisementDetailedDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarAdvertisementReducedDto;
import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ByteArrayMapper.class, DateMapper.class})
public abstract class CarAdvertisementMapper {
    @Mapping(target = "id", ignore = true)
    public abstract CarAdvertisement dtoToModel(CarAdvertisementCreationDto carAdvertisementDto, Car car);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract CarAdvertisement updateModelByDto(
            CarAdvertisementUpdateDto dto,
            @MappingTarget CarAdvertisement model
    );

    @IterableMapping(elementTargetType = CarAdvertisementReducedDto.class)
    public abstract Collection<CarAdvertisementReducedDto> modelsToReducedDtos(Iterable<CarAdvertisement> model);

    public abstract CarAdvertisementDetailedDto modelToDetailedDto(CarAdvertisement carAdvertisement);
}
