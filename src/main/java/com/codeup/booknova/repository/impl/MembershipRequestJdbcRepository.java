package com.codeup.booknova.repository.impl;

import com.codeup.booknova.domain.MembershipRequest;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.jdbc.JdbcTemplateLight;
import com.codeup.booknova.jdbc.RowMapper;
import com.codeup.booknova.repository.IMembershipRequestRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of IMembershipRequestRepository
 */
public class MembershipRequestJdbcRepository implements IMembershipRequestRepository {
    
    private final JdbcTemplateLight jdbc;
    
    private static final RowMapper<MembershipRequest> REQUEST_MAPPER = rs -> {
        MembershipRequest request = new MembershipRequest();
        request.setId(rs.getInt("id"));
        request.setUserId(rs.getInt("user_id"));
        request.setUserName(rs.getString("user_name"));
        request.setUserEmail(rs.getString("user_email"));
        request.setStatus(rs.getString("status"));
        request.setRequestReason(rs.getString("request_reason"));
        
        int approvedBy = rs.getInt("approved_by_user_id");
        if (!rs.wasNull()) {
            request.setApprovedByUserId(approvedBy);
        }
        
        Timestamp requestedAt = rs.getTimestamp("requested_at");
        if (requestedAt != null) {
            request.setRequestedAt(requestedAt.toInstant());
        }
        
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            request.setProcessedAt(processedAt.toInstant());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            request.setCreatedAt(createdAt.toInstant());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            request.setUpdatedAt(updatedAt.toInstant());
        }
        
        return request;
    };
    
    public MembershipRequestJdbcRepository(JdbcTemplateLight jdbc) {
        this.jdbc = jdbc;
    }
    
    @Override
    public MembershipRequest create(MembershipRequest request) throws DatabaseException {
        String sql = "INSERT INTO membership_request (user_id, user_name, user_email, status, request_reason, requested_at, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Instant now = Instant.now();
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        if (request.getRequestedAt() == null) {
            request.setRequestedAt(now);
        }
        
        jdbc.update(sql, ps -> {
            try {
                ps.setInt(1, request.getUserId());
                ps.setString(2, request.getUserName());
                ps.setString(3, request.getUserEmail());
                ps.setString(4, request.getStatus());
                ps.setString(5, request.getRequestReason());
                ps.setTimestamp(6, Timestamp.from(request.getRequestedAt()));
                ps.setTimestamp(7, Timestamp.from(request.getCreatedAt()));
                ps.setTimestamp(8, Timestamp.from(request.getUpdatedAt()));
            } catch (SQLException e) {
                throw new RuntimeException("Error creating membership request", e);
            }
        });
        
        // Get the created request
        return findPendingByUserId(request.getUserId())
            .orElseThrow(() -> new DatabaseException("Failed to retrieve created request"));
    }
    
    @Override
    public Optional<MembershipRequest> findById(Integer id) {
        String sql = "SELECT * FROM membership_request WHERE id = ?";
        List<MembershipRequest> results = jdbc.query(sql, ps -> {
            try {
                ps.setInt(1, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, REQUEST_MAPPER);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public Optional<MembershipRequest> findPendingByUserId(Integer userId) {
        String sql = "SELECT * FROM membership_request WHERE user_id = ? AND status = 'PENDING' ORDER BY requested_at DESC LIMIT 1";
        List<MembershipRequest> results = jdbc.query(sql, ps -> {
            try {
                ps.setInt(1, userId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, REQUEST_MAPPER);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<MembershipRequest> findAll() {
        String sql = "SELECT * FROM membership_request ORDER BY requested_at DESC";
        return jdbc.query(sql, null, REQUEST_MAPPER);
    }
    
    @Override
    public List<MembershipRequest> findAllPending() {
        String sql = "SELECT * FROM membership_request WHERE status = 'PENDING' ORDER BY requested_at ASC";
        return jdbc.query(sql, null, REQUEST_MAPPER);
    }
    
    @Override
    public MembershipRequest update(MembershipRequest request) throws DatabaseException {
        String sql = "UPDATE membership_request SET " +
                    "status = ?, " +
                    "approved_by_user_id = ?, " +
                    "processed_at = ?, " +
                    "updated_at = ? " +
                    "WHERE id = ?";
        
        request.setUpdatedAt(Instant.now());
        
        jdbc.update(sql, ps -> {
            try {
                ps.setString(1, request.getStatus());
                if (request.getApprovedByUserId() != null) {
                    ps.setInt(2, request.getApprovedByUserId());
                } else {
                    ps.setNull(2, java.sql.Types.INTEGER);
                }
                if (request.getProcessedAt() != null) {
                    ps.setTimestamp(3, Timestamp.from(request.getProcessedAt()));
                } else {
                    ps.setNull(3, java.sql.Types.TIMESTAMP);
                }
                ps.setTimestamp(4, Timestamp.from(request.getUpdatedAt()));
                ps.setInt(5, request.getId());
            } catch (SQLException e) {
                throw new RuntimeException("Error updating membership request", e);
            }
        });
        
        return request;
    }
    
    @Override
    public void delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM membership_request WHERE id = ?";
        jdbc.update(sql, ps -> {
            try {
                ps.setInt(1, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @Override
    public boolean hasPendingRequest(Integer userId) {
        String sql = "SELECT COUNT(*) as count FROM membership_request WHERE user_id = ? AND status = 'PENDING'";
        List<Integer> results = jdbc.query(sql, ps -> {
            try {
                ps.setInt(1, userId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, rs -> rs.getInt("count"));
        
        return !results.isEmpty() && results.get(0) > 0;
    }
}
