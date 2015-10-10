package in.dogue.quadrivial.audio

import com.deweyvm.gleany.AssetLoader

object SoundManager {
  val die = loadS("dead", 1.0)
  val song = loadM("Quadrivial", 1.0)
  def loadS(s:String, d:Double) = {
    val sound = AssetLoader.loadSound(s)
    sound.setAdjustVolume(d.toFloat)
    sound
  }

  def loadM(s:String, d:Double) = {
    val music = AssetLoader.loadMusic(s)
    music.setAdjustVolume(d.toFloat)
    music
  }
}
