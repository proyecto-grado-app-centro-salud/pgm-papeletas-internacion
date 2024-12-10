package com.example.microservicio_papeletas_internacion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.microservicio_papeletas_internacion.models.HistoriaClinicaEntity;
import com.example.microservicio_papeletas_internacion.models.PapeletaInternacionEntity;
import com.example.microservicio_papeletas_internacion.models.UsuarioEntity;
import com.example.microservicio_papeletas_internacion.models.dtos.PapeletaInternacionDto;
import com.example.microservicio_papeletas_internacion.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.PapeletaInternacionRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.UsuariosRepositoryJPA;
import com.example.microservicio_papeletas_internacion.util.PapeletasInternacionSpecification;

@Service
public class PapeletasInternacionService {
    @Autowired
    private PapeletaInternacionRepositoryJPA papeletaInternacionRepositoryJPA;
    @Autowired
    private HistoriaClinicaRepositoryJPA historiaClinicaRepositoryJPA;
    @Autowired
    private UsuariosRepositoryJPA usuariosRepositoryJPA;
    @Autowired
    PDFService pdfService;
    @Autowired
    private ConvertirTiposDatosService convertirTiposDatosService;

    public PapeletaInternacionDto registrarPapeletaInternacion(PapeletaInternacionDto papeletaInternacionDto) {
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findById(papeletaInternacionDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(papeletaInternacionDto.getIdHistoriaClinica())
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        PapeletaInternacionEntity papeletaInternacionEntity = new PapeletaInternacionEntity();
        papeletaInternacionEntity.setFechaIngreso(papeletaInternacionDto.getFechaIngreso());
        papeletaInternacionEntity.setDiagnostico(papeletaInternacionDto.getDiagnostico());
        papeletaInternacionEntity.setHistoriaClinica(historiaClinicaEntity);
        papeletaInternacionEntity.setMedico(medicoEntity);

        papeletaInternacionEntity = papeletaInternacionRepositoryJPA.save(papeletaInternacionEntity);
        return new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeletaInternacionEntity);
    }

    public Page<PapeletaInternacionDto> obtenerTodasPapeletasInternacion(String fechaInicio, String fechaFin, String ciPaciente, String nombrePaciente, String nombreMedico, String nombreEspecialidad, String diagnosticoPresuntivo, Integer page, Integer size) {
        Pageable pageable = Pageable.unpaged();
        if(page!=null && size!=null){
            pageable = PageRequest.of(page, size);
        } 
        Specification<PapeletaInternacionEntity> spec = Specification.where(PapeletasInternacionSpecification.obtenerPapeletasInternacionPorParametros(convertirTiposDatosService.convertirStringADate(fechaInicio),convertirTiposDatosService.convertirStringADate(fechaFin),ciPaciente,nombrePaciente,nombreMedico,nombreEspecialidad,diagnosticoPresuntivo));
        Page<PapeletaInternacionEntity> papeletasEntitiesPage=papeletaInternacionRepositoryJPA.findAll(spec,pageable);
        return papeletasEntitiesPage.map(PapeletaInternacionDto::convertirPapeletaInternacionEntityAPapeletaInternacionDto);
        }

    public PapeletaInternacionDto obtenerPapeletaInternacionPorId(Integer id) {
        PapeletaInternacionEntity papeletaInternacionEntity = papeletaInternacionRepositoryJPA.findByIdPapeletaDeInternacionAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Papeleta de internación no encontrada"));
        return new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeletaInternacionEntity);
    }

    public PapeletaInternacionDto actualizarPapeletaInternacion(Integer idPapeleta, PapeletaInternacionDto papeletaInternacionDto) {
        PapeletaInternacionEntity papeletaInternacionEntity = papeletaInternacionRepositoryJPA.findByIdPapeletaDeInternacionAndDeletedAtIsNull(idPapeleta)
                .orElseThrow(() -> new RuntimeException("Papeleta de internación no encontrada"));
        
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findById(papeletaInternacionDto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(papeletaInternacionDto.getIdHistoriaClinica())
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        papeletaInternacionEntity.setFechaIngreso(papeletaInternacionDto.getFechaIngreso());
        papeletaInternacionEntity.setDiagnostico(papeletaInternacionDto.getDiagnostico());
        papeletaInternacionEntity.setHistoriaClinica(historiaClinicaEntity);
        papeletaInternacionEntity.setMedico(medicoEntity);

        papeletaInternacionEntity = papeletaInternacionRepositoryJPA.save(papeletaInternacionEntity);
        return new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeletaInternacionEntity);
    }

    public Page<PapeletaInternacionDto> obtenerTodasPapeletasInternacionDePaciente(int idPaciente, String fechaInicio, String fechaFin, String nombreMedico, String nombreEspecialidad, String diagnosticoPresuntivo, Integer page, Integer size) {
        Pageable pageable = Pageable.unpaged();
        if(page!=null && size!=null){
            pageable = PageRequest.of(page, size);
        } 
        Specification<PapeletaInternacionEntity> spec = Specification.where(PapeletasInternacionSpecification.obtenerPapeletasInternacionDePacientePorParametros(idPaciente,convertirTiposDatosService.convertirStringADate(fechaInicio),convertirTiposDatosService.convertirStringADate(fechaFin),nombreMedico,nombreEspecialidad,diagnosticoPresuntivo));
        Page<PapeletaInternacionEntity> papeletasEntitiesPage=papeletaInternacionRepositoryJPA.findAll(spec,pageable);
        return papeletasEntitiesPage.map(PapeletaInternacionDto::convertirPapeletaInternacionEntityAPapeletaInternacionDto);
    }

    public byte[] obtenerPDFPapeletaInternacion(PapeletaInternacionDto papeletaInternacionDto) {
        try {
                return pdfService.generarPdfReportePapeletaInternacion(papeletaInternacionDto);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al generar el PDF de la historia clinica.", e);
            }
        }

    public void delete(int id) {
        PapeletaInternacionEntity papeletaInternacionEntity = papeletaInternacionRepositoryJPA.findByIdPapeletaDeInternacionAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("Papeleta de internación no encontrada"));
        papeletaInternacionEntity.markAsDeleted();
        papeletaInternacionRepositoryJPA.save(papeletaInternacionEntity);

    }

   
}
