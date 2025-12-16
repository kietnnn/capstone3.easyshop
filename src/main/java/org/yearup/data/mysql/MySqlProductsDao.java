package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class MySqlProductsDao implements ProductDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlProductsDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory)
    {
        String sql = "SELECT * FROM products WHERE 1=1";
        if (categoryId != null) sql += " AND category_id = " + categoryId;
        if (minPrice != null) sql += " AND price >= " + minPrice;
        if (maxPrice != null) sql += " AND price <= " + maxPrice;
        if (subCategory != null) sql += " AND sub_category = '" + subCategory + "'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToProduct(rs));
    }

    @Override
    public List<Product> listByCategoryId(int categoryId)
    {
        String sql = "SELECT * FROM products WHERE category_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToProduct(rs), categoryId);
    }

    @Override
    public Product getById(int productId)
    {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToProduct(rs), productId);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Product create(Product product)
    {
        String sql = """
            INSERT INTO products (name, price, category_id, description, sub_category, stock, image_url, featured)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getCategoryId(),
                product.getDescription(),
                product.getSubCategory(),
                product.getStock(),
                product.getImageUrl(),
                product.isFeatured()
        );

        // Get last inserted ID
        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        product.setProductId(id);
        return product;
    }

    @Override
    public void delete(int productId)
    {
        String sql = "DELETE FROM products WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }

    // Helper method to map ResultSet to Product
    private Product mapRowToProduct(java.sql.ResultSet rs) throws java.sql.SQLException
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
        return product;
    }
}
