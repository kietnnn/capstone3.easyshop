package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public class MySqlShoppingCartDao implements ShoppingCartDao
{
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate, ProductDao productDao)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
            SELECT product_id, quantity
            FROM shopping_cart
            WHERE user_id = ?
        """;

        List<?> rows = jdbcTemplate.query(sql, (rs, rowNum) -> {
            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");

            Product product = productDao.getById(productId);

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(quantity);

            cart.add(item);
            return null;
        }, userId);

        return cart;
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        String existsSql = """
            SELECT COUNT(*)
            FROM shopping_cart
            WHERE user_id = ? AND product_id = ?
        """;

        Integer count = jdbcTemplate.queryForObject(
                existsSql, Integer.class, userId, productId
        );

        if (count != null && count > 0)
        {
            String updateSql = """
                UPDATE shopping_cart
                SET quantity = quantity + 1
                WHERE user_id = ? AND product_id = ?
            """;

            jdbcTemplate.update(updateSql, userId, productId);
        }
        else
        {
            String insertSql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1)
            """;

            jdbcTemplate.update(insertSql, userId, productId);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}

