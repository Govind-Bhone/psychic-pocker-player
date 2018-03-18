package com.bitmain.psychic.pocker.solver

class PsychicPockerPlayer(filePath: Option[String]) {
  val inputParser = InputTextParser(filePath)
  val bestRankPicker = new BestRankPicker(new HandRankCategory)

  import java.text.MessageFormat

  def solve(): Unit = {
    val games = inputParser.parseLines()
    for (game <- games) {
      val hand = game.slice(0, 5)
      val deck = game.slice(5, 10)
      val bestScore = bestRankPicker.pickBestScore(hand, deck)
      println(MessageFormat.format("Hand: {0} Deck: {1} Best hand: {2}", format(hand), format(deck), bestScore))
    }
  }

  private def format(hand: List[Card]) = hand.mkString(" ")
}

object PsychicPockerPlayer {
  def apply(filePath: Option[String]) = new PsychicPockerPlayer(filePath)
}
