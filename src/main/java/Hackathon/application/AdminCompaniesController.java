package Hackathon.application;

import Hackathon.domain.EmpresaService;
import Hackathon.dto.EmpresaDTO;
import Hackathon.domain.Empresa;
import Hackathon.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class AdminCompaniesController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> createCompany(@RequestBody EmpresaDTO empresaDto) {
        Empresa nuevaEmpresa = empresaService.createCompany(empresaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> listCompanies() {
        List<EmpresaDTO> empresas = empresaService.listAllCompanies();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> getCompany(@PathVariable Long id) {
        return empresaService.getCompanyById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada con el ID: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody EmpresaDTO empresaDto) {
        try {
            Empresa empresaActualizada = empresaService.updateCompany(id, empresaDto);
            return ResponseEntity.ok(empresaActualizada);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> changeCompanyStatus(@PathVariable Long id, @RequestBody Boolean isActive) {
        try {
            Empresa empresaActualizada = empresaService.changeCompanyStatus(id, isActive);
            return ResponseEntity.ok(empresaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/consumption")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<?> getCompanyConsumption(@PathVariable Long id) {
        try {
            String reporteConsumo = empresaService.getCompanyConsumption(id);
            return ResponseEntity.ok(reporteConsumo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
