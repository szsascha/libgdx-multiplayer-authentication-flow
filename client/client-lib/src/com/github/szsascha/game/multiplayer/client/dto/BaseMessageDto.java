package com.github.szsascha.game.multiplayer.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseMessageDto {

    private String operation;

    private UUID sessionId;

}
