package in.dogue.quadrivial

import com.badlogic.gdx.Gdx
import in.dogue.antiqua.Antiqua
import Antiqua._

object Hiscore {
  private val filename = "./score.bin"
  private def countBits(n:Long)=n.toBinaryString.count(_=='1')
  def put(p:Int) = {
    val array = new Array[Byte](8)
    write(array, 0, p)
    val numBits = countBits(p)
    write(array, 4, numBits)
    Gdx.files.external(filename).writeBytes(array, false)
  }

  private def write(arr:Array[Byte], off:Int, value:Int) = {
    arr(3+off) = (value & 0x000000ff).toByte
    arr(2+off) = ((value >> 8) & 0x000000ff).toByte
    arr(1+off) = ((value >> 16) & 0x000000ff).toByte
    arr(0+off) = ((value >> 24) & 0x000000ff).toByte
  }

  private def read(arr:Array[Byte], off:Int) = {
    def get(i:Int) = {
      val x = arr(i)
      x & 0xff
    }
    get(off + 3) | (get(off + 2) << 8) | (get(off + 1) << 16) | (get(off + 0) << 24)
  }

  def get:Int = {
    try {
      val bytes = new Array[Byte](8)
      Gdx.files.external(filename).readBytes(bytes, 0, 8)
      val p = read(bytes, 0)
      val bits = read(bytes, 4)
      val actualBits = countBits(p)
      if (bits != actualBits) {
        0
      } else {
        p.clamp(0, 99999)
      }
    } catch {
      case e:Exception => 0
    }

  }
}
