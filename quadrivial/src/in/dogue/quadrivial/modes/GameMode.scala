package in.dogue.quadrivial.modes

import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.quadrivial.graphics.Spinner
import scala.util.Random
import in.dogue.quadrivial.world.{Halted, Moving, Board}
import com.deweyvm.gleany.data.Recti
import in.dogue.quadrivial.input.Controls

object GameMode {
  def create(cols:Int, rows:Int, r:Random) = {
    val size = 9
    val sp = Spinner.create(cols, rows, Recti(cols/2-size/2, rows/2-size/2, size, size), 0.1, r)
    val b = Board.create(cols, rows, size, sp, r)
    GameMode(cols, rows, b, r)
  }
}

case class GameMode private (cols:Int, rows:Int, board:Board, r:Random) {
  def update = {
    val newBoard = board.update
    newBoard.state match {
      case Moving => copy(board = newBoard).toMode
      case Halted =>
        if (Controls.Middle.justPressed) {
          GameMode.create(rows, cols, r).toMode
        } else {
          this.toMode
        }
    }
  }
  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< board.draw((cols/2, rows/2))
  }

  def toMode:Mode = Mode[GameMode](_.update, _.draw, this)
}
