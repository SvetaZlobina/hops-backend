package org.hops.backend.api.entity;

import org.hops.backend.api.model.HomeInfo;
import org.hops.backend.api.model.RoomInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "room", schema = "hops")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home = null;

    @Column(name = "title")
    private String title = null;

    public Room(Home home, String title) {
        this.home = home;
        this.title = title;
    }

    public Room() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RoomInfo convertToRoomInfo() {
        return new RoomInfo(id, home.getId(), title);
    }
}
