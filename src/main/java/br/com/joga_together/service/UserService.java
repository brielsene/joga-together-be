package br.com.joga_together.service;

import br.com.joga_together.dto.UserCreateRequestDto;
import br.com.joga_together.mapper.UserMapper;
import br.com.joga_together.model.User;
import br.com.joga_together.model.enums.UserStatus;
import br.com.joga_together.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserMapper userMapper, UserRepository userRepository, EmailService emailService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void creteUser(UserCreateRequestDto request) {
        User user = userMapper.toEntity(request);
        user.setUserStatus(UserStatus.INACTIVE);
        user.setCreationDate(LocalDateTime.now());
        user.setVerificationCode(generateToken());

        emailService.sendConfirmCodeRegistrer(user.getEmail(), user.getVerificationCode());
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        //TODO TERMINAR A CRIACAO DE USUARIO E TOKEN PARA CONFIRMACAO DO USUARIO
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        int n = 100_000 + random.nextInt(900_000);
        return String.valueOf(n);
    }

}


