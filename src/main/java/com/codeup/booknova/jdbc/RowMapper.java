/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional interface for mapping database result set rows to domain objects.
 * <p>
 * This interface defines a contract for converting {@link ResultSet} rows
 * into domain objects. It's used in conjunction with {@link JdbcTemplateLight}
 * to provide type-safe database query results.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * RowMapper<User> userMapper = rs -> {
 *     User user = new User(
 *         rs.getString("name"),
 *         rs.getString("email"),
 *         rs.getString("password"),
 *         rs.getString("phone")
 *     );
 *     user.setId(rs.getInt("id"));
 *     return user;
 * };
 * }</pre>
 * @param <T> the type of object to map to
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface RowMapper<T> {
    
    /**
     * Maps a single row of the {@link ResultSet} to an object of type T.
     * <p>
     * This method is called for each row in the result set. The ResultSet
     * is positioned at the current row, and this method should extract
     * the necessary data to construct and return the domain object.
     * </p>
     * 
     * @param rs the ResultSet positioned at the current row
     * @return the mapped object, should not be null
     * @throws SQLException if any database access error occurs
     */
    T map(ResultSet rs) throws SQLException;
}