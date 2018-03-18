package com.bitmain.psychic.pocker.solver

class BestRankPicker(val rankPicker: HandRankCategory) {

  import HandRankCategory._

  def pickBestScore(hand: List[Card], deck: List[Card]): HandRank.Value = {
    var maxScore = HandRank.HIGHEST_CARD
    generateAllPossibleGames(hand, deck).map(rankPicker.calcScore).foldLeft(maxScore) { (acc, ele) => if (ele.id < acc.id) ele else acc }
  }

  private def generateAllPossibleGames(hand: List[Card], deck: List[Card]) = generateHandCombinatory(hand).map(currentHand => pickFromDeck(currentHand, deck))

  private def pickFromDeck(currentHand: List[Card], deck: List[Card]) = currentHand ::: (deck.slice(0, 5 - currentHand.length))

  private def generateHandCombinatory(hand: List[Card]) = {
    var ret = List[List[Card]]()
    for (i <- 0 until 5) {
      val generator = new CombinationGenerator(5, i)
      while (generator.hasMore) {
        var list = List[Card]()
        for (index <- generator.getNext()) {
          list = list :+ hand(index)
        }
        ret = ret :+ list
      }
    }
    ret
  }
}
