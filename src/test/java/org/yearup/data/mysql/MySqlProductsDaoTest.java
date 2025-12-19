//package org.yearup.data.mysql;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.yearup.data.ProductDao;
//import org.yearup.models.Product;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MySqlProductsDaoTest {
//
//    private ProductDao productDao;
//
//    @BeforeEach
//    public void setUp() {
//        // 1️⃣ Create H2 in-memory DataSource
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"); // keep DB alive
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//
//        // 2️⃣ Create JdbcTemplate
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//
//        // 3️⃣ Create products table
//        jdbcTemplate.execute("""
//            CREATE TABLE IF NOT EXISTS products (
//                product_id INT AUTO_INCREMENT PRIMARY KEY,
//                name VARCHAR(255),
//                price DECIMAL(10,2),
//                category_id INT,
//                description VARCHAR(500),
//                sub_category VARCHAR(100),
//                stock INT,
//                image_url VARCHAR(255),
//                featured BOOLEAN
//            );
//        """);
//
//        // 4️⃣ Insert sample product
//        jdbcTemplate.update("""
//            INSERT INTO products
//            (name, price, category_id, description, sub_category, stock, image_url, featured)
//            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
//        """, "Laptop", new BigDecimal("999.99"), 1, "High performance laptop", "Black", 10, "laptop.jpg", true);
//
//        // 5️⃣ Instantiate DAO
//        productDao = new MySqlProductsDao(jdbcTemplate);
//    }
//
//    @Test
//    public void getById_shouldReturn_theCorrectProduct() {
//        Product product = productDao.getById(1);
//
//        assertNotNull(product, "Product should not be null");
//        assertEquals(1, product.getProductId());
//        assertEquals("Laptop", product.getName());
//        assertEquals(new BigDecimal("999.99"), product.getPrice());
//        assertEquals(1, product.getCategoryId());
//        assertEquals("High performance laptop", product.getDescription());
//        assertEquals("Black", product.getSubCategory());
//        assertEquals(10, product.getStock());
//        assertEquals("laptop.jpg", product.getImageUrl());
//        assertTrue(product.isFeatured());
//    }
//}
