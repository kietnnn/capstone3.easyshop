package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(
            ShoppingCartDao shoppingCartDao,
            UserDao userDao,
            ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // GET /cart
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return shoppingCartDao.getByUserId(user.getId());
    }

    // POST /cart/products/{productId}
    @PostMapping("/products/{productId}")
    public void addProduct(
            @PathVariable int productId,
            Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());

        if (productDao.getById(productId) == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        shoppingCartDao.addProduct(user.getId(), productId, 1);
    }

    // PUT /cart/products/{productId}
    @PutMapping("/products/{productId}")
    public void updateProduct(
            @PathVariable int productId,
            @RequestBody ShoppingCartItem item,
            Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());

        if (item.getQuantity() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
        }

        shoppingCartDao.updateProduct(
                user.getId(),
                productId,
                item.getQuantity()
        );
    }

    // DELETE /cart
    @DeleteMapping
    public void clearCart(Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.clearCart(user.getId());
    }
}