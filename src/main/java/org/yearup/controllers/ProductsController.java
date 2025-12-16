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
@CrossOrigin
public class ProductsController
{
    private final ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    // GET /products
    @GetMapping
    public List<Product> search(
            @RequestParam(name = "cat", required = false) Integer categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "subCategory", required = false) String subCategory)
    {
        return productDao.search(categoryId, minPrice, maxPrice, subCategory);
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public Product getById(@PathVariable int id)
    {
        Product product = productDao.getById(id);
        if (product == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return product;
    }

    // POST /products (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product)
    {
        return productDao.create(product);
    }

    // PUT /products/{id} (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        product.setProductId(id);     // 🔑 critical fix
        productDao.update(id, product); // 🔑 critical fix
    }

    // DELETE /products/{id} (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id)
    {
        Product product = productDao.getById(id);
        if (product == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        productDao.delete(id);
    }
}
