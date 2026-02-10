package br.com.joga_together.controller;

import br.com.joga_together.dto.GameCreateRequestDto;
import br.com.joga_together.dto.GameResponseDto;
import br.com.joga_together.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameResponseDto>createGame(@RequestBody GameCreateRequestDto dto){
        GameResponseDto game = gameService.createGame(dto);
        URI uri = URI.create("/games/" + game.id());
        return ResponseEntity.created(uri).body(game);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameResponseDto>>allGames(){
        List<GameResponseDto>games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }
}
