package com.packt.spring_mdb.controller;

import com.packt.spring_mdb.entities.TradeRequest;
import com.packt.spring_mdb.service.TradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trading")
public class TradingController {
  private final TradingService tradingService;

  public TradingController(TradingService tradingService) {
    this.tradingService = tradingService;
  }

  @PostMapping("{cardId}")
  public ResponseEntity<String> Trade (@PathVariable String cardId, @RequestBody TradeRequest tradeRequest) {
    if (tradingService.exchangeCard(cardId, tradeRequest.newOwnerId(), tradeRequest.price())) {
      return ResponseEntity.ok("Card exchanged");
    } else {
      return ResponseEntity.status(409).body("Someone achieved the card before you");
    }
  }
}
