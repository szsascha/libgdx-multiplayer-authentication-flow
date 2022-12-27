package com.github.szsascha.game.multiplayer.client.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto extends BaseMessageDto {

    private LocalDateTime timestamp;

    private String message;

    private String username;

}
