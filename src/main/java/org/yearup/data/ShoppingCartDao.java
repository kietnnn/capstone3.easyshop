package org.yearup.data;

import org.yearup.models.ShoppingCart;

/**
 * ShoppingCartDao defines all actions related to a user's shopping cart.
 * Each method is user-specific; no cart IDs are exposed in the controller.
 */
public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    void addProduct(int userId, int productId);

    void updateQuantity(int userId, int productId, int quantity);

    void clearCart(int userId);
}