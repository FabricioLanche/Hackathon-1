package com.sparkyconsulting.aihub.repository;

import com.sparkyconsulting.aihub.model.entity.Company;
import com.sparkyconsulting.aihub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCompany(Company company);
    List<User> findByCompanyId(Long companyId);
    List<User> findByCompanyAndRole(Company company, User.UserRole role);
    boolean existsByEmail(String email);
}
