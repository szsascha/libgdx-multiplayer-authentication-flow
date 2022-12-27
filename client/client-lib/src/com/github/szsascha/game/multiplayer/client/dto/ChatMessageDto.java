package com.github.szsascha.game.multiplayer.client.dto;

import com.github.szsascha.game.multiplayer.client.api.BaseMessage;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto extends BaseMessage {

    private LocalDateTime timestamp;

    private String message;

    private String username;

}
