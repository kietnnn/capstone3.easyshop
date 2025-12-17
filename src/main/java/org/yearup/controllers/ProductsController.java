package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController
{
    private final ProductDao productDao;

    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    @GetMapping
    public List<Product> search(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String subCategory)
    {
        return productDao.search(categoryId, minPrice, maxPrice, subCategory);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable int id)
    {
        return productDao.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product create(@RequestBody Product product)
    {
        return productDao.create(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Product product)
    {
        productDao.update(id, product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id)
    {
        productDao.delete(id);
    }
}