package com.bitmain.psychic.pocker.solver

class CombinationGenerator(var n: Int, var r: Int) {
  private var indexes = new Array[Int](r)
  private var nFact = getFact(n)
  private var rFact = getFact(r)
  private var nMinusRFact = getFact(n - r)
  private var total: BigInt = nFact / (rFact * nMinusRFact)
  private var numLeft: BigInt = 0
  reset()


  def reset() ={
    indexes.zipWithIndex.map(f=>indexes.update(f._2,f._2))
    numLeft = total
  }

  def getNumLeft = numLeft

  def hasMore = numLeft.compare(0) == 1

  def getTotal() = total

  def getFact(n: Int): BigInt = {
    def loop(acc:BigInt,n:Int):BigInt= if ( n == 0) acc else loop(acc*n,n-1)
    loop(1,n)
  }

  def getNext():Array[Int] = {
    if (numLeft.equals(total)) {
      numLeft = numLeft - 1
      return indexes
    }
    var i = r - 1
    while (indexes(i) == n - r + i) {
      i -= 1
    }
    indexes(i) = indexes(i) + 1
    for (j <- i + 1 until r by 1) {
      indexes(j) = indexes(i) + j - i
    }
    numLeft = numLeft - 1
    indexes
  }
}
