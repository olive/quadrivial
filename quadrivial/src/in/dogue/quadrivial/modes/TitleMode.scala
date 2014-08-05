package in.dogue.quadrivial.modes

import in.dogue.antiqua.graphics.TileRenderer

object TitleMode {
  def create = TitleMode()
}

case class TitleMode private () {
  def update = this.toMode
  def draw(tr:TileRenderer):TileRenderer = {
    tr
  }

  def toMode:Mode = Mode[TitleMode](_.update, _.draw, this)
}
