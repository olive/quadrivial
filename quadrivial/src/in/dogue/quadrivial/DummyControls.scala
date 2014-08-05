package in.dogue.quadrivial

import com.deweyvm.gleany.saving.{ControlName, ControlNameCollection}


class DummyControl(descriptor: String) extends ControlName {
  override def name: String = descriptor
}

object DummyControls extends ControlNameCollection[DummyControl] {
  def fromString(string: String): Option[DummyControl] = None
  def makeJoypadDefault: Map[String,String] = Map()
  def makeKeyboardDefault: Map[String,java.lang.Float] = Map()
  def values: Seq[DummyControl] = Seq()
}
