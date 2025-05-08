package com.jimmy_d.notesserver.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(of = {"id", "content"}, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notes")
public class Note extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String tag;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;
}