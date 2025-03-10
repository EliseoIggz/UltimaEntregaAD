package com.example.ultimaentregaad;

public class Persona {
    private String nombre;
    private String trabajo;
    private int edad;

    public Persona(String nombre, String trabajo, int edad) {
        this.nombre = nombre;
        this.trabajo = trabajo;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTrabajo() { return trabajo; }
    public void setTrabajo(String trabajo) { this.trabajo = trabajo; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}

