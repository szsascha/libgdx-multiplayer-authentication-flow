package com.github.szsascha.game.multiplayer.server.player;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Session {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    private Player player;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
