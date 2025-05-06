package com.sparkyconsulting.aihub.repository;

import com.sparkyconsulting.aihub.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
    Optional<Company> findByRuc(String ruc);
    boolean existsByName(String name);
    boolean existsByRuc(String ruc);
}
