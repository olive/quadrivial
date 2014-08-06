package in.dogue.quadrivial

import in.dogue.antiqua.data.{CP437, Direction}
import in.dogue.quadrivial.data.AugDirection
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.graphics.{Tile, TextFactory}
import in.dogue.antiqua.Antiqua
import Antiqua._

object Quadrivial {
  implicit def direction2Aug(d:Direction) = new AugDirection(d)
  def loadMap(s:String) = Tile.groupFromFile(s, "tiles", CP437.intToCode, _.mkTile(Color.Black, Color.White))
  def tf = TextFactory(Color.Black, Color.White, CP437.unicodeToCode)
}
