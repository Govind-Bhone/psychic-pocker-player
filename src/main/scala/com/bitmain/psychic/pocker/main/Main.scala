package com.bitmain.psychic.pocker.main

import com.bitmain.psychic.pocker.solver.PsychicPockerPlayer

object Main {
  def main(args: Array[String]): Unit = {
    /*
     * this is the startup file for the Application
     * it accept the filePath as program argument from the console
     * if not provided explicitly then
     * default file get processed by the program (src/main/resources/input.txt)
     */
    val filePath = if (args.length != 0) Some(args(0)) else None
    val processor = PsychicPockerPlayer(filePath).solve()
  }
}
