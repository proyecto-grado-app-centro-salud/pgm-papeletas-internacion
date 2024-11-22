package com.example.microservicio_papeletas_internacion.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservicio_papeletas_internacion.models.dtos.PapeletaInternacionDto;
import com.example.microservicio_papeletas_internacion.services.ContainerMetadataService;
import com.example.microservicio_papeletas_internacion.services.PapeletasInternacionService;



@RestController
@RequestMapping(path = "/papeletas-internacion")
public class PapeletasInternacionController {
    @Autowired
    private ContainerMetadataService containerMetadataService;

    @Autowired
    private PapeletasInternacionService papeletasInternacionService;

    @PostMapping
    public ResponseEntity<PapeletaInternacionDto> registrarPapeletaInternacion(@RequestBody PapeletaInternacionDto papeletaInternacionDto) {
        try {
            PapeletaInternacionDto nuevaPapeleta = papeletasInternacionService.registrarPapeletaInternacion(papeletaInternacionDto);
            return new ResponseEntity<>(nuevaPapeleta, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<PapeletaInternacionDto>> obtenerTodasPapeletasInternacion() {
        try {
            List<PapeletaInternacionDto> papeletas = papeletasInternacionService.obtenerTodasPapeletasInternacion();
            return new ResponseEntity<>(papeletas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PapeletaInternacionDto> obtenerPapeletaInternacionPorId(@PathVariable Integer id) {
        try {
            PapeletaInternacionDto papeleta = papeletasInternacionService.obtenerPapeletaInternacionPorId(id);
            return new ResponseEntity<>(papeleta, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PapeletaInternacionDto> actualizarPapeletaInternacion(@PathVariable Integer id, @RequestBody PapeletaInternacionDto actualizada) {
        try {
            PapeletaInternacionDto papeletaActualizada = papeletasInternacionService.actualizarPapeletaInternacion(id, actualizada);
            return new ResponseEntity<>(papeletaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<PapeletaInternacionDto>> obtenerPapeletasInternacionDePaciente(@PathVariable int idPaciente) {
        try {
            List<PapeletaInternacionDto> papeletas = papeletasInternacionService.obtenerTodasPapeletasInternacionDePaciente(idPaciente);
            return new ResponseEntity<>(papeletas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/info-container")
    public @ResponseBody String obtenerInformacionContenedor() {
        return "microservicio historias clinicas: " + containerMetadataService.retrieveContainerMetadataInfo();
    }
    @GetMapping("/pdf")
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
}
