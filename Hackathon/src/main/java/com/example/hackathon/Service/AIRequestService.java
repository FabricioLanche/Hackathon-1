package com.sparkyconsulting.aihub.service;

import com.sparkyconsulting.aihub.exception.LimitExceededException;
import com.sparkyconsulting.aihub.exception.ResourceNotFoundException;
import com.sparkyconsulting.aihub.model.dto.AIRequestDTO;
import com.sparkyconsulting.aihub.model.entity.AIRequest;
import com.sparkyconsulting.aihub.model.entity.User;
import com.sparkyconsulting.aihub.repository.AIRequestRepository;
import com.sparkyconsulting.aihub.repository.UserRepository;
import com.sparkyconsulting.aihub.util.AIModelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AIRequestService {

    private final AIRequestRepository aiRequestRepository;
    private final UserRepository userRepository;
    private final UserLimitService userLimitService;
    private final ModelRestrictionService modelRestrictionService;
    private final AIModelClient aiModelClient;

    public Page<AIRequestDTO> getRequestsByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return aiRequestRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }

    public Page<AIRequestDTO> getRequestsByCompanyId(Long companyId, Pageable pageable) {
        return aiRequestRepository.findByCompanyId(companyId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public AIRequestDTO processAIRequest(Long userId, AIRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Verificar si la empresa tiene permiso para usar este modelo
        if (!modelRestrictionService.isModelAllowedForCompany(
                user.getCompany().getId(), requestDTO.getModelName(), requestDTO.getModelProvider())) {
            throw new IllegalStateException("This model is not enabled for your company");
        }
        
        // Crear el registro de solicitud
        AIRequest request = new AIRequest();
