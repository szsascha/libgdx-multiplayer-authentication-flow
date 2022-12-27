package com.github.szsascha.game.multiplayer.server.player;

import com.github.szsascha.game.multiplayer.server.cache.session.SessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository repository;
    @Autowired
    private PlayerMapper mapper;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private SessionCache sessionCache;

    public void deleteSession(UUID uuid) {
        repository.findById(uuid).ifPresent(session -> {
            sessionCache.remove(uuid);
            repository.delete(session);
        });
    }

    public UUID createSession(PlayerDto playerDto) {
        final Player mappedPlayer = mapper.dto2Entity(playerDto);
        final Player player = playerRepository.findPlayerByNameIgnoreCase(mappedPlayer.getName());

        // Delete existing session if exists
        repository.findByPlayer(player).ifPresent(p -> deleteSession(p.getId()));

        if (Arrays.equals(player.getPassword(), mappedPlayer.getPassword())) {
            Session session = Session.builder()
                    .player(player)
                    .build();
            session = repository.save(session);
            sessionCache.put(session.getId(), player.getId(), player.getName());
            return session.getId();
        }
        return null;
    }

}
