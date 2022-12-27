package com.github.szsascha.game.multiplayer.server.player;

import jakarta.persistence.*;
import lombok.Data;;

import java.util.UUID;

@Entity
@Data
class Player {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private byte[] password;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "player")
    private Session session;

}
