package br.com.joga_together.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendConfirmCodeRegistrer(
            String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jogatogether@gmail.com");
        message.setTo(to);
        message.setSubject("Confirmação de Registro - Joga Together");
        message.setText("Olá, \n\n" +
                "Obrigado por se registrar no Joga Together! Para confirmar seu registro, utilize o código de confirmação abaixo:\n\n" +
                "Código de Confirmação: " + code + "\n\n" +
                "lembrando que o código expira em 15 minutos.");
        emailSender.send(message);
    }
}
