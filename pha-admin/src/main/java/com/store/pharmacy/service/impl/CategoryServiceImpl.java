package com.store.pharmacy.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.pharmacy.model.Category;
import com.store.pharmacy.model.CategoryDTO;
import com.store.pharmacy.exception.DataNotFoundException;
import com.store.pharmacy.exception.DuplicateDataException;

import com.store.pharmacy.repository.CategoryRepository;
import com.store.pharmacy.service.CategoryService;
import com.store.pharmacy.utils.ExecContext;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Value("${app.timeZone}")
	private String timeZone;

	@Autowired
	private ExecContext execContext;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public String save(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setCategoryId(categoryDTO.getCategoryId().trim());
		if (categoryDTO.getDescription() != null && !categoryDTO.getDescription().trim().isEmpty()) {
			category.setDescription(categoryDTO.getDescription().trim());
		}
		category.setCategoryName(categoryDTO.getCategoryName());
		category.setCreateAt(LocalDateTime.now(ZoneId.of(timeZone)));
		if (execContext.getUserId() != null && !execContext.getUserId().isEmpty()) {
			category.setCreateBy(execContext.getUserId());
		}
		if (categoryDTO.getEnabled() != null && !categoryDTO.getEnabled()) {
			category.setEnabled(categoryDTO.getEnabled());
		} else {
			category.setEnabled(true);
			categoryDTO.setEnabled(true);
		}
		categoryRepository.save(category);
		return categoryDTO.getCategoryId();
	}

	@Override
	public CategoryDTO findCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null) {
			throw new DataNotFoundException("no.category.exists", new Object[] { categoryId });
		}
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(categoryId);
		categoryDTO.setEnabled(category.isEnabled());
		categoryDTO.setDescription(category.getDescription());
		categoryDTO.setCategoryName(category.getCategoryName());
		return categoryDTO;
	}

	@Override
	public void update(String categoryId, CategoryDTO categoryDTO) {
		Category category = categoryRepository.findCategoryByCategoryId(categoryId);
		if (categoryDTO.getCategoryName() != null) {
			category.setCategoryName(categoryDTO.getCategoryName());
		} else {
			categoryDTO.setCategoryName(category.getCategoryName());
		}
		if (categoryDTO.getDescription() != null) {
			category.setDescription(categoryDTO.getDescription());
		} else {
			categoryDTO.setDescription(category.getDescription());
		}
		if (categoryDTO.getEnabled() != null) {
			category.setEnabled(categoryDTO.getEnabled());
		} else {
			categoryDTO.setEnabled(category.isEnabled());
		}
		if (execContext.getUserId() != null) {
			category.setUpdateBy(execContext.getUserId());
		}
		categoryDTO.setCategoryId(category.getCategoryId());
		category.setUpdateAt(LocalDateTime.now(ZoneId.of(timeZone)));
		categoryRepository.save(category);
	}

	@Override
	public void checkIfDuplicatedCategory(String categoryId) {
		if (categoryRepository.findCategoryByCategoryId(categoryId) != null) {
			throw new DuplicateDataException("categoryId.duplicate", new Object[] { categoryId });
		}
	}

	@Override
	public void checkIfCategoryExits(String categoryId) {
		if (categoryRepository.findCategoryByCategoryId(categoryId) == null) {
			throw new DataNotFoundException("no.category.exists", new Object[] { categoryId });
		}
	}
}