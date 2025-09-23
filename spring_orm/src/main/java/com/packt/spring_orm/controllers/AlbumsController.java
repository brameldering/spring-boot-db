package com.packt.spring_orm.controllers;

import java.util.List;
import java.util.Optional;

import com.packt.spring_orm.records.Album;
import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.records.TradingUser;
import com.packt.spring_orm.services.AlbumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/albums")
@RestController
public class AlbumsController {

  private AlbumService albumService;

  public AlbumsController(AlbumService albumService) {
    this.albumService = albumService;
  }

  @PostMapping("/{userId}")
  public Album buyAlbum(@PathVariable Long userId, @RequestBody String title) {
    return albumService.buyAlbum(userId, title);
  }

  @PostMapping("/{userId}/cards")
  public List<Card> buyCards(@PathVariable Long userId, @RequestBody Integer count) {
    return albumService.buyCards(userId, count);
  }

  @GetMapping("users/{userId}")
  public TradingUser getUserWithCardsAndAlbums(@PathVariable Long userId) {
    return albumService.getUserWithCardsAndAlbums(userId);
  }

  @PostMapping("/{albumId}/cards/{cardId}")
  public Card addCardToAlbum(@PathVariable Long cardId, @PathVariable Long albumId) {
    return albumService.addCardToAlbum(cardId, albumId);
  }

  @PostMapping("/{userId}/transfer/{cardId}")
  public Optional<Card> transferCard(@PathVariable Long cardId, @PathVariable Long userId) {
    return albumService.transferCard(cardId, userId);
  }

  @PostMapping("/{userId}/auto")
  public List<Card> autoAssign(@PathVariable Long userId) {
    return albumService.useAllCardAvailable(userId);
  }

  @PostMapping("/trade/{userId1}/{userId2}")
  public List<Card> tradeCardsBetweenUsers(@PathVariable Long userId1, @PathVariable Long userId2) {
    return albumService.tradeAllCards(userId1, userId2);
  }

}