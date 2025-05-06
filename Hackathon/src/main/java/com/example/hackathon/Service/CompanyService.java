package com.sparkyconsulting.aihub.service;

import com.sparkyconsulting.aihub.exception.ResourceNotFoundException;
import com.sparkyconsulting.aihub.model.dto.CompanyDTO;
import com.sparkyconsulting.aihub.model.entity.Company;
import com.sparkyconsulting.aihub.model.entity.User;
import com.sparkyconsulting.aihub.repository.CompanyRepository;
import com.sparkyconsulting.aihub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return convertToDTO(company);
    }

    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        // Validar si la empresa ya existe
        if (companyRepository.existsByName(companyDTO.getName())) {
            throw new IllegalStateException("Company with name " + companyDTO.getName() + " already exists");
        }
        if (companyRepository.existsByRuc(companyDTO.getRuc())) {
            throw new IllegalStateException("Company with RUC " + companyDTO.getRuc() + " already exists");
        }

        // Crear y guardar la empresa
        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setRuc(companyDTO.getRuc());
        company.setContactEmail(companyDTO.getContactEmail());
        company.setContactPhone(companyDTO.getContactPhone());
        company.setActive(true);
        
        Company savedCompany = companyRepository.save(company);

        // Si se proporciona información del administrador, crearlo
        if (companyDTO.getAdminUser() != null) {
            User admin = new User();
            admin.setFullName(companyDTO.getAdminUser().getFullName());
            admin.setEmail(companyDTO.getAdminUser().getEmail());
            admin.setPassword(passwordEncoder.encode(companyDTO.getAdminUser().getPassword()));
            admin.setCompany(savedCompany);
            admin.setRole(User.UserRole.ROLE_COMPANY_ADMIN);
            admin.setActive(true);
            
            userRepository.save(admin);
        }

        return convertToDTO(savedCompany);
    }

    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        company.setName(companyDTO.getName());
        company.setContactEmail(companyDTO.getContactEmail());
        company.setContactPhone(companyDTO.getContactPhone());

        Company updatedCompany = companyRepository.save(company);
        return convertToDTO(updatedCompany);
    }

    @Transactional
    public CompanyDTO updateCompanyStatus(Long id, boolean active) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        company.setActive(active);
        Company updatedCompany = companyRepository.save(company);
        return convertToDTO(updatedCompany);
    }

    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setRuc(company.getRuc());
        dto.setRegistrationDate(company.getRegistrationDate());
        dto.setActive(company.isActive());
        dto.setContactEmail(company.getContactEmail());
        dto.setContactPhone(company.getContactPhone());
        return dto;
    }
}
