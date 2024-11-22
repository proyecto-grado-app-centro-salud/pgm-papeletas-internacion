package com.example.microservicio_papeletas_internacion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservicio_papeletas_internacion.models.HistoriaClinicaEntity;
import com.example.microservicio_papeletas_internacion.models.PapeletaInternacionEntity;
import com.example.microservicio_papeletas_internacion.models.UsuarioEntity;
import com.example.microservicio_papeletas_internacion.models.dtos.PapeletaInternacionDto;
import com.example.microservicio_papeletas_internacion.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.PapeletaInternacionRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.UsuariosRepositoryJPA;

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

    public List<PapeletaInternacionDto> obtenerTodasPapeletasInternacion() {
        List<PapeletaInternacionEntity> papeletas = papeletaInternacionRepositoryJPA.findAll();
        return papeletas.stream()
                        .map(papeleta -> new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeleta))
                        .toList();
    }

    public PapeletaInternacionDto obtenerPapeletaInternacionPorId(Integer id) {
        PapeletaInternacionEntity papeletaInternacionEntity = papeletaInternacionRepositoryJPA.findById(id)
                .orElseThrow(() -> new RuntimeException("Papeleta de internación no encontrada"));
        return new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeletaInternacionEntity);
    }

    public PapeletaInternacionDto actualizarPapeletaInternacion(Integer idPapeleta, PapeletaInternacionDto papeletaInternacionDto) {
        PapeletaInternacionEntity papeletaInternacionEntity = papeletaInternacionRepositoryJPA.findById(idPapeleta)
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

    public List<PapeletaInternacionDto> obtenerTodasPapeletasInternacionDePaciente(int idPaciente) {
        List<PapeletaInternacionEntity> papeletas = papeletaInternacionRepositoryJPA.obtenerNotasEvolucionPaciente(idPaciente);
        return papeletas.stream()
                        .map(papeleta -> new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeleta))
                        .toList();
    }

    public byte[] obtenerPDFPapeletaInternacion(PapeletaInternacionDto papeletaInternacionDto) {
        try {
                return pdfService.generarPdfReportePapeletaInternacion(papeletaInternacionDto);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al generar el PDF de la historia clinica.", e);
            }
        }
}
