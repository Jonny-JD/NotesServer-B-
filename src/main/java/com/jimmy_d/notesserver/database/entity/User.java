package com.jimmy_d.notesserver.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(of = {"id", "username"}, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;
}
