package br.com.joga_together.model;

import br.com.joga_together.model.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    @CPF
    @Column(unique = true)
    private String cpf;
    @Email
    @Column(unique = true)
    private String email;
    private LocalDate bornDate;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @ManyToMany(mappedBy = "users")
    public Set<Scheduling>schedulings;

    private String verificationCode;
    private LocalDateTime codeExpiresAt;

    private LocalDateTime creationDate;
}
