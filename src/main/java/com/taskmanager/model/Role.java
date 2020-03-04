package com.taskmanager.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name="security_role")
public class Role  implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name")
    private String roleName;

    @Column(name="description")
    private String description;

    @Override
    public String getAuthority() {
        return roleName;
    }

}