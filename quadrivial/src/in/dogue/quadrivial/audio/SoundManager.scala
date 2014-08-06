package in.dogue.quadrivial.audio

import com.deweyvm.gleany.AssetLoader

object SoundManager {
  val die = load("dead", 1.0)
  def load(s:String, d:Double) = {
    val sound = AssetLoader.loadSound(s)
    sound.setAdjustVolume(d.toFloat)
    sound
  }
}
