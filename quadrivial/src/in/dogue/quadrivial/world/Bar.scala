package in.dogue.quadrivial.world

import in.dogue.antiqua.data.{CP437, Direction}
import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.antiqua.geometry.{Quad, Line}
object Bar {
  def create(cols:Int, rows:Int, d:Direction) = {
    val tile = CP437.█.mkTile(Color.Black, Color.White)
    Bar(cols, rows, d, tile, 0)
  }
  def rotatePoint(center:Cell, angle:Double, p:Cell) = {
    val s = math.sin(angle)
    val c = math.cos(angle)

    val px = p.x - center.x
    val py = p.y - center.y

    val xnew = px * c - py * s
    val ynew = px * s + py * c

    val ppx = xnew + center.x
    val ppy = ynew + center.y
    (ppx, ppy).map{k => math.round(k).toInt}
  }
}




case class Bar private (cols:Int, rows:Int, d:Direction, tile:Tile, t:Int) {
  def update = copy(t=t+1)
  private val scale=2
  private def getTRaw(t:Int) = t/scale
  def getT = getTRaw(t)
  val xStart = getT
  val xEnd = cols - getT
  val isDone = xEnd < xStart
  val pi = math.Pi
  def inAngle(a:Double) = d match {
    case Direction.Down => a >= pi/4 && a < 3*pi/4
    case Direction.Left => a >= 3*pi/4 && a < 5*pi/4
    case Direction.Up => a >= 5*pi/4 && a < 7*pi/4
    case Direction.Right => a >= 7*pi/4 || a < pi/4

  }

  def mapi(offset:Double)(i:Int, t:Int) = {
    def get = getTRaw(t)
    val p = d match {
      case Direction.Down => (i, get)
      case Direction.Up => (i, rows - get)
      case Direction.Right => (get, i)
      case Direction.Left => (cols - get, i)

    }

    Bar.rotatePoint((cols/2,rows/2), offset, p)
  }

  def getPoints(offset:Double) = {
    val get = mapi(offset) _
    val p1 = get(xStart, t)
    val q1 = get(xEnd, t)
    val p2 = get(xStart-1, t-scale)
    val q2 = get(xEnd+1, t-scale)
    new Quad(p1, q1, p2, q2).fill

  }


  def draw(offset:Double)(tr:TileRenderer):TileRenderer = {
    tr <++ getPoints(offset).map { case p => (p, tile) }
  }
}
