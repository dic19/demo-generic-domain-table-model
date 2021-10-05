/*
 * Copyright (C) 2014 Delcio Amarillo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.javadeepcafe.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Delcio Amarillo
 */
public class Fabricante {
    
    private final String nombre;
    private final Date fechaFundación;
    private final List<Procesador> procesadores;

    public Fabricante(String nombre, Date fechaFundación) {
        this.nombre = nombre;
        this.fechaFundación = fechaFundación;
        this.procesadores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFechaFundación() {
        return fechaFundación;
    }

    public List<Procesador> getProcesadores() {
        return procesadores;
    }

    public void addProcesador(Procesador p) {
        procesadores.add(p);
    }
        
    public void removeProcesador(Procesador p) {
        procesadores.remove(p);
    }
    
}
