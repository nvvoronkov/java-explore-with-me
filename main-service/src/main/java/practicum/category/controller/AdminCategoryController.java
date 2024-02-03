package practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.category.dto.CategoryDto;
import practicum.category.dto.NewCategoryDto;
import practicum.category.mapper.CategoryMapper;
import practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@RequestBody @Valid NewCategoryDto newCategoryDto) {

        log.info("Calling POST: /admin/categories with 'newCategoryDto':{}", newCategoryDto.toString());
        return categoryMapper.categoryToCategoryDto(categoryService.add(newCategoryDto));
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                              @RequestBody @Valid NewCategoryDto newCategoryDto) {

        log.info("Calling PATCH: /admin/categories with 'categoryDto': {}", newCategoryDto.toString());
        return categoryMapper.categoryToCategoryDto(categoryService.update(catId, newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {

        log.info("Calling DELETE: /admin/categories/{catId} with 'categoryId': {}", catId);
        categoryService.delete(catId);
    }
}
