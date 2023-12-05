package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public App(String name) {
        this.name = name;
    }
}
