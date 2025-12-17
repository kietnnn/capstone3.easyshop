package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.yearup.data.UserDao;
import org.yearup.models.User;

import java.util.List;

@Repository
public class MySqlUserDao implements UserDao
{
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MySqlUserDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User newUser)
    {
        String sql = """
            INSERT INTO users (username, hashed_password, role)
            VALUES (?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                newUser.getUsername(),
                passwordEncoder.encode(newUser.getPassword()),
                newUser.getRole()
        );

        return getByUserName(newUser.getUsername());
    }

    @Override
    public List<User> getAll()
    {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public User getUserById(int id)
    {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public User getByUserName(String username)
    {
        String sql = "SELECT * FROM users WHERE username = ?";

        try
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), username);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public int getIdByUsername(String username)
    {
        User user = getByUserName(username);
        return user != null ? user.getId() : -1;
    }

    @Override
    public boolean exists(String username)
    {
        return getByUserName(username) != null;
    }

    private User mapRow(java.sql.ResultSet rs) throws java.sql.SQLException
    {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("hashed_password"),
                rs.getString("role")
        );
    }
}
