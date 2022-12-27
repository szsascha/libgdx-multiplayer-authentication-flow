package com.github.szsascha.game.multiplayer.server.player;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface PlayerRepository extends CrudRepository<Player, UUID> {

    Player findPlayerByNameIgnoreCase(String name);

}
