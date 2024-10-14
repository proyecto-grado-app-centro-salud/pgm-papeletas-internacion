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
    private String diagnosticoPresuntivo;
    private Integer idEspecialidad;
    private String nombreEspecialidad;
    private Integer idMedico;
    private String nombreMedico;
    private Integer idPaciente;
    private String pacientePropietario;
    private String ciPropietario;

    public PapeletaInternacionDto convertirPapeletaInternacionEntityAPapeletaInternacionDto(PapeletaInternacionEntity papeletaInternacionEntity) {
        PapeletaInternacionDto dto = new PapeletaInternacionDto();
        dto.setIdPapeletaDeInternacion(papeletaInternacionEntity.getIdPapeletaDeInternacion());
        dto.setFechaIngreso(papeletaInternacionEntity.getFechaIngreso());
        dto.setDiagnostico(papeletaInternacionEntity.getDiagnostico());
        dto.setCreatedAt(papeletaInternacionEntity.getCreatedAt());
        dto.setUpdatedAt(papeletaInternacionEntity.getUpdatedAt());
        dto.setDeletedAt(papeletaInternacionEntity.getDeletedAt());
        dto.setIdHistoriaClinica(papeletaInternacionEntity.getHistoriaClinica().getIdHistoriaClinica());
        dto.setDiagnosticoPresuntivo(papeletaInternacionEntity.getHistoriaClinica().getDiagnosticoPresuntivo());
        dto.setIdEspecialidad(papeletaInternacionEntity.getHistoriaClinica().getEspecialidad().getIdEspecialidad());
        dto.setNombreEspecialidad(papeletaInternacionEntity.getHistoriaClinica().getEspecialidad().getNombre());
        dto.setIdMedico(papeletaInternacionEntity.getMedico().getIdUsuario());
        dto.setNombreMedico(papeletaInternacionEntity.getMedico().getNombres() + " " + 
                           papeletaInternacionEntity.getMedico().getApellidoPaterno() + " " + 
                           papeletaInternacionEntity.getMedico().getApellidoMaterno());
        dto.setIdPaciente(papeletaInternacionEntity.getHistoriaClinica().getPaciente().getIdUsuario());
        dto.setPacientePropietario(papeletaInternacionEntity.getHistoriaClinica().getPaciente().getNombres() + " " +
                                   papeletaInternacionEntity.getHistoriaClinica().getPaciente().getApellidoPaterno() + " " +        
                                   papeletaInternacionEntity.getHistoriaClinica().getPaciente().getApellidoMaterno());
        dto.setCiPropietario(papeletaInternacionEntity.getHistoriaClinica().getPaciente().getCi());
        return dto;
    }
}