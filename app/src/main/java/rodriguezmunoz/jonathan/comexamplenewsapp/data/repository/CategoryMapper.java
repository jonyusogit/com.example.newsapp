package rodriguezmunoz.jonathan.comexamplenewsapp.data.repository;

import java.util.ArrayList;
import java.util.List;

import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.Category;
import rodriguezmunoz.jonathan.comexamplenewsapp.data.model.CategoryEntity;

public class CategoryMapper {

    public static CategoryEntity toEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setSlug(category.getSlug());
        entity.setCount(category.getCount());
        return entity;
    }

    public static List<CategoryEntity> toEntityList(List<Category> categories) {
        List<CategoryEntity> entities = new ArrayList<>();
        for (Category c : categories) entities.add(toEntity(c));
        return entities;
    }

    public static Category fromEntity(CategoryEntity entity) {
        Category category = new Category();
        return category;
    }

    public static List<Category> fromEntityList(List<CategoryEntity> entities) {
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity e : entities) categories.add(fromEntity(e));
        return categories;
    }
}