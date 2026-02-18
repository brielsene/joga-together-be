package br.com.joga_together.controller;

import br.com.joga_together.dto.ConfirmCodeDto;
import br.com.joga_together.dto.UserCreateRequestDto;
import br.com.joga_together.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserCreateRequestDto dto) {
        userService.creteUser(dto);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/confirm-code")
    public ResponseEntity<Void>confirmCode(@RequestBody ConfirmCodeDto confirmCodeDto){
        userService.confirmRegister(confirmCodeDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
