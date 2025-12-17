package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.List;

@Repository
public class MySqlCategoryDao implements CategoryDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlCategoryDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> getAllCategories()
    {
        String sql = """
            SELECT category_id, name, description
            FROM categories
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = """
            SELECT category_id, name, description
            FROM categories
            WHERE category_id = ?
        """;

        try
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), categoryId);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Category create(Category category)
    {
        String sql = """
            INSERT INTO categories (name, description)
            VALUES (?, ?)
        """;

        jdbcTemplate.update(sql,
                category.getName(),
                category.getDescription()
        );

        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        category.setCategoryId(id);
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = """
            UPDATE categories
            SET name = ?, description = ?
            WHERE category_id = ?
        """;

        jdbcTemplate.update(sql,
                category.getName(),
                category.getDescription(),
                categoryId
        );
    }

    @Override
    public void delete(int categoryId)
    {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        jdbcTemplate.update(sql, categoryId);
    }

    private Category mapRow(java.sql.ResultSet rs) throws java.sql.SQLException
    {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        return category;
    }
}
