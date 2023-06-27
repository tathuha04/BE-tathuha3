package com.example.demo.service.category;

import com.example.demo.model.Category;
import com.example.demo.service.IGenericService;

import java.util.Optional;

public interface ICategoryService extends IGenericService<Category> {
    Boolean existsByName(String name);

}
