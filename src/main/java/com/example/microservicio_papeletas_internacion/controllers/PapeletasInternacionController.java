package com.example.microservicio_papeletas_internacion.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservicio_papeletas_internacion.models.dtos.PapeletaInternacionDto;
import com.example.microservicio_papeletas_internacion.services.ContainerMetadataService;
import com.example.microservicio_papeletas_internacion.services.PapeletasInternacionService;

import jakarta.annotation.security.PermitAll;



@RestController
@RequestMapping(path = "/papeletas-internacion")
public class PapeletasInternacionController {
    @Autowired
    private ContainerMetadataService containerMetadataService;

    @Autowired
    private PapeletasInternacionService papeletasInternacionService;

    @PostMapping
    @PermitAll
    public ResponseEntity<PapeletaInternacionDto> registrarPapeletaInternacion(@RequestBody PapeletaInternacionDto papeletaInternacionDto) {
        try {
            PapeletaInternacionDto nuevaPapeleta = papeletasInternacionService.registrarPapeletaInternacion(papeletaInternacionDto);
            return new ResponseEntity<>(nuevaPapeleta, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<Page<PapeletaInternacionDto>> obtenerTodasPapeletasInternacion(@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,@RequestParam(required = false) String ciPaciente,@RequestParam(required = false) String nombrePaciente,@RequestParam(required = false) String nombreMedico,@RequestParam(required = false) String nombreEspecialidad,@RequestParam(required = false) String diagnosticoPresuntivo,@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        try {
            Page<PapeletaInternacionDto> papeletas = papeletasInternacionService.obtenerTodasPapeletasInternacion(fechaInicio,fechaFin,ciPaciente,nombrePaciente,nombreMedico,nombreEspecialidad,diagnosticoPresuntivo,page,size);
            return new ResponseEntity<>(papeletas, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<PapeletaInternacionDto> obtenerPapeletaInternacionPorId(@PathVariable Integer id) {
        try {
            PapeletaInternacionDto papeleta = papeletasInternacionService.obtenerPapeletaInternacionPorId(id);
            return new ResponseEntity<>(papeleta, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PermitAll
    public ResponseEntity<PapeletaInternacionDto> actualizarPapeletaInternacion(@PathVariable Integer id, @RequestBody PapeletaInternacionDto actualizada) {
        try {
            PapeletaInternacionDto papeletaActualizada = papeletasInternacionService.actualizarPapeletaInternacion(id, actualizada);
            return new ResponseEntity<>(papeletaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    @PermitAll
    public ResponseEntity<Page<PapeletaInternacionDto>> obtenerPapeletasInternacionDePaciente(@PathVariable int idPaciente,@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,@RequestParam(required = false) String nombreMedico,@RequestParam(required = false) String nombreEspecialidad,@RequestParam(required = false) String diagnosticoPresuntivo,@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size){
        try {
            Page<PapeletaInternacionDto> papeletas = papeletasInternacionService.obtenerTodasPapeletasInternacionDePaciente(idPaciente,fechaInicio,fechaFin,nombreMedico,nombreEspecialidad,diagnosticoPresuntivo,page,size);
            return new ResponseEntity<>(papeletas, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/info-container")
    @PermitAll
    public @ResponseBody String obtenerInformacionContenedor() {
        return "microservicio historias clinicas: " + containerMetadataService.retrieveContainerMetadataInfo();
    }
    @GetMapping("/pdf")
    @PermitAll
    public ResponseEntity<byte[]> obtenerPDFDePapeletaInternacion(PapeletaInternacionDto papeletaInternacionDto) {
        try {
            byte[] pdfBytes = papeletasInternacionService.obtenerPDFPapeletaInternacion(papeletaInternacionDto);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=PapeletaInternacion.pdf");
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Length", "" + pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(value = "/{id}")
    @PermitAll
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try{
            papeletasInternacionService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping(value = "/historia-clinica/{id}")
    @PermitAll
    public ResponseEntity<Void> deletePapeletasInternacionDeHistoriaClinica(@PathVariable int id) {
        try{
            papeletasInternacionService.deletePapeletasInternacionDeHistoriaClinica(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
