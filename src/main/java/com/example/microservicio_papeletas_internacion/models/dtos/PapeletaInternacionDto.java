package com.example.microservicio_papeletas_internacion.models.dtos;

import java.util.Date;

import com.example.microservicio_papeletas_internacion.models.PapeletaInternacionEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PapeletaInternacionDto {
    private Integer idPapeletaDeInternacion;
    private Date fechaIngreso;
    private String diagnostico;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Integer idHistoriaClinica;
    private Integer idMedico;
    private String nombreMedico;

    public PapeletaInternacionDto convertirPapeletaInternacionEntityAPapeletaInternacionDto(PapeletaInternacionEntity papeletaInternacionEntity) {
        PapeletaInternacionDto dto = new PapeletaInternacionDto();
        dto.setIdPapeletaDeInternacion(papeletaInternacionEntity.getIdPapeletaDeInternacion());
        dto.setFechaIngreso(papeletaInternacionEntity.getFechaIngreso());
        dto.setDiagnostico(papeletaInternacionEntity.getDiagnostico());
        dto.setCreatedAt(papeletaInternacionEntity.getCreatedAt());
        dto.setUpdatedAt(papeletaInternacionEntity.getUpdatedAt());
        dto.setDeletedAt(papeletaInternacionEntity.getDeletedAt());
        dto.setIdHistoriaClinica(papeletaInternacionEntity.getHistoriaClinica().getIdHistoriaClinica());
        dto.setIdMedico(papeletaInternacionEntity.getMedico().getIdUsuario());
        dto.setNombreMedico(papeletaInternacionEntity.getMedico().getNombres() + " " + 
                           papeletaInternacionEntity.getMedico().getApellidoPaterno() + " " + 
                           papeletaInternacionEntity.getMedico().getApellidoMaterno());
        return dto;
    }
}