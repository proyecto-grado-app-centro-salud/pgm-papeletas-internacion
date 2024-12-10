package com.example.microservicio_papeletas_internacion.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

@Service
public class ConvertirTiposDatosService {
    public Date convertirStringADate(String fecha) {
        if (fecha == null) {
            return null;
        }
        Date fechaDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         try {
            fechaDate = format.parse(fecha);
            return fechaDate;

        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al convertir la fecha.");
        }
    }
}
