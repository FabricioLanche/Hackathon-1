
package com.sparkyconsulting.aihub.repository;

import com.sparkyconsulting.aihub.model.entity.Company;
import com.sparkyconsulting.aihub.model.entity.ModelRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRestrictionRepository extends JpaRepository<ModelRestriction, Long> {
    List<ModelRestriction> findByCompany(Company company);
    List<ModelRestriction> findByCompanyId(Long companyId);
    Optional<ModelRestriction> findByCompanyIdAndModelNameAndModelProvider(Long companyId, String modelName, String modelProvider);
    boolean existsByCompanyIdAndModelNameAndModelProvider(Long companyId, String modelName, String modelProvider);
}
