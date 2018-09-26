package com.example.leandro.controlpensiones;

public class registro {

    int id;
    String trabajador_id;
    String pension_id;
    String servicio_id;
    String hora;
    String fecha;
    String estado;

    public registro() {

    }

    public registro(int id, String trabajador_id, String pension_id, String servicio_id, String hora, String fecha, String estado) {
        this.id = id;
        this.trabajador_id = trabajador_id;
        this.pension_id = pension_id;
        this.servicio_id = servicio_id;
        this.hora = hora;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrabajador_id() {
        return trabajador_id;
    }

    public void setTrabajador_id(String trabajador_id) {
        this.trabajador_id = trabajador_id;
    }

    public String getPension_id() {
        return pension_id;
    }

    public void setPension_id(String pension_id) {
        this.pension_id = pension_id;
    }

    public String getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(String servicio_id) {
        this.servicio_id = servicio_id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
