package com.bitmain.psychic.pocker.solver

import java.util

import com.bitmain.psychic.pocker.solver.Card.{Face, Suit}


class HandRankCategory {
  import HandRankCategory._

  private def suitValue(suit: Suit.Value): Int = {
    suit match {
      case Suit.CLUBS => 65536
      case Suit.DIAMONDS => 32768
      case Suit.HEARTS => 16384
      case Suit.SPADES => 8192
    }
  }

  private def faceValue(face: Face.Value): Int = {
    face match {
      case Face.TWO => 4096
      case Face.THREE => 2048
      case Face.FOUR => 1024
      case Face.FIVE => 512
      case Face.SIX => 256
      case Face.SEVEN => 128
      case Face.EIGHT => 64
      case Face.NINE => 32
      case Face.TEN => 16
      case Face.JACK => 8
      case Face.QUEEN => 4
      case Face.KING => 2
      case Face.ACE => 1
    }
  }

  private def asBits(card: Card): Int = {
    suitValue(card.suit) + faceValue(card.face)
  }


  private def numberOfEnabledBits(number: Int): Int = {
    def loop(acc: Int, number: Int): Int = {
      number match {
        case n if n == 0 => acc
        case _ => if ((number & 1) == 1) {
          loop(acc + 1, number >> 1)
        } else {
          loop(acc, number >> 1)
        }
      }
    }

    loop(0, number)
  }

  private def generateFrequencyMap(cardsAsBits:List[Int])= {
    val frequencyMap = new scala.collection.mutable.HashMap[Int, Int]()
    for(i<-0 until cardsAsBits.length ){
      val cardValue = cardsAsBits(i) & Card.FACE_MASK
      val occuranceValue = if(frequencyMap.contains(cardValue)) frequencyMap(cardValue) else 0
      frequencyMap.put(cardValue,occuranceValue+1)
    }
    frequencyMap
  }


  private def straightOrFlushOrHighCard(oredFace: Int, cardsAsBits: List[Int]) = {
    val isStraight = util.Arrays.binarySearch(possibleStraights, oredFace) >= 0
    val isFlush = cardsAsBits.foldLeft(Card.SUIT_MASK){(acc,ele)=> acc & ele}  != 0
    if (isStraight && isFlush) HandRank.STRAIGHT_FLUSH
    else if (isStraight) HandRank.STRAIGHT
    else if (isFlush) HandRank.FLUSH
    else HandRank.HIGHEST_CARD
  }

  private def fullHouseOrFourOfAKind(cardsAsBits: List[Int]) = if (generateFrequencyMap(cardsAsBits).exists(_._2==4))
    HandRank.FOUR_OF_A_KIND
  else
    HandRank.FULL_HOUSE

  private def twoPairsOrThreeOfAKind(cardsAsBits: List[Int]) = if (generateFrequencyMap(cardsAsBits).exists(_._2==3))
    HandRank.THREE_OF_A_KIND
  else
    HandRank.TWO_PAIRS

  def calcScore(hand: List[Card]):HandRank.Value= {
    val cardsAsBits = hand.map(asBits)
    val oredFace = cardsAsBits.reduce((acc, current) => acc | current) & (Card.FACE_MASK)
    numberOfEnabledBits(oredFace) match {
      case 5 =>
        return straightOrFlushOrHighCard(oredFace, cardsAsBits)
      case 4 =>
        return HandRank.ONE_PAIR
      case 3 =>
        return twoPairsOrThreeOfAKind(cardsAsBits)
      case 2 =>
        return fullHouseOrFourOfAKind(cardsAsBits)
      case 1 =>
        return HandRank.FOUR_OF_A_KIND
      case a @ _ =>
        throw new RuntimeException(s"Invalid card masks ${a}")
    }
  }
}


object HandRankCategory {

  object HandRank extends Enumeration {
    type HandRank = Value
    val STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIRS, ONE_PAIR, HIGHEST_CARD = Value
  }

  /**
    * Face masks to identify all possible straights. Is sorted so is possible
    * to implement a bynary search. Note: value 31 represent a high-end
    * straight (10, J, Q, K, Ace) and value 7681 represents low-end straight
    * (Ace, 2, 3, 4, 5).
    */
  val possibleStraights = Array[Int](31, 62, 124, 248, 496, 992, 1984, 3968, 7681, 7936)

  def apply()= new HandRankCategory
}
