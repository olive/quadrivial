package in.dogue.quadrivial.graphics

import in.dogue.antiqua.graphics.{Tile, TileRenderer, Rect}
import com.deweyvm.gleany.graphics.Color
import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._

import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.data.Recti
import in.dogue.antiqua.geometry.{Line, Quad}
import in.dogue.quadrivial.world.Bar

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
    val bTile = CP437.█.mkTile(Color.Black, Color.White)
    Spinner(cols, rows, rect, 0, speed, r1, r2, bTile)
  }
}

case class Spinner(cols:Int, rows:Int, rect:Recti, a:Double, speed:Double, r1:Rect, r2:Rect, bTile:Tile) {
  def update = this
  val angle = a// + math.Pi/4
  def setAngle(ang:Double) = copy(a=ang)
  val p0 = rect.x @@ rect.y
  val p1 = p0 |+ rect.width
  val p2 = p0 |+| ((rect.width, rect.height))
  val p3 = p0 +| rect.height
  val center = (cols/2, rows/2)
  val pp0 = Bar.rotatePoint(center, angle, p0)
  val pp1 = Bar.rotatePoint(center, angle, p1)
  val pp2 = Bar.rotatePoint(center, angle, p2)
  val pp3 = Bar.rotatePoint(center, angle, p3)
  private def rectToCells = {
    new Quad(pp0, pp1, pp2, pp3).fill
  }

  def draw(tr:TileRenderer):TileRenderer = {

    val rcells = rectToCells.toSet
    val map = collection.mutable.Set[Cell]()
    def draw1(c:Cell):Boolean = {
      val a = math.atan2(c.y - rows/2, c.x - cols/2)
      val angle = (this.a + math.Pi/4) %% math.Pi
      val a1=  angle - math.Pi
      val a2 = angle + math.Pi
      val a3 = angle - 3*math.Pi
      val b1 = a1 > a && a1 < a + math.Pi/2
      val b2 = a2 > a && a2 < a + math.Pi/2
      val b3 = a1 > a - math.Pi && a1 < a - math.Pi/2
      val b4 = a3 > a - math.Pi && a3 < a - math.Pi/2
      val b = rcells.contains(c) || (b1 || b2 || b3 || b4)
      if (b) {
        map += c
      }
      b
    }

    def draw2(c:Cell) = !map.contains(c)

    tr <+< r1.filterDraw((0,0))(draw1) <+< r2.filterDraw((0,0))(draw2) <+< drawLines
  }

  private def drawLines(tr:TileRenderer):TileRenderer = {
    val ls = (Line.bresenhamTup(pp0, pp1)
           ++ Line.bresenhamTup(pp1, pp2)
           ++ Line.bresenhamTup(pp2, pp3)
           ++ Line.bresenhamTup(pp3, pp0))
    tr <++ ls.map { case p => (p, bTile)}
  }
}
