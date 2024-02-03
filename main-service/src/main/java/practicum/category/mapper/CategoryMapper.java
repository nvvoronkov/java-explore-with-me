package practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import practicum.category.dto.CategoryDto;
import practicum.category.dto.NewCategoryDto;
import practicum.category.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryToCategoryDto(Category category);

    List<CategoryDto> listCategoryToListCategoryDto(List<Category> category);

    @Mapping(target = "id", ignore = true)
    Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto);
}