package in.dogue.quadrivial

import in.dogue.antiqua.graphics.{TileRenderer, Renderer}
import com.deweyvm.gleany.{GleanyGame, AssetLoader}
import in.dogue.antiqua.graphics.Tileset
import in.dogue.quadrivial.input.Controls
import in.dogue.quadrivial.modes.{GameMode, Mode}
import scala.util.Random

class Engine {
  val cols = Game.Cols
  val rows = Game.Rows
  val tsize = Game.TileSize
  val rand = new Random()
  val m:Mode = {
    GameMode.create(cols, rows, rand).toMode
  }
  var mode:Mode = m
  val ts = new Tileset(16, 16, tsize, tsize, AssetLoader.loadTexture("16x16"))
  val r = new Renderer(cols*tsize, rows*tsize, 1, ts)

  def update() = {
    if (Controls.Escape.justPressed) {
      GleanyGame.exit()
    }
    mode = mode.update

  }

  def draw() = {
    val tr = TileRenderer.create(cols, rows)
    r.render(tr <+< mode.draw)
    ()
  }

}
