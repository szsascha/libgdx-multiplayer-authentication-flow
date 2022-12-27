package com.github.szsascha.game.multiplayer.server.player;

import lombok.extern.java.Log;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log
@Mapper(componentModel = "spring")
abstract class PlayerMapper {

    @Mapping(source = "username", target = "name")
    abstract Player dto2Entity(PlayerDto dto);

    @AfterMapping
    protected void afterMapping(PlayerDto dto, @MappingTarget Player player) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((dto.passwordHash() + player.getId()).getBytes());
            player.setPassword(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getMessage());
        }
    }

}
