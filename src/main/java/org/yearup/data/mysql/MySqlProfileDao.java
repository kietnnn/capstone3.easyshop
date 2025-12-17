package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@Repository
public class MySqlProfileDao implements ProfileDao
{
    private final JdbcTemplate jdbcTemplate;

    public MySqlProfileDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = """
            INSERT INTO profiles
            (user_id, first_name, last_name, phone, email, address, city, state, zip)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getEmail(),
                profile.getAddress(),
                profile.getCity(),
                profile.getState(),
                profile.getZip()
        );

        return profile;
    }
}
