package in.dogue.quadrivial

import in.dogue.antiqua.data.{CP437, Direction}
import in.dogue.quadrivial.data.AugDirection
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.graphics.TextFactory

object Quadrivial {
  implicit def direction2Aug(d:Direction) = new AugDirection(d)
  def tf = TextFactory(Color.Black, Color.White, CP437.unicodeToCode)
}
