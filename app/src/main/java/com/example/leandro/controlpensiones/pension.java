package com.example.leandro.controlpensiones;

public class pension {

    String id;
    String rsocial;
    String rut;
    String id_servicio;
    String nameservice;
    String desde;
    String hasta;

    public pension() {

    }

    public pension(String id, String rsocial, String rut, String id_servicio, String nameservice, String desde, String hasta) {
        this.id = id;
        this.rsocial = rsocial;
        this.rut = rut;
        this.id_servicio = id_servicio;
        this.nameservice = nameservice;
        this.desde = desde;
        this.hasta = hasta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRsocial() {
        return rsocial;
    }

    public void setRsocial(String rsocial) {
        this.rsocial = rsocial;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNameservice() {
        return nameservice;
    }

    public void setNameservice(String nameservice) {
        this.nameservice = nameservice;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }
}
