package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @Autowired
    public ShoppingCartController(
            ShoppingCartDao shoppingCartDao,
            UserDao userDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    // PUT /cart/products/{productId}
    @PutMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCartItem(
            @PathVariable int productId,
            @RequestBody ShoppingCartItem item,
            Principal principal)
    {
        // 1. Get logged-in user
        User user = userDao.getByUserName(principal.getName());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // 2. Get user's cart
        ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

        // 3. Only update if product already exists in cart
        if (!cart.contains(productId))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product not found in shopping cart"
            );
        }

        // 4. Update quantity in database
        shoppingCartDao.updateQuantity(
                user.getId(),
                productId,
                item.getQuantity()
        );
    }
}
