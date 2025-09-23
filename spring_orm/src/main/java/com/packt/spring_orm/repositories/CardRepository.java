package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.CardEntity;
import com.packt.spring_orm.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, Long> {

  List<CardEntity> findAllByOwnerAndAlbumIsNull(UserEntity user);

  List<CardEntity> findAllByOwnerId(Long userId);

  @Modifying
  @Query(nativeQuery = true, value = "UPDATE cards " + //
      "SET album_id = r.album_id  " + //
      "FROM " + //
      "(SELECT available.album_id, (SELECT c2.id from cards c2 where c2.owner_id=?1 AND c2.player_id = available.player_id AND c2.album_id IS NULL LIMIT 1) as card_id " + //
      "FROM " + //
      "(SELECT DISTINCT a.id as album_id, c.player_id FROM albums a CROSS JOIN cards c WHERE a.owner_id=?1 AND c.owner_id=?1 AND c.album_id IS NULL AND c.player_id NOT IN (SELECT uc.player_id from cards uc WHERE uc.album_id = a.id)) available) as r " + //
      "WHERE cards.id = r.card_id " +
      "RETURNING cards.*")
  List<CardEntity> assignCardsToUserAlbums(Long userId);

  @Modifying
  @Query(value = "UPDATE CardEntity " +
      " SET album = null, " +
      " owner= (SELECT u FROM UserEntity u WHERE u.id=?2) " +
      "WHERE id = ?1 ")
  Integer transferCard(Long cardId, Long userId);

  @Modifying
  @Query(nativeQuery = true, value = "UPDATE cards " +
      "SET owner_id=?2 " +
      " FROM (select c1.id from cards c1 where c1.owner_id=?1 and c1.album_id IS NULL AND c1.player_id IN (select p2.id from players p2 where p2.id NOT IN (SELECT c2.player_id FROM cards c2 WHERE c2.owner_id=?2)) LIMIT ?3) cards_from_user1_for_user2 " +
      "WHERE cards.id = cards_from_user1_for_user2.id " +
      "RETURNING cards.*")
  List<CardEntity> tradeCardsBetweenUsers(Long userId1, Long userId2, Integer count);

  @Query("SELECT COUNT(c) FROM CardEntity c WHERE c.owner.id = ?1 AND c.album = NULL AND c.player NOT IN (SELECT c2.player FROM CardEntity c2 WHERE c2.owner.id = ?2)")
  Integer countMatchBetweenUsers(Long userId1, Long userId2);

}
