package in.dogue.quadrivial.modes

import in.dogue.antiqua.graphics.TileRenderer

object GameMode {
  def create = GameMode()
}

case class GameMode private () {
  def update = this.toMode
  def draw(tr:TileRenderer):TileRenderer = {
    tr
  }

  def toMode:Mode = Mode[GameMode](_.update, _.draw, this)
}
