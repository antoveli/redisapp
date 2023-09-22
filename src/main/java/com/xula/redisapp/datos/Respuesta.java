package com.xula.redisapp.datos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Respuesta
{
    @JsonProperty("codigo")
    private int codigo;
    @JsonProperty("formulario")
    private String formulario;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFormulario() {
        return formulario;
    }

    public void setFormulario(String formulario) {
        this.formulario = formulario;
    }
}
