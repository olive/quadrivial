package in.dogue.quadrivial

import com.deweyvm.gleany.{Glean, GleanyGame, GleanyInitializer}
import java.util.concurrent.{Executors, Callable, TimeUnit}
import in.dogue.quadrivial.input.Controls
object Game {
  var frame = 0
  val Cols = 47
  val Rows = 47
  val TileSize = 16
}
class Game(initializer: GleanyInitializer) extends GleanyGame(initializer) {
  private lazy val engine = new Engine()
  override def update() {
    Controls.update()
    engine.update()
    Game.frame += 1
  }

  override def draw() {
    engine.draw()
  }

  override def resize(width: Int, height: Int) {
    Glean.y.settings.setWindowSize(width, height)
  }

  override def dispose() {
    val executor = Executors.newSingleThreadExecutor()
    executor.invokeAll(java.util.Arrays.asList(new Callable[Unit] {
      override def call(): Unit = ()
    }), 2, TimeUnit.SECONDS)
    executor.shutdown()
  }
}
