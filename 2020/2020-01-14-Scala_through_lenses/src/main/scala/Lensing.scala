import cats._
import cats.implicits._
import monocle._
import monocle.function.all._
import monocle.function.{At, Index}
import monocle.macros.{GenLens, GenPrism}

object Lensing {
  def println[T](t: T) = pprint.pprintln(t, width = 70)

  sealed trait Connector
  case class USB(device: Option[String]) extends Connector
  case class Ethernet(in: Boolean) extends Connector
  case class Power(in: Boolean) extends Connector

  case class Interface(connectors: List[Connector])

  case class Register(value: Long)
  case class Owner(name: String, surname: String)
  case class Cpu(index: Int, registers: Map[String, Register])

  case class Laptop(cpus: List[Cpu], memory: Int, interface: Interface, owner: Owner)

  val memoryL: Lens[Laptop, Int] = Lens[Laptop, Int](_.memory)(newm => l => l.copy(memory = newm))

  val ownerNameL: Lens[Owner, String] = GenLens[Owner](_.name)
  val ownerSurnameL: Lens[Owner, String] = GenLens[Owner](_.surname)

  val ownerL: Lens[Laptop, Owner] = GenLens[Laptop](_.owner)

  val laptopOwnerNameL: Lens[Laptop, String] =
    GenLens[Laptop](_.owner.name)

  val laptopOwnerSurnameL: Lens[Laptop, String] = ownerL composeLens ownerSurnameL

  val laptopCpuListL: Lens[Laptop, List[Cpu]] = GenLens[Laptop](_.cpus)
  val cpuListT: Traversal[List[Cpu], Cpu] = Traversal.fromTraverse[List, Cpu]
  val laptopCpuT: Traversal[Laptop, Cpu] = laptopCpuListL composeTraversal cpuListT

  val cpuIndexL: Lens[Cpu, Int] = GenLens[Cpu](_.index)
  val laptopCpuIndexT: Traversal[Laptop, Int] = laptopCpuT composeLens cpuIndexL

  val cpuRegisterL: Lens[Cpu, Map[String, Register]] = GenLens[Cpu](_.registers)
  val cpuRxL: Lens[Cpu, Option[Register]] = cpuRegisterL composeLens at("v0")
  val laptopRxT: Traversal[Laptop, Option[Register]] = laptopCpuT composeLens cpuRxL

  val laptopOwnerNameFstO: Optional[Laptop, Char] = laptopOwnerNameL composeOptional headOption
  val laptopOwnerSurnameFstO: Optional[Laptop, Char] = laptopOwnerSurnameL composeOptional headOption

  val laptopConnectorT: Traversal[Laptop, Connector] =
    GenLens[Laptop](_.interface.connectors) composeTraversal each

  val connectorUsbP: Prism[Connector, USB] = Prism[Connector, USB] {
    case u: USB => Some(u)
    case _ => None
  }(identity)

  val laptopUsbT: Traversal[Laptop, USB] = laptopConnectorT composePrism GenPrism[Connector, USB]

  val bigbit: At[BigInt, Int, Boolean] = new At[BigInt, Int, Boolean] {
    override def at(i: Int): Lens[BigInt, Boolean] = Lens[BigInt, Boolean](_.testBit(i))(b => bi => if(b) bi.setBit(i) else bi.clearBit(i))
  }

  def main(args: Array[String]): scala.Unit = {

    println("----------------------------------")

    val l = Laptop(
      List(Cpu(1, Map(("rx", Register(100)))), Cpu(2, Map.empty)),
      16,
      Interface(List(Ethernet(false), USB(None), USB(Some("printer")), Power(true))),
      Owner("robin", "hood")
    )

    println(l)
    println("----------------------------------")

    // Experiment here with all the optics we created

    val b = BigInt(5)
    println(bigbit.at(100).get(b))
    println(bigbit.at(100).set(true)(b))

    println("----------------------------------")
  }

}
