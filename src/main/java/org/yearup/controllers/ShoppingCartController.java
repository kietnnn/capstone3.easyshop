package org.yearup.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;


import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;

    public ShoppingCartController(
            ShoppingCartDao shoppingCartDao,
            UserDao userDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        int userId = userDao.getIdByUsername(principal.getName());
        return shoppingCartDao.getByUserId(userId);
    }

    @PostMapping("/{productId}")
    public void addProduct(
            @PathVariable int productId,
            Principal principal)
    {
        int userId = userDao.getIdByUsername(principal.getName());
        shoppingCartDao.addProduct(userId, productId);
    }

    @DeleteMapping
    public void clearCart(Principal principal)
    {
        int userId = userDao.getIdByUsername(principal.getName());
        shoppingCartDao.clearCart(userId);
    }
}