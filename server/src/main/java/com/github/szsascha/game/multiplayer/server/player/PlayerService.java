package com.github.szsascha.game.multiplayer.server.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository repository;
    @Autowired
    private PlayerMapper mapper;
    @Autowired
    private SessionService sessionService;

    public void createPlayer(PlayerDto playerDto) {
        final Player player = mapper.dto2Entity(playerDto);
        repository.save(player);
    }

    public void deletePlayer(UUID uuid) {
        repository.findById(uuid).ifPresent(repository::delete);
    }

}
