package com.bitmain.psychic.pocker.solver

import com.bitmain.psychic.pocker.solver.Card.{Face, Suit}
import com.bitmain.psychic.pocker.solver.Card.Face.Face
import com.bitmain.psychic.pocker.solver.Card.Suit.Suit

class Card(val suit: Suit, val face: Face) {
  override def toString: String = return s"${faceInitials(face)}${suitInitials(suit)}"

  def suitInitials(suit: Suit.Value): String = suit match {
    case Suit.CLUBS => "C"
    case Suit.DIAMONDS => "D"
    case Suit.HEARTS => "H"
    case Suit.SPADES => "S"
  }

  def faceInitials(face: Face.Value): String = face match {
    case Face.TWO => "2"
    case Face.THREE => "3"
    case Face.FOUR => "4"
    case Face.FIVE => "5"
    case Face.SIX => "6"
    case Face.SEVEN => "7"
    case Face.EIGHT => "8"
    case Face.NINE => "9"
    case Face.TEN => "T"
    case Face.JACK => "J"
    case Face.QUEEN => "Q"
    case Face.KING => "K"
    case Face.ACE => "A"
  }
}

object Card {
  val SUIT_MASK: Int = 122880
  val FACE_MASK: Int = 8191

  object Suit extends Enumeration {
    type Suit = Value
    val CLUBS, DIAMONDS, HEARTS, SPADES = Value
  }

  object Face extends Enumeration {
    type Face = Value
    val TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE = Value
  }

  def apply(suit: Suit, face: Face) = new Card(suit, face)
}

