package com.github.szsascha.game.multiplayer.server.player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface SessionRepository extends CrudRepository<Session, UUID> {

    Optional<Session> findByPlayer(Player player);

}
