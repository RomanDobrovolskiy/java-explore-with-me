package ru.practicum.explore_with_me.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.exception.CategoryNotFoundException;
import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.category.CategoryDto;
import ru.practicum.explore_with_me.model.category.CategoryMapper;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.fromDto(categoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        getCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category cat = getCategory(categoryDto.getId());
        cat.setName(categoryDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(cat));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDto(Long categoryId) {
        return CategoryMapper.toDto(getCategory(categoryId));
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

}
