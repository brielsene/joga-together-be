package br.com.joga_together.service;

import br.com.joga_together.dto.GameCreateRequestDto;
import br.com.joga_together.dto.GameResponseDto;
import br.com.joga_together.exception.GameAlreadyExistsException;
import br.com.joga_together.model.Game;
import br.com.joga_together.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public GameResponseDto createGame(GameCreateRequestDto dto) {
        if(gameExists(dto.title())){
            throw new GameAlreadyExistsException("Game with title " + dto.title() + " already exists.");
        }
        Game game = gameDtoToEntity(dto);
        gameRepository.save(game);
        return gameEntityToResponseDto(game);
    }
    private Game gameDtoToEntity(GameCreateRequestDto dto) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new Game(
                null,
                dto.title(),
                dto.description(),
                LocalDate.from(dateTimeFormatter.parse(dto.releaseDate())),
                dto.developer(),
                dto.genre()
        );
    }

    private GameResponseDto gameEntityToResponseDto(Game game) {
        GameResponseDto gameResponseDto = new GameResponseDto(game.getId(), game.getTitle(), game.getDescription(),
                game.getReleaseDate().toString(), game.getDeveloper(), game.getGenre());
        return gameResponseDto;
    }

    public List<GameResponseDto>getAllGames() {
        List<Game>games = gameRepository.findAll();
        return games.stream().map(g -> new GameResponseDto(g.getId(), g.getTitle(), g.getDescription(), g.getReleaseDate().toString(), g.getDeveloper(), g.getGenre())).toList();
    }

    private boolean gameExists(String title) {
        return gameRepository.findByTitle(title).isPresent();
    }
}
