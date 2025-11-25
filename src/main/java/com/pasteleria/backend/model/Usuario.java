package com.pasteleria.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO", uniqueConstraints = {
    @UniqueConstraint(columnNames = "correo_usuario")
})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    
    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "rol_usuario", nullable = false)
    private String rol;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(name = "correo_usuario", nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre_usuario", nullable = false)
    private String nombre;
    
    @JsonIgnore
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "contrasena_usuario", nullable = false)
    private String contrasena;
    
    @Column(name = "fecha_nacimiento_usuario")
    private LocalDate fechaNacimiento;
    
    @Column(name = "cupon_usuario")
    private String cupon;
    
    @Column(name = "cuenta_activa")
    @Builder.Default
    private Boolean cuentaActiva = true;
    
    @Column(name = "fecha_creacion")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return contrasena;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return cuentaActiva;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}
