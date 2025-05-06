package com.sparkyconsulting.aihub.service;

import com.sparkyconsulting.aihub.exception.ResourceNotFoundException;
import com.sparkyconsulting.aihub.model.dto.ModelRestrictionDTO;
import com.sparkyconsulting.aihub.model.entity.Company;
import com.sparkyconsulting.aihub.model.entity.ModelRestriction;
import com.sparkyconsulting.aihub.repository.CompanyRepository;
import com.sparkyconsulting.aihub.repository.ModelRestrictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelRestrictionService {

    private final ModelRestrictionRepository modelRestrictionRepository;
    private final CompanyRepository companyRepository;

    public List<ModelRestrictionDTO> getAllRestrictionsByCompanyId(Long companyId) {
        return modelRestrictionRepository.findByCompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ModelRestrictionDTO getRestrictionById(Long id) {
        ModelRestriction restriction = modelRestrictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Model restriction not found with id: " + id));
        return convertToDTO(restriction);
    }

    @Transactional
    public ModelRestrictionDTO createRestriction(ModelRestrictionDTO restrictionDTO) {
        Company company = companyRepository.findById(restrictionDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + restrictionDTO.getCompanyId()));
        
        // Verificar si ya existe una restricción para este modelo en esta empresa
        if (modelRestrictionRepository.existsByCompanyIdAndModelNameAndModelProvider(
                company.getId(), restrictionDTO.getModelName(), restrictionDTO.getModelProvider())) {
            throw new IllegalStateException("A restriction for this model already exists for this company");
        }
        
        ModelRestriction restriction = new ModelRestriction();
        restriction.setCompany(company);
        restriction.setModelName(restrictionDTO.getModelName());
        restriction.setModelProvider(restrictionDTO.getModelProvider());
        restriction.setEnabled(restrictionDTO.isEnabled());
        restriction.setMaxRequestsPerWindow(restrictionDTO.getMaxRequestsPerWindow());
        restriction.setMaxTokensPerWindow(restrictionDTO.getMaxTokensPerWindow());
        restriction.setWindowDurationMinutes(restrictionDTO.getWindowDurationMinutes());
        
        ModelRestriction savedRestriction = modelRestrictionRepository.save(restriction);
        return convertToDTO(savedRestriction);
    }

    @Transactional
    public ModelRestrictionDTO updateRestriction(Long id, ModelRestrictionDTO restrictionDTO) {
        ModelRestriction restriction = modelRestrictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Model restriction not found with id: " + id));
        
        restriction.setEnabled(restrictionDTO.isEnabled());
        restriction.setMaxRequestsPerWindow(restrictionDTO.getMaxRequestsPerWindow());
        restriction.setMaxTokensPerWindow(restrictionDTO.getMaxTokensPerWindow());
        restriction.setWindowDurationMinutes(restrictionDTO.getWindowDurationMinutes());
        
        ModelRestriction updatedRestriction = modelRestrictionRepository.save(restriction);
        return convertToDTO(updatedRestriction);
    }

    @Transactional
    public void deleteRestriction(Long id) {
        if (!modelRestrictionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Model restriction not found with id: " + id);
        }
        modelRestrictionRepository.deleteById(id);
    }

    public boolean isModelAllowedForCompany(Long companyId, String modelName, String modelProvider) {
        return modelRestrictionRepository.findByCompanyIdAndModelNameAndModelProvider(companyId, modelName, modelProvider)
                .map(ModelRestriction::isEnabled)
                .orElse(false); // Si no existe restricción, el modelo no está permitido
    }

    private ModelRestrictionDTO convertToDTO(ModelRestriction restriction) {
        ModelRestrictionDTO dto = new ModelRestrictionDTO();
        dto.setId(restriction.getId());
        dto.setModelName(restriction.getModelName());
        dto.setModelProvider(restriction.getModelProvider());
        dto.setEnabled(restriction.isEnabled());
        dto.setMaxRequestsPerWindow(restriction.getMaxRequestsPerWindow());
        dto.setMaxTokensPerWindow(restriction.getMaxTokensPerWindow());
        dto.setWindowDurationMinutes(restriction.getWindowDurationMinutes());
        dto.setCreatedAt(restriction.getCreatedAt());
        dto.setUpdatedAt(restriction.getUpdatedAt());
        dto.setCompanyId(restriction.getCompany().getId());
        return dto;
    }
}
