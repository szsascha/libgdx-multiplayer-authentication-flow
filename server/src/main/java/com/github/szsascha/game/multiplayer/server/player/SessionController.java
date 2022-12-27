package com.github.szsascha.game.multiplayer.server.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/player/session")
class SessionController {

    @Autowired
    private SessionService service;

    @PostMapping
    ResponseEntity<String> login(@RequestBody PlayerDto dto) {
        final UUID id = service.createSession(dto);
        if (id != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(id.toString());
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    void logout(@PathVariable UUID id) {
        service.deleteSession(id);
    }
}
