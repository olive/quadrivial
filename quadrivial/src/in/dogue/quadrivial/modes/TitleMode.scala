package in.dogue.quadrivial.modes

import in.dogue.antiqua.Antiqua.TileGroup
import scala.util.Random
import in.dogue.quadrivial.{Game, Quadrivial}
import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.quadrivial.input.Controls
import in.dogue.antiqua.Antiqua
import Antiqua._

object TitleMode {
  def create(cols:Int, rows:Int, gm:BoardMode, r:Random) = {
    val tg = Quadrivial.loadMap("title")
    val tf = Quadrivial.tf
    def mk(s:String)(x:Int, y:Int) = tf.create(s).toTileGroup |++| ((x, y))
    val text = Vector(
      mk("Move left: LMB")(0,0),
      mk("Move right: RMB")(0, 1),
      mk("Start: LMB + RMB")(0, 2),
      mk("Retry: MMB/Space")(0, 3)

    )
    val vers = mk(Game.Version)(0, rows - 1)
    TitleMode(cols, rows, gm.update, tg, text.flatten, vers, r)
  }
}
case class TitleMode(cols:Int, rows:Int, gm:Mode, tg:TileGroup, text:TileGroup, vers:TileGroup, r:Random) {
  def update = {
    if (Controls.Left.isPressed && Controls.Right.isPressed) {
      gm
    } else {
      this.toMode

    }
  }
  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< gm.draw <|| (tg |++| ((11, 8))) <|| (text |++| ((16,36))) <|| vers
  }
  def toMode:Mode = Mode[TitleMode](_.update, _.draw, this)
}
