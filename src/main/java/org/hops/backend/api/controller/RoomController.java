package org.hops.backend.api.controller;

import org.hops.backend.api.entity.Home;
import org.hops.backend.api.entity.Room;
import org.hops.backend.api.model.RoomInfo;
import org.hops.backend.api.repository.HomeRepository;
import org.hops.backend.api.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("room")
public class RoomController {
    private final HomeRepository homeRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomController(HomeRepository homeRepository, RoomRepository roomRepository) {
        this.homeRepository = homeRepository;
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<RoomInfo> createRoom(
            @RequestBody Room room,
            @RequestParam(name = "homeId") long homeId) {
        try {
            Home home = homeRepository.findById(homeId).orElse(null);
            if (null == home) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Room newRoom = roomRepository.save(new Room(home, room.getTitle()));
            return new ResponseEntity<>(
                    new RoomInfo(newRoom.getId(), newRoom.getHome().getId(), newRoom.getTitle()),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoomInfo>> getRoomsByHome(@RequestParam(name = "homeId") long homeId) {
        Home home = homeRepository.findById(homeId).orElse(null);
        if (null == home) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        List<Room> rooms = roomRepository.findAllByHome(home);
        return new ResponseEntity<>(
                rooms.stream().map(Room::convertToRoomInfo).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteHome(@PathVariable(value = "roomId") long roomId) {
        try {
            roomRepository.deleteById(roomId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
