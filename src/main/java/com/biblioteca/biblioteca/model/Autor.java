package com.biblioteca.biblioteca.model;

import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDate;

@Entity
@Table(name = "autor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    private LocalDate nascimento;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 80)
    private String nacionalidade;
}