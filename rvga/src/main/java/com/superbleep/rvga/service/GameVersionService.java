package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.repository.GameVersionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameVersionService {
    private final GameVersionRepository gameVersionRepository;
    private final GameService gameService;

    public GameVersionService(GameVersionRepository gameVersionRepository, GameService gameService) {
        this.gameVersionRepository = gameVersionRepository;
        this.gameService = gameService;
    }

    @Transactional
    public GameVersion create(GameVersionPost gameVersionPost, Game createdGame) {
        Game game;

        if(createdGame != null)
            game = createdGame;
        else
            game = gameService.getById(gameVersionPost.gameId());

        GameVersion newGameVersion = new GameVersion(gameVersionPost, game);

        return gameVersionRepository.save(newGameVersion);
    }

    public List<GameVersionGet> getAll() {
        List<GameVersionGet> res = new java.util.ArrayList<>(List.of());
        List<GameVersion> repo = gameVersionRepository.findAll();

        repo.forEach(x -> res.add(new GameVersionGet(x.getId().getId(), x.getGame(), x.getRelease(), x.getNotes())));

        return res;
    }
}
