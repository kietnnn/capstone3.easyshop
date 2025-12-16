package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --------------------------------------------------
    // GET cart by userId
    // --------------------------------------------------
    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = """
            SELECT
                p.product_id,
                p.name,
                p.price,
                p.category_id,
                p.description,
                p.sub_category,
                p.stock,
                p.image_url,
                p.featured,
                sc.quantity
            FROM shopping_cart sc
            JOIN products p ON p.product_id = sc.product_id
            WHERE sc.user_id = ?
            """;

        ShoppingCart cart = new ShoppingCart();

        jdbcTemplate.query(sql, rs ->
        {
            Product product = new Product();
            product.setProductId(rs.getInt("product_id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setCategoryId(rs.getInt("category_id"));
            product.setDescription(rs.getString("description"));
            product.setSubCategory(rs.getString("sub_category"));
            product.setStock(rs.getInt("stock"));
            product.setImageUrl(rs.getString("image_url"));
            product.setFeatured(rs.getBoolean("featured"));

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(rs.getInt("quantity"));
            item.setDiscountPercent(BigDecimal.ZERO);

            cart.add(item);
        }, userId);

        return cart;
    }

    // --------------------------------------------------
    // POST /cart/products/{productId}
    // Insert or increment quantity
    // --------------------------------------------------
    @Override
    public void addProduct(int userId, int productId)
    {
        String checkSql = """
            SELECT quantity
            FROM shopping_cart
            WHERE user_id = ?
              AND product_id = ?
            """;

        Integer existingQuantity = jdbcTemplate.query(
                checkSql,
                rs -> rs.next() ? rs.getInt("quantity") : null,
                userId,
                productId
        );

        if (existingQuantity == null)
        {
            String insertSql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1)
                """;
            jdbcTemplate.update(insertSql, userId, productId);
        }
        else
        {
            String updateSql = """
                UPDATE shopping_cart
                SET quantity = quantity + 1
                WHERE user_id = ?
                  AND product_id = ?
                """;
            jdbcTemplate.update(updateSql, userId, productId);
        }
    }

    // --------------------------------------------------
    // PUT /cart/products/{productId}
    // Update quantity only (no insert)
    // --------------------------------------------------
    @Override
    public void updateQuantity(int userId, int productId, int quantity)
    {
        String sql = """
            UPDATE shopping_cart
            SET quantity = ?
            WHERE user_id = ?
              AND product_id = ?
            """;

        jdbcTemplate.update(sql, quantity, userId, productId);
    }

    // --------------------------------------------------
    // DELETE /cart
    // Clear cart
    // --------------------------------------------------
    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
