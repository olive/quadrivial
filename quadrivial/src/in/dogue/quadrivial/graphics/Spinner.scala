package in.dogue.quadrivial.graphics

import in.dogue.antiqua.graphics.{TileRenderer, Rect}
import com.deweyvm.gleany.graphics.Color
import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._

import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.data.Recti

object Spinner {
  def create(cols:Int, rows:Int, rect:Recti, speed:Double, r:Random) = {
    def mkTile1(r:Random) = {
      val bg = Color.Red.dim(5 + r.nextDouble)
      val fg = Color.Brown.mix(Color.Red, r.nextDouble).dim(5 + r.nextDouble)
      val code = Vector(CP437.a, CP437.b, CP437.c).randomR(r)
      code.mkTile(bg, fg)
    }

    def mkTile2(r:Random) = {
      val bg = Color.Orange.dim(5 + r.nextDouble)
      val fg = Color.Brown.mix(Color.Orange, r.nextDouble).dim(5 + r.nextDouble)
      val code = Vector(CP437.a, CP437.b, CP437.c).randomR(r)
      code.mkTile(bg, fg)
    }

    val r1 = Rect.createTextured(cols, rows, mkTile1, r)
    val r2 = Rect.createTextured(cols, rows, mkTile2, r)
    Spinner(cols, rows, rect, speed, r1, r2, 0)
  }
}

case class Spinner(cols:Int, rows:Int, rect:Recti, speed:Double, r1:Rect, r2:Rect, angle:Double) {
  def update = copy(angle=(angle+speed) % (2*math.Pi))
  def draw(tr:TileRenderer):TileRenderer = {


    def draw1(c:Cell):Boolean = {
      val a = math.atan2(c.y - rows/2, c.x - cols/2)
      val a1=  angle - math.Pi
      val a2 = angle + math.Pi
      val a3 = angle - 3*math.Pi
      val b1 = a1 > a && a1 < a + math.Pi/2
      val b2 = a2 > a && a2 < a + math.Pi/2
      val b3 = a1 > a - math.Pi && a1 < a - math.Pi/2
      val b4 = a3 > a - math.Pi && a3 < a - math.Pi/2
      !rect.contains(c) && (b1 || b2 || b3 || b4)
    }

    def draw2(c:Cell) = !draw1(c)

    tr <+< r1.filterDraw((0,0))(draw1) <+< r2.filterDraw((0,0))(draw2)
  }
}
