package in.dogue.quadrivial.world

import in.dogue.antiqua.data.{CP437, Direction}
import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._

object Bar {
  def create(cols:Int, rows:Int, d:Direction) = {
    val tile = CP437.█.mkTile(Color.Black, Color.White)
    Bar(cols, rows, d, tile, 0)
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

  def mapi(i:Int, t:Int) = {
    def get = getTRaw(t)
    d match {
      case Direction.Down => (i, get)
      case Direction.Up => (i, rows - get)
      case Direction.Right => (get, i)
      case Direction.Left => (cols - get, i)

    }
  }

  def draw(tr:TileRenderer):TileRenderer = {
    (tr <++ (xStart to xEnd).map { i => (mapi(i, t), tile)}
        <++  ((xStart - 1) to (xEnd + 1)).map { i => (mapi(i, t-scale), tile)})
  }
}
