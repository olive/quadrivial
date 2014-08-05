package in.dogue.quadrivial.world

import in.dogue.antiqua.data.{CP437, Code, Direction}
import in.dogue.quadrivial.input.Controls
import in.dogue.quadrivial.{Hiscore, Quadrivial}
import Quadrivial._
import in.dogue.antiqua.graphics.{TileRenderer, Tile}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._
import scala.util.Random
import in.dogue.quadrivial.graphics.Spinner

object Board {
  def create(cols:Int, rows:Int, size:Int, sp:Spinner, r:Random) = {
    import Direction._
    def mk(code:Code) = code.mkTile(Color.Black, Color.White)

    val left = mk(CP437.◄)
    val right = mk(CP437.►)
    val up = mk(CP437.▲)
    val down = mk(CP437.▼)
    def tile(d:Direction) = d match {
      case Up => up
      case Left => left
      case Right => right
      case Down => down
    }
    val pat = Pattern.random(r)(60)
    Board(cols, rows, size, Up, tile, sp, pat, Seq(), 0, r, Moving, Hiscore.get)
  }
}

sealed trait BoardState
case object Halted extends BoardState
case object Moving extends BoardState

case class Board(cols:Int, rows:Int, size:Int, pos:Direction, tile:Direction=>Tile, sp:Spinner, pattern:Pattern, bars:Seq[Bar], t:Int, r:Random, state:BoardState, hiscore:Int) {
  println(hiscore)
  def update = {
    state match {
      case Moving => updateMoving
      case Halted => this
    }
  }

  def updateMoving = {
    val newPos = if (Controls.Left.justPressed) {
      pos.rotLeft1
    } else if (Controls.Right.justPressed) {
      pos.rotRight1
    } else {
      pos
    }
    val newState = if (bars.exists(b => inRect(b) && b.d == pos.opposite)) {
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
    copy(pattern=newPat, pos=newPos, bars = patBars.map{_.update}, t=t+1, state=newState, sp = sp.update)
  }

  private def inRect(b:Bar) = {
    b.getT >= (cols - size)/2
  }

  private def fmtTime(t:Int) = {
    val secs = t / 60
    val ms = (((t % 60) / 60.0) * 100).toInt
    "%4s:%d".format(secs.toString, ms)
  }

  def getTime = {
    fmtTime(t)

  }

  def getHiscore = {
    val tt = if (t > hiscore) {
      t
    } else {
      hiscore
    }
    fmtTime(tt)
  }

  def draw(center:Cell)(tr:TileRenderer):TileRenderer = {
    val s = size/2 + 1
    val tf = Quadrivial.tf
    val label = tf.create("Time:")
    val time = tf.create(getTime)
    val hs = tf.create(getHiscore)
    (tr <+< sp.draw
        <| (center |+| ((s*pos.dx, s*pos.dy)), tile(pos))
        <++< bars.map {_.draw _}
        <+< label.draw(center |-| ((3, 1)))
        <+< time.draw(center |-| ((3,0)))
        <+< hs.draw(center |-| ((3,-1)))
      )
  }
}
