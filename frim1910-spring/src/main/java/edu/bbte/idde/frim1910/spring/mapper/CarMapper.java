package edu.bbte.idde.frim1910.spring.mapper;

import edu.bbte.idde.frim1910.spring.dto.incoming.CarCreationDto;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarUpdateDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarDto;
import edu.bbte.idde.frim1910.spring.model.Car;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ByteArrayMapper.class})
public abstract class CarMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Car creationDtoToModel(CarCreationDto carDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Car updateModelByDto(CarUpdateDto dto, @MappingTarget Car model);

    @IterableMapping(elementTargetType = CarDto.class)
    public abstract Collection<CarDto> modelsToDtos(Iterable<Car> model);

    public abstract CarDto modelToDto(Car car);
}
