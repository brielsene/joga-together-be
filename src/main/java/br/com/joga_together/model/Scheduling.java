package br.com.joga_together.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_schedulings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Scheduling {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
    @ManyToMany
    @JoinTable(
            name = "scheduling_users",
            joinColumns = @JoinColumn(name = "scheduling_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User>users;
}
