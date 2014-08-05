package in.dogue.quadrivial.world

import in.dogue.antiqua.data.Direction
import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.quadrivial.Quadrivial
import Quadrivial._

object Pattern {
  def create(start:Int)(vs:Map[Int, Seq[Direction]]) = {
    val max = vs.keys.max
    Pattern(start, max, vs)
  }

  def spiral(v:Vector[Direction])(times:Int, spacing:Int)(r:Random)(start:Int): Pattern = {
    val b = r.nextBoolean
    def rot(d:Direction)(i:Int) = {
      b.select(d.rotRight(i), d.rotLeft(i))
    }
    val map = (for (i <- 0 until times) yield {
      val vs = v.map{ d => rot(d)(i)}
      (i*spacing) -> vs
    }).toMap
    create(start)(map)
  }

  def alternate(vecs:Vector[Vector[Direction]])(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val map = (for (i <- 0 until times) yield {
      val vec = vecs(i % vecs.length)
      (i*spacing) -> vec
    }).toMap
    create(start)(map)
  }

  def hook(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val seed = Direction.All.randomR(r)
    val first = Vector(seed, seed.rotRight1)
    val second = Vector(seed.rotLeft1, seed.rotLeft1.rotLeft1)
    alternate(Vector(first, second))(times, spacing)(r)(start)
  }

  def reversal(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val seed = Direction.All.randomR(r)
    val first = Vector(seed.rotLeft1, seed, seed.rotRight1)
    val second = Vector(seed.rotLeft1, seed.opposite, seed.rotRight1)
    alternate(Vector(first, second))(times, spacing)(r)(start)
  }

  def hookSpiral(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val seed = Direction.All.randomR(r)
    val first = Vector(seed, seed.rotRight1)
    spiral(first)(times, spacing)(r)(start)
  }

  def packedSpiral(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val seed = Direction.All.randomR(r)
    val first = Vector(seed.rotLeft1, seed, seed.rotRight1)
    spiral(first)(times, spacing)(r)(start)
  }

  def looseSpiral(times:Int, spacing:Int)(r:Random)(start:Int):Pattern = {
    val seed = Direction.All.randomR(r)
    val first = Vector(seed)
    spiral(first)(times, spacing)(r)(start)
  }

  def random(r:Random): (Int) => Pattern = {
    val rep = 6
    Vector(
      hook(rep, 20) _,
      reversal(rep, 30) _,
      hookSpiral(rep, 20) _,
      packedSpiral(rep, 30) _,
      looseSpiral(rep*4, 8) _
    ).randomR(r)(r)
  }

}

case class Pattern private (start:Int, duration:Int, map:Map[Int, Seq[Direction]]) {
  def isDone(i:Int) =  (i - start) > duration
  def update(i:Int):Seq[Direction] = {
    val o:Option[Seq[Direction]] = map.get(i - start)
    o.toSeq.flatten
  }

  def transform(r:Random) = {
    Pattern.random(r)(start + duration + 60)
  }
}
