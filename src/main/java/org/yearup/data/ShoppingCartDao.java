package org.yearup.data;

import org.yearup.models.ShoppingCart;

/**
 * ShoppingCartDao defines all actions related to a user's shopping cart.
 * Each method is user-specific; no cart IDs are exposed in the controller.
 */
public interface ShoppingCartDao
{
    /**
     * Retrieve the shopping cart for a specific user.
     *
     * @param userId the user's ID
     * @return the user's ShoppingCart
     */
    ShoppingCart getByUserId(int userId);

    /**
     * Add a product to the user's shopping cart.
     * If the product already exists in the cart, increase the quantity.
     *
     * @param userId the user's ID
     * @param productId the product ID to add
     * @param quantity the quantity to add
     */
    void addProduct(int userId, int productId, int quantity);

    /**
     * Update the quantity of a specific product in the user's cart.
     *
     * @param userId the user's ID
     * @param productId the product ID to update
     * @param quantity the new quantity
     */
    void updateProduct(int userId, int productId, int quantity);

    /**
     * Clear all products from the user's shopping cart.
     *
     * @param userId the user's ID
     */
    void clearCart(int userId);
}