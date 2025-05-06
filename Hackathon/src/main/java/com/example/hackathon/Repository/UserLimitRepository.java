
package com.sparkyconsulting.aihub.repository;

import com.sparkyconsulting.aihub.model.entity.User;
import com.sparkyconsulting.aihub.model.entity.UserLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
    List<UserLimit> findByUser(User user);
    List<UserLimit> findByUserId(Long userId);
    Optional<UserLimit> findByUserIdAndModelNameAndModelProvider(Long userId, String modelName, String modelProvider);
    boolean existsByUserIdAndModelNameAndModelProvider(Long userId, String modelName, String modelProvider);
}
