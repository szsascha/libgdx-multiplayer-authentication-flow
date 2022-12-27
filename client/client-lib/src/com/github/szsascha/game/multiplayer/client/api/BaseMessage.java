package com.github.szsascha.game.multiplayer.client.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseMessage implements Message {

    private String operation;

    private UUID sessionId;

}
