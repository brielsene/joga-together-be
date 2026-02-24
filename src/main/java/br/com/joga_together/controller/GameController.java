package br.com.joga_together.controller;

import br.com.joga_together.dto.ErrorResponseDto;
import br.com.joga_together.dto.GameCreateRequestDto;
import br.com.joga_together.dto.GameResponseDto;
import br.com.joga_together.service.GameService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody GameCreateRequestDto dto){
        GameResponseDto game = gameService.createGame(dto);
        URI uri = URI.create("/games/" + game.id());
        return ResponseEntity.created(uri).body(game);
    }

    @GetMapping("/all")
    @RateLimiter(name = "games", fallbackMethod = "fallbackGetAllGames")
    public ResponseEntity<List<GameResponseDto>> allGames(){
        List<GameResponseDto>games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    public ResponseEntity<ErrorResponseDto>fallbackGetAllGames(RequestNotPermitted e){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(LocalDateTime.now().toString(), 429, "ManyRequests", e.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponseDto);
    }
}
