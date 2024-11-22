package com.example.microservicio_papeletas_internacion.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservicio_papeletas_internacion.models.EspecialidadesEntity;
import com.example.microservicio_papeletas_internacion.models.HistoriaClinicaEntity;
import com.example.microservicio_papeletas_internacion.models.PapeletaInternacionEntity;
import com.example.microservicio_papeletas_internacion.models.UsuarioEntity;
import com.example.microservicio_papeletas_internacion.models.dtos.PapeletaInternacionDto;
import com.example.microservicio_papeletas_internacion.repositories.EspecialidadesRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.HistoriaClinicaRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.PapeletaInternacionRepositoryJPA;
import com.example.microservicio_papeletas_internacion.repositories.UsuariosRepositoryJPA;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PDFService {
    @Autowired
    UsuariosRepositoryJPA usuariosRepositoryJPA;
    @Autowired
    HistoriaClinicaRepositoryJPA historiaClinicaRepositoryJPA;
    @Autowired
    EspecialidadesRepositoryJPA especialidadesRepositoryJPA;
    @Autowired
    PapeletaInternacionRepositoryJPA papeletaInternacionRepositoryJPA;

    public byte[] generarPdfReportePapeletaInternacion(PapeletaInternacionDto papeletaInternacionDto) throws JRException {
        Optional<PapeletaInternacionEntity> papeletaInternacionEntityOptional=(papeletaInternacionDto.getId()!=null)?papeletaInternacionRepositoryJPA.findById(papeletaInternacionDto.getId()):Optional.empty();
        if(papeletaInternacionEntityOptional.isPresent()){
            papeletaInternacionDto=new PapeletaInternacionDto().convertirPapeletaInternacionEntityAPapeletaInternacionDto(papeletaInternacionEntityOptional.get());
        }else{
            papeletaInternacionDto.setCreatedAt(new Date());
            papeletaInternacionDto.setUpdatedAt(new Date());
        }
        InputStream jrxmlInputStream = getClass().getClassLoader().getResourceAsStream("reports/papeleta_internacion.jrxml");
        HistoriaClinicaEntity historiaClinicaEntity = historiaClinicaRepositoryJPA.findById(papeletaInternacionDto.getIdHistoriaClinica()).orElseThrow(() -> new RuntimeException("Historia clinica no encontrada"));
        UsuarioEntity pacienteEntity = usuariosRepositoryJPA.findById(historiaClinicaEntity.getPaciente().getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioEntity medicoEntity = usuariosRepositoryJPA.findById(papeletaInternacionDto.getIdMedico()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        EspecialidadesEntity especialidadesEntity = especialidadesRepositoryJPA.findById(historiaClinicaEntity.getEspecialidad().getIdEspecialidad()).orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        if (jrxmlInputStream == null) {
            throw new JRException("No se pudo encontrar el archivo .jrxml en el classpath.");
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
        Map<String, Object> parameters = new HashMap<>();
        String fechaIngreso=papeletaInternacionDto.getFechaIngreso()!=null?formato.format(papeletaInternacionDto.getFechaIngreso()):"";
        parameters.put("fechaIngreso",fechaIngreso);
        parameters.put("diagnostico",papeletaInternacionDto.getDiagnostico());
       
        parameters.put("apellidoPaterno", pacienteEntity.getApellidoPaterno());
        parameters.put("apellidoMaterno", pacienteEntity.getApellidoMaterno());
        parameters.put("nombres", pacienteEntity.getNombres());
        parameters.put("nhc", historiaClinicaEntity.getIdHistoriaClinica()+"");
        parameters.put("edad", pacienteEntity.getEdad()+"");
        parameters.put("sexo", pacienteEntity.getSexo());
        parameters.put("estadoCivil", pacienteEntity.getEstadoCivil());
        parameters.put("unidad", especialidadesEntity.getNombre());



        parameters.put("fecha", formato.format(papeletaInternacionDto.getUpdatedAt()));
        parameters.put("nombreCompletoPaciente", pacienteEntity.getNombres()+" "+pacienteEntity.getApellidoPaterno());
        parameters.put("nombreCompletoMedico", medicoEntity.getNombres()+" "+medicoEntity.getApellidoPaterno());
        parameters.put("firmaPaciente", "");
        parameters.put("firmaMedico", "");

        parameters.put("IMAGE_PATH", getClass().getClassLoader().getResource("images/logo.jpeg").getPath());
        List<PapeletaInternacionDto> list = new ArrayList<>();
        list.add(papeletaInternacionDto);

        JRBeanCollectionDataSource emptyDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, emptyDataSource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

       return outputStream.toByteArray();    
    }
}
