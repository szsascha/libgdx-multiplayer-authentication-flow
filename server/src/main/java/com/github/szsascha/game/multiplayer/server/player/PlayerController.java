package com.github.szsascha.game.multiplayer.server.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/player")
class PlayerController {

    @Autowired
    private PlayerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody PlayerDto dto) {
        service.createPlayer(dto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) {
        service.deletePlayer(id);
    }

}
