package in.dogue.quadrivial.modes

import in.dogue.antiqua.data.{CP437, Code}
import in.dogue.quadrivial.input.Controls
import in.dogue.quadrivial.{Hiscore, Quadrivial}
import Quadrivial._
import in.dogue.antiqua.graphics.{TileRenderer, Tile}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._
import scala.util.Random
import in.dogue.quadrivial.graphics.Spinner
import in.dogue.antiqua.geometry.Circle
import com.deweyvm.gleany.data.Recti
import in.dogue.quadrivial.world.{Bar, Pattern}
import in.dogue.quadrivial.audio.SoundManager

object BoardMode {
  def create(cols:Int, rows:Int, r:Random) = {
    SoundManager.song.play()
    val size = 9
    val sp = Spinner.create(cols, rows, Recti(cols/2-size/2, rows/2-size/2, size, size), 0.1, r)
    def mk(code:Code) = code.mkTile(Color.Black, Color.White)

    val left = mk(CP437.`@`)
    val pat = Pattern.random(r)(60)
    BoardMode(cols, rows, size, 0, 0, left, sp, pat, Seq(), 0, r.nextInt(10000), r, Moving, Hiscore.get)
  }
}

sealed trait BoardState
case object Halted extends BoardState
case object Moving extends BoardState

case class BoardMode(cols:Int, rows:Int, size:Int, theta:Double, pAngle:Double, tile:Tile, sp:Spinner, pattern:Pattern, bars:Seq[Bar], t:Int, t0:Int, r:Random, state:BoardState, hiscore:Int) {
  def update = {
    state match {
      case Moving => updateMoving.toMode
      case Halted =>
        if (Controls.Middle.justPressed) {
          BoardMode.create(rows, cols, r).toMode
        } else {
          this.toMode
        }
    }
  }

  private def updateMoving = {
    val angV = 0.2
    val newPos = if (Controls.Left.isPressed) {
      pAngle - angV
    } else if (Controls.Right.isPressed) {
      pAngle + angV
    } else {
      pAngle
    }
    val newState = if (bars.exists(b => inRect(b) && intersects((cols/2, rows/2), b))) {
      if (state == Moving) {
        SoundManager.die.play()
        SoundManager.song.pause()
      }
      if (state == Moving && t > hiscore) {

        Hiscore.put(t)
      }
      Halted
    } else {
      Moving
    }
    val newBars = bars.filter{b => !b.isDone && !inRect(b)}
    val patBars = pattern.update(t).map{d => Bar.create(cols, rows, d)} ++ newBars
    val newPat = if (pattern.isDone(t)) {
      pattern.transform(r)
    } else {
      pattern
    }
    val nt = math.sin((t + t0)/70.0) + 1
    copy(pattern=newPat, pAngle=newPos, bars = patBars.map{_.update}, t=t+1, state=newState, sp = sp.setAngle(nt), theta=nt)
  }

  private def inRect(b:Bar) = {
    b.getT >= (cols - size - 2)/2
  }

  private def intersects(center:Cell, b:Bar) = {
    b.inAngle(playerAngle)
  }

  private def fmtTime(t:Int) = {
    val secs = t / 60
    val ms = (((t % 60) / 60.0) * 100).toInt
    "%4s:%02d".format(secs.toString, ms)
  }

  private def getTime = {
    fmtTime(t)

  }

  private def getHiscore = {
    val tt = if (t > hiscore) {
      t
    } else {
      hiscore
    }
    fmtTime(tt)
  }



  private def playerAngle = pAngle %% (math.Pi * 2)

  private def playerPos(center:Cell):Cell = {
    val s = size/2 + 3
    val circle = Circle.bresenham(center.x, center.y, s).sortBy{ case (x, y) => math.atan2(y - center.y, x - center.x)}
    val index = (((playerAngle + theta) % (math.Pi*2))/(math.Pi * 2) * circle.length).toInt
    circle.toVector(index)
  }

  def draw(tr:TileRenderer):TileRenderer = {
    val center = (cols/2, rows/2)
    val tf = Quadrivial.tf
    val label = tf.create("Time:")
    val time = tf.create(getTime)
    val hs = tf.create(getHiscore)
    (tr <+< sp.draw
        <+| (playerPos(center), tile)
        <++< bars.map {_.draw(theta) _}
        <+< label.draw(center |-| ((3, 1)))
        <+< time.draw(center |-| ((3,0)))
        <+< hs.draw(center |-| ((3,-1)))
      )
  }


  def toMode:Mode = Mode[BoardMode](_.update, _.draw, this)
}
