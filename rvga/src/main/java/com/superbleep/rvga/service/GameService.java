package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.GamePatch;
import com.superbleep.rvga.dto.GamePost;
import com.superbleep.rvga.exception.GameEmptyBody;
import com.superbleep.rvga.exception.GameIdenticalFound;
import com.superbleep.rvga.exception.GameNotFound;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlatformService platformService;

    public GameService(GameRepository gameRepository, PlatformService platformService) {
        this.gameRepository = gameRepository;
        this.platformService = platformService;
    }

    @Transactional
    public Game create(GamePost gamePost) {
        Platform platform = platformService.getById(gamePost.platformId());

        List<Game> games = gameRepository.findAllByTitleWithPlatform(gamePost.title());

        for (Game game : games)
            if (game.getPlatform().getId() == gamePost.platformId())
                throw new GameIdenticalFound();

        Game newGame = new Game(gamePost, platform);

        return gameRepository.save(newGame);
    }

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Game getById(long id) {
        Optional<Game> gameOptional = gameRepository.findById(id);

        if(gameOptional.isPresent())
            return gameOptional.get();
        else
            throw new GameNotFound(id);
    }

    public Game getByIdFull(long id) {
        Optional<Game> gameOptional = gameRepository.findByIdWithPlatform(id);

        if(gameOptional.isPresent())
            return gameOptional.get();
        else
            throw new GameNotFound(id);
    }

    @Transactional
    public void modifyData(GamePatch newGame, long id) {
        Game oldGame = this.getById(id);
        Platform newPlatform;

        if(newGame.getTitle() == null && newGame.getDeveloper() == null && newGame.getPublisher() == null &&
            newGame.getPlatformId() == null)
            throw new GameEmptyBody();

        if(newGame.getTitle() == null)
            newGame.setTitle(oldGame.getTitle());

        if(newGame.getPlatformId() == null)
            newPlatform = oldGame.getPlatform();
        else {
            Long newPlatformId = newGame.getPlatformId();

            List<Game> games = gameRepository.findAllByTitleWithPlatform(newGame.getTitle());

            for (Game game : games)
                if (game.getPlatform().getId() == newPlatformId)
                    throw new GameIdenticalFound();

            newPlatform = this.platformService.getById(newPlatformId);
        }

        if(newGame.getDeveloper() == null)
            newGame.setDeveloper(oldGame.getDeveloper());

        if(newGame.getPublisher() == null)
            newGame.setPublisher(oldGame.getPublisher());

        if(newGame.getGenre() == null)
            newGame.setGenre(oldGame.getGenre());

        gameRepository.modifyData(newGame.getTitle(), newGame.getDeveloper(), newGame.getPublisher(), newPlatform,
                newGame.getGenre(), id);
    }

    @Transactional
    public void delete(long id) {
        this.getById(id);

        gameRepository.deleteById(id);
    }
}
