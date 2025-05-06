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
        request.setUser(user);
        request.setCompany(user.getCompany());
        request.setPrompt(requestDTO.getPrompt());
        request.setModelName(requestDTO.getModelName());
        request.setModelProvider(requestDTO.getModelProvider());
        request.setTimestamp(LocalDateTime.now());
        request.setInputTokens(requestDTO.getInputTokens());
        request.setOutputTokens(0); // Se actualizará después de la respuesta
        
        // Verificar los límites del usuario
        userLimitService.checkAndUpdateUserLimits(user.getId(), request);
        
        // Llamar al cliente del modelo AI
        String response = aiModelClient.sendRequest(
            requestDTO.getModelProvider(), 
            requestDTO.getModelName(),
            requestDTO.getPrompt()
        );
        
        // Calcular tokens de salida (simulación)
        int outputTokens = calculateOutputTokens(response);
        request.setOutputTokens(outputTokens);
        request.setResponse(response);
        
        // Guardar la solicitud
        AIRequest savedRequest = aiRequestRepository.save(request);
        
        // Actualizar los límites del usuario con los tokens de salida
        userLimitService.updateUserLimitsWithOutputTokens(user.getId(), outputTokens);
        
        return convertToDTO(savedRequest);
    }
    
    public AIRequestDTO getRequestById(Long requestId) {
        AIRequest request = aiRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("AI request not found with id: " + requestId));
        return convertToDTO(request);
    }
    
    private int calculateOutputTokens(String response) {
        // Una implementación simple: aproximadamente 4 caracteres por token
        // En una implementación real, usarías un tokenizador específico para el modelo
        return response.length() / 4;
    }
    
    private AIRequestDTO convertToDTO(AIRequest request) {
        AIRequestDTO dto = new AIRequestDTO();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setUserName(request.getUser().getFullName());
        dto.setCompanyId(request.getCompany().getId());
        dto.setCompanyName(request.getCompany().getName());
        dto.setPrompt(request.getPrompt());
        dto.setResponse(request.getResponse());
        dto.setModelName(request.getModelName());
        dto.setModelProvider(request.getModelProvider());
        dto.setTimestamp(request.getTimestamp());
        dto.setInputTokens(request.getInputTokens());
        dto.setOutputTokens(request.getOutputTokens());
        return dto;
    }
}
