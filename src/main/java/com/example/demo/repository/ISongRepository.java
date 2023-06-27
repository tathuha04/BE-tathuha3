package com.example.demo.repository;

import com.example.demo.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ISongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByCategoryId(Long categoryId);


    ///findAllByNameContaining  : tìm kiem gan dung : sql like %
    Page<Song> findAllByNameContaining(String name, Pageable pageable);

    @Query("SELECT s FROM Song AS s ORDER BY rand()")
    List<Song> getSongByLit3();

    List<Song> findTop5ByOrderByTimeDesc();
}
