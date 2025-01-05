package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPatch;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.exception.GameVersionEmptyBody;
import com.superbleep.rvga.exception.GameVersionNotFound;
import com.superbleep.rvga.exception.GameVersionOnlyOne;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.GameVersionId;
import com.superbleep.rvga.repository.GameVersionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public GameVersionGet getById(GameVersionId id) {
        Optional<GameVersion> gameVersionOptional = gameVersionRepository.findById(id);

        if(gameVersionOptional.isPresent()) {
            GameVersion gameVersion = gameVersionOptional.get();
            return new GameVersionGet(gameVersion.getId().getId(), gameVersion.getGame(), gameVersion.getRelease(),
                    gameVersion.getNotes());
        } else
            throw new GameVersionNotFound(id);
    }

    @Transactional
    public void modifyData(GameVersionPatch newGameVersion) {
        GameVersionId id = new GameVersionId(newGameVersion.getId(), newGameVersion.getGameId());
        GameVersionGet oldGameVersionGet = this.getById(id);

        if(newGameVersion.getRelease() == null && newGameVersion.getNotes() == null)
            throw new GameVersionEmptyBody();

        if(newGameVersion.getRelease() == null)
            newGameVersion.setRelease(oldGameVersionGet.release());

        if(newGameVersion.getNotes() == null)
            newGameVersion.setNotes(oldGameVersionGet.notes());

        gameVersionRepository.modifyData(newGameVersion.getRelease(), newGameVersion.getNotes(), id);
    }

    @Transactional
    public void delete(GameVersionId id) {
        GameVersionGet gameVersionGet = this.getById(id);
        List<GameVersionGet> vers = gameService.getGameVersions(id.getGameId());

        if(vers.size() == 1 && vers.contains(gameVersionGet))
            throw new GameVersionOnlyOne(id);

        gameVersionRepository.deleteById(id);
    }
}
