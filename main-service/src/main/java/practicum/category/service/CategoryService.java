package practicum.category.service;


import practicum.category.dto.NewCategoryDto;
import practicum.category.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll(Integer from, Integer size);

    Category get(Long categoryId);

    Category add(NewCategoryDto body);

    void delete(Long catId);

    Category update(Long categoryId, NewCategoryDto newCategoryDto);
}
