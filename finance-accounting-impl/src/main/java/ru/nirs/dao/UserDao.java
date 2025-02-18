package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.nirs.entity.User;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {
    
    @Query("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    boolean existsByUsername(@Param("username") String username);
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    boolean existsByEmail(@Param("email") String email);
} 