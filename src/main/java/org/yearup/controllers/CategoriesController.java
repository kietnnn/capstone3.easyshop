package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryDao categoryDao;

    // Inject CategoryDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao)
    {
        this.categoryDao = categoryDao;
    }

    // ---------------------------------------
    // GET http://localhost:8080/categories
    // ---------------------------------------
    @GetMapping
    public List<Category> getAll()
    {
        return categoryDao.getAllCategories();
    }

    // ---------------------------------------
    // GET http://localhost:8080/categories/1
    // ---------------------------------------
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        Category category = categoryDao.getById(id);

        if (category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return category;
    }

    // ---------------------------------------
    // POST http://localhost:8080/categories
    // ADMIN only
    // ---------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category category)
    {
        return categoryDao.create(category);
    }

    // ---------------------------------------
    // PUT http://localhost:8080/categories/1
    // ADMIN only
    // ---------------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody Category category)
    {
        category.setCategoryId(id);
        categoryDao.update(id, category);
    }

    // ---------------------------------------
    // DELETE http://localhost:8080/categories/1
    // ADMIN only
    // ---------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id)
    {
        categoryDao.delete(id);
    }
}
