package com.example.demo.controller;

import com.example.demo.dto.request.SongDTO;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.Song;
import com.example.demo.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping("/page")
    public ResponseEntity<?> pageSong(Pageable pageable) {
        return new ResponseEntity<>(songService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> listSong() {
        return new ResponseEntity<>(songService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSong(@Valid @RequestBody SongDTO songDTO) {
        Song song = new Song(songDTO.getName(), songDTO.getAvatar(), songDTO.getLyrics(), songDTO.getURL(), songDTO.getCategory(), songDTO.getSingerList());
        songService.save(song);
        return new ResponseEntity<>(new ResponMessage("create_success"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable Long id) {
        Optional<Song> song = songService.findById(id);
        if (!song.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable Long id, @RequestBody SongDTO songDTO) {
        Optional<Song> optionalSong = songService.findById(id);
        if (!optionalSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Song existingSong = optionalSong.get();

        if (songDTO.equals(existingSong)) {
            return new ResponseEntity<>(new ResponMessage("no_change"), HttpStatus.OK);
        }

        if (!songDTO.getAvatar().equals(existingSong.getAvatar())) {
            existingSong.setAvatar(songDTO.getAvatar());
        }
        if (!songDTO.getLyrics().equals(existingSong.getLyrics())) {
            existingSong.setLyrics(songDTO.getLyrics());
        }
        if (!songDTO.getName().equals(existingSong.getName())) {
            existingSong.setName(songDTO.getName());
        }
        if (!songDTO.getURL().equals(existingSong.getURL())) {
            existingSong.setURL(songDTO.getURL());
        }
        if (!songDTO.getCategory().equals(existingSong.getCategory())) {
            existingSong.setCategory(songDTO.getCategory());
        }
        if (!songDTO.getSingerList().equals(existingSong.getSingerList())) {
            existingSong.setSingerList(songDTO.getSingerList());
        }
        songService.save(existingSong);
        return new ResponseEntity<>(new ResponMessage("update_success"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        Optional<Song> song = songService.findById(id);
        if (!song.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        songService.deleteById(id);
        return new ResponseEntity<>(new ResponMessage("delete_success"), HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getSongByName(@PathVariable String name, Pageable pageable) {
        Page<Song> songPage = songService.findAllByNameContaining(name, pageable);
        return new ResponseEntity<>(songPage, HttpStatus.OK);
    }

    @GetMapping("/randomSong")
    public ResponseEntity<?> getRandomSong() {
        List<Song> songList = songService.getSongByLit3();
        List<Song> listRandom = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            listRandom.add(songList.get(i));
        }
        return new ResponseEntity<>(listRandom, HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<?> getSongPlaying(@PathVariable Long id) {
        ///Optional doi tuong null sex khong chet ctrinh
        Optional<Song> song = songService.findById(id);
        if (!song.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        song.get().setTime(song.get().getTime() + 1);
        songService.saveView(song.get());
        return new ResponseEntity<>(new ResponMessage("view+1"), HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<?> topTrendingSong() {
        List<Song> songList = songService.findTop5ByOrderByTimeDesc();
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }
}
