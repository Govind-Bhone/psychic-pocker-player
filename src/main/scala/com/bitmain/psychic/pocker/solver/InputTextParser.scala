package com.bitmain.psychic.pocker.solver

import java.nio.file.{Files, Paths, StandardOpenOption}

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}
import com.bitmain.psychic.pocker.solver.Card.Face
import com.bitmain.psychic.pocker.solver.Card.Suit

class InputTextParser(filePath: Option[String]) {

  import InputTextParser._

  val source = Source.fromInputStream(inputstream)

  /* this method check for the filePath if it is null then use the default file for creating
   * inputstream else get the path of the provided file to create inputstream
   */
  private def inputstream: java.io.InputStream = if (filePath == None) this.getClass().getClassLoader().getResourceAsStream("input.txt")
  else
    Files.newInputStream(Paths.get(filePath.get), StandardOpenOption.READ)

  /* read the all lines from file at a time recommended ror less lines only  */
  private def scan(source: BufferedSource): List[String] =source.getLines().toList

  private def filterPredicate(line: String) = if (line == null || line.split(" ").length != 10 ) {
    println(s"Input present in file not matching the expected criteria... Skipping line=$line")
    false
  } else true

  private def mapFunction(line: String): List[Card] = {
    val splits = line.split(" ").toList
    splits.map(split => Card(suits(split.substring(1, 2)), faces(split.substring(0, 1))))
  }

  def parseLines():List[List[Card]] = Try {
    scan(source).filter(filterPredicate).map(mapFunction)
  } match {
    case Success(result)=>result
    case Failure(ex) => throw NonParsableFileFormatException(s"Exception occured while parsing file input , reason = $ex")
  }
}

object InputTextParser {

  val suits = scala.collection.immutable.HashMap[String, Suit.Value](
    "C" -> Suit.CLUBS,
    "D" -> Suit.DIAMONDS,
    "H" -> Suit.HEARTS,
    "S" -> Suit.SPADES
  )

  val faces = scala.collection.immutable.HashMap[String, Face.Value](
    "A" -> Face.ACE,
    "2" -> Face.TWO,
    "3" -> Face.THREE,
    "4" -> Face.FOUR,
    "5" -> Face.FIVE,
    "6" -> Face.SIX,
    "7" -> Face.SEVEN,
    "8" -> Face.EIGHT,
    "9" -> Face.NINE,
    "T" -> Face.TEN,
    "J" -> Face.JACK,
    "Q" -> Face.QUEEN,
    "K" -> Face.KING
  )

  def apply(filePath: Option[String]) = new InputTextParser(filePath)
}
