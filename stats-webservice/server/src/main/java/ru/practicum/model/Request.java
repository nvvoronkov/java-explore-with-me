package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @NotNull
    private String uri;

    @NotNull
    private String ip;

    @NotNull
    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    public Request(Long id, String uri, String ip, LocalDateTime timestamp, Long appId, String name) {
        this.id = id;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
        this.app = new App(appId, name);
    }
}
