package practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import practicum.compilation.dto.CompilationDto;
import practicum.compilation.dto.NewCompilationDto;
import practicum.compilation.model.Compilation;
import practicum.event.mapper.EventMapper;

import java.util.List;

@Mapper(uses = EventMapper.class, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation newCompilationDtoToCompilation(NewCompilationDto compilationDto);

    CompilationDto compilationToCompilationDto(Compilation compilation);

    List<CompilationDto> listCompilationToListCompilationDto(List<Compilation> compilation);
}