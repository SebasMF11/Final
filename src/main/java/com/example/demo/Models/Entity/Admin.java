package com.example.demo.Models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Admins")
public class Admin {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String contraseña;
    private String correo;

    public Admin(String correo, String contraseña) {
        this.contraseña = contraseña;
        this.correo = correo;
    }

    public Admin() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }  
}
