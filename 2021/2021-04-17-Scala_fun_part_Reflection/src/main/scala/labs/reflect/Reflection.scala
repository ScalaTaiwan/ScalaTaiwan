package labs.reflect

object Reflection_Invoke_Method extends App {

  val myObj = List(1, 2, 3, 4, 5)
  val myMethod = "filter"
  val myFilter: Int => Boolean = (a: Int) => {  println(a);  a >= 3 }

  import scala.reflect.runtime.{universe => ru}   // Runtime universe
  val rm: ru.Mirror = ru.runtimeMirror(this.getClass.getClassLoader)   // Runtime mirror

  // get method symbol
  val objType: ru.Type = ru.typeTag[List[Int]].tpe
  val methodName: ru.TermName = ru.TermName(myMethod)
  val methodSymbol: ru.MethodSymbol = objType.member(methodName).asMethod

  // get mirror method
  val instanceMirror: ru.InstanceMirror = rm.reflect(myObj)
  val mirrorMethod: ru.MethodMirror = instanceMirror.reflectMethod(methodSymbol)

  // invoke method
  val result: Any = mirrorMethod(myFilter)
  println( result )
}

object Reflection_ModuleMirror extends App {

  import scala.reflect.runtime.{universe => ru}
  val rm: ru.Mirror = ru.runtimeMirror(this.getClass.getClassLoader)

  // scala.collection.immutable.List is a object(module)
  val moduleSymbol: ru.ModuleSymbol = rm.staticModule("scala.collection.immutable.List")
  val moduleMirror: ru.ModuleMirror = rm.reflectModule(moduleSymbol)

  val obj: List.type = moduleMirror.instance.asInstanceOf[List.type]

  println(obj(1,2,3))     // Same as List(1, 2, 3)

}

object Reflection_ClassMirror extends App {

  case class User(name: String, age: Int, var status: String)

  import scala.reflect.runtime.{universe => ru}
  val rm = ru.runtimeMirror(this.getClass.getClassLoader)

  val typeUser = ru.typeOf[User]
  val classSymbol = typeUser.typeSymbol.asClass
  val classMirror = rm.reflectClass(classSymbol)

  val methodSymbol = typeUser.decl(ru.termNames.CONSTRUCTOR).asMethod
  val methodMirror: ru.MethodMirror = classMirror.reflectConstructor(methodSymbol)

  val obj: Any = methodMirror("Tom", 32, "Ok")   // Same as User("Tom", 32, "Ok")
  println(obj)

}

object Reflection_ClassTag extends App {

  import scala.reflect.ClassTag

  { // Without ClassTag
    // warn: abstract type pattern T is unchecked since it is eliminated by erasure
    def mkSeqTypeOf[T](seq: Seq[Any]): Seq[T] =
      seq.flatMap {
        case item: T => Some(item)
        case _ => None
      }

    val seq = Seq(1, "string1", List(), "string2")
    val result = mkSeqTypeOf[String](seq)
    println(result)
  }

  { // With ClassTag - Implicit parameter
    def mkSeqTypeOf[T](seq: Seq[Any])(implicit ct: ClassTag[T]): Seq[T] =
      seq.flatMap {
        case item: T => Some(item)
        case _ => None
      }

    val seq = Seq(1, "string1", List(), "string2")
    val result = mkSeqTypeOf[String](seq)
    println(result)
  }


  { // With ClassTag - Context bound
    def mkSeqTypeOf[T: ClassTag](seq: Seq[Any]): Seq[T] =
      seq.flatMap {
        case item: T => Some(item)
        case _ => None
      }

    val seq = Seq(1, "string1", List(), "string2")
    val result = mkSeqTypeOf[String](seq)
    println(result)
  }

}

object Reflection_TypeTag extends App {

  import scala.reflect.runtime.{universe => ru}   // Runtime universe

  // use Implicit parameter
  def getTypeTag[T](obj: T)(implicit tt: ru.TypeTag[T]): ru.TypeTag[T] = tt

  val tt1 = ru.typeTag[Seq[Int]]
  val tt2 = getTypeTag( Seq(1,2,3) )
  assert( !(tt1 eq tt2) )
  assert( !(tt1.tpe eq tt2.tpe) )
  assert( tt1.tpe =:= tt2.tpe )

  // use Context bound
  def printTypeArgs[T: ru.TypeTag]: Unit = {
    val tt = ru.typeTag[T].tpe
    tt match {
      case ru.TypeRef(_,sym,args) =>
        println("Symbol: " + sym.fullName + ", type args: " + args.mkString(","))
    }
  }

  printTypeArgs[Map[String, Int]]
}

object Reflection_WeakTypeTag extends App {

  import scala.reflect.runtime.{universe => ru}   // Runtime universe

  trait MyTrait {
    type Foo
    //def barType = ru.typeTag[Bar].tpe         // No TypeTag available for Foo.this.Bar
    def fooType = ru.weakTypeTag[Foo].tpe
  }

  val tt = ru.weakTypeTag[Seq[Int]]

}

object Reflection_Type extends App {

  import scala.reflect.runtime.{universe => ru}

  val tpe = ru.typeOf[Some[String]]
  val tpe1 = ru.typeTag[Some[String]].tpe

  assert( tpe.eq(tpe1) )
}

object Reflection_OperationOnTypes extends App {


  import scala.reflect.runtime.{universe => ru}

  val rm = ru.runtimeMirror(this.getClass.getClassLoader)

  def getType[T: ru.TypeTag](obj: T): ru.Type = ru.typeTag[T].tpe

  class TA
  type AnotherA = TA

  {   // check type equality
    val a1 = new TA
    val a2 = new TA

    println( "Type(AnotherA) == Type(TA) : " + (ru.typeOf[AnotherA] == ru.typeOf[TA]) )
    println( "Type(AnotherA) =:= Type(TA) : " + (ru.typeOf[AnotherA] =:= ru.typeOf[TA]) )

    println( "Type(TA) == Type(a1) : " + (ru.typeOf[TA] == getType(a1)) )
    println( "Type(AnotherA) == Type(a1) : " + (ru.typeOf[AnotherA] == getType(a1)) )
    println( "Type(AnotherA) =:= Type(a1) : " + (ru.typeOf[AnotherA] =:= getType(a1)) )

    println( "Type(AnotherA) == Type(A) : " + (ru.typeOf[AnotherA] == ru.typeOf[TA]) )
    println( "Type(AnotherA) =:= Type(A) : " + (ru.typeOf[AnotherA] =:= ru.typeOf[TA]) )
  }

  println("=" * 80)

  class TE {
    type T
    val x: Option[T] = None
  }
  class TC extends TE
  class TD extends TC

  { // check subtyping relationships
    val c = new TC
    val d = new TD
    println( "Type(c) <:< Type(TE) : " + (getType(c) <:< ru.typeOf[TE]) )
    println( "Type(c) <:< Type(d) : " + (getType(c) <:< getType(d)) )
    println( "Type(d) <:< Type(c) : " + (getType(d) <:< getType(c)) )
  }

  println("=" * 80)

  { // check weak conformance relations
    println( "Int <:w Double : " + (ru.typeOf[Int] <:< ru.typeOf[Double]))
    println( "Double <:w Int : " + (ru.typeOf[Double] <:< ru.typeOf[Int]))
    println( "Int <:w Double : " + (ru.typeOf[Int] weak_<:< ru.typeOf[Double]))
    println( "Double <:w Int : " + (ru.typeOf[Double] weak_<:< ru.typeOf[Int]))
  }

}

object Reflection_Symbols extends App {

  import scala.reflect.runtime.{universe => ru}
  val rm: ru.Mirror = ru.runtimeMirror(this.getClass.getClassLoader)

  private def symbolInfo(symbol: ru.Symbol): String = {
    new StringBuilder()
            .append(symbol.fullName + "(" )
            .append(if (symbol.isType) "type," else "")
            .append(if (symbol.isTerm) "term," else "")
            .append(if (symbol.isPackage) "package," else "")
            .append(if (symbol.isPackageClass) "package-class," else "")
            .append(if (symbol.isClass) "class," else "")
            .append(if (symbol.isModule) "module," else "")
            .append(if (symbol.isModuleClass) "module-class," else "")
            .append(if (symbol.isMethod) "method," else "")

            .append(if (symbol.isFinal) "final," else "")
            .append(if (symbol.isAbstract) "abstract," else "")
            .append(if (symbol.isPrivate) "private," else "")
            .append(if (symbol.isPublic) "public," else "")
            .append(if (symbol.isConstructor) "constructor," else "")
            .append(if (symbol.isStatic) "static," else "")
            .append(if (symbol.isAbstractOverride) "abstract-override," else "")
            .append(")")
            .toString()
  }

  {
    // Access Symbols from runtime mirror
    val symbol1: ru.ModuleSymbol = rm.staticPackage("scala.reflect")
    val symbol2: ru.ModuleSymbol = rm.staticModule("scala.None")
    val symbol3: ru.ClassSymbol = rm.staticClass("scala.concurrent.Future")
    val symbol4: ru.ModuleSymbol = rm.RootPackage     // root module
    val symbol5: ru.ClassSymbol = rm.RootClass        // root module-class
    println("Package: " + symbolInfo(symbol1))
    println("Module: " + symbolInfo(symbol2))
    println("Module: " + symbolInfo(symbol3))
    println("RootPackage: " + symbolInfo(symbol4))
    println("RootClass: " + symbolInfo(symbol5))
  }

  println("=" * 80)

  { // Access Symbols under a `Type`
    import scala.concurrent.Future
    val tpe: ru.Type = ru.typeOf[Future[String]]
    println("typeSymbol: " + symbolInfo(tpe.typeSymbol))
    println("termSymbol: " + symbolInfo(tpe.termSymbol))
    println("baseClasses: " + tpe.baseClasses)
    println("""decl("value"): """ + symbolInfo(tpe.decl(ru.TermName("value")).asMethod))
    println("typeArgs: " + tpe.typeArgs.mkString(","))

    // Symbol owner hierarchy
    val methodSymbol: ru.MethodSymbol = tpe.decl(ru.TermName("value")).asMethod
    println("Future owner: " + tpe.typeSymbol.owner.fullName)
    println("""method "value" owner: """ + methodSymbol.owner.fullName)
  }

  println("=" * 80)

  private class MyObject(name: String) {
    private val data = Seq(1,2,3)
    def message(msg: String): String = "[" + name + "]:" + "$msg"
    def getData() = data
  }

  { // Query types
    import scala.concurrent.Future
    val tpe: ru.Type = ru.typeOf[Future[String]]
    println("""decl("value"): """ + symbolInfo(tpe.decl(ru.TermName("value")).asMethod))

    val tpe2: ru.Type = ru.typeOf[MyObject]
    println("typeSymbol: " + symbolInfo(tpe2.typeSymbol))
    println("decls:\n" + tpe2.decls)
    println("members:\n" + tpe2.members)
  }

}

object Reflection_TraversePackage extends App {

  import scala.reflect.runtime.{universe => ru}

  val rm = ru.runtimeMirror(this.getClass.getClassLoader)

  def traversePackage(handler: (Int, ru.ModuleSymbol) => Unit): Unit = {
    def _traverse(level: Int, pkg: ru.ModuleSymbol): Unit = {
      handler(level, pkg)
      val subPkgs: Seq[ru.ModuleSymbol] =
        pkg.info.decls.sorted
                .filter(sym=>sym.isPackage && sym.isModule)
                .map(_.asModule)
      subPkgs.foreach { sym: ru.Symbol =>
        if (sym!=pkg)
          _traverse(level + 1, sym.asModule)
      }
    }
    _traverse(0, rm.RootPackage)
  }

  traversePackage { (level, pkg) =>
    println("    " * level + "+" + "-- "+  pkg.name)
  }
}

object Reflection_Tree_Parse extends App {

  import scala.reflect.runtime.{universe => ru}
  import scala.tools.reflect.ToolBox
  val rm = ru.runtimeMirror(this.getClass.getClassLoader)
  val toolbox = rm.mkToolBox()    // options = "-verbose"

  private def printTree(tree: toolbox.u.Tree): Unit = {
    println("show: " + ru.show(tree))
    println("showRaw: " + ru.showRaw(tree))
  }

  // Parse source code to generate a ASTs
  val tree1: toolbox.u.Tree = toolbox.parse("val i = 1")
  printTree(tree1)

  println("*" * 40)

  val tree2 = toolbox.parse("for(x <- 1 to 3) println(x)")
  printTree(tree2)

  println("*" * 40)

  val code3 = """
    |class MyClass[T] {
    |  def func: T
    |}
    |""".stripMargin
  val tree3 = toolbox.parse(code3)
  printTree(tree3)

}

object SingleObject {
  import java.util.{HashMap => JHashMap}
  import scala.collection.mutable

  val scalaMap = mutable.HashMap.empty[String, String]
  val scalaMap2 = Map(100 -> "abc", 200 -> "def")
  val javaMap = new JHashMap[String, Any]()
}

case class User(name: String)

class User2(val name: String)

object Reflection_Tree_Eval extends App {

  import scala.reflect.runtime.{universe => ru}
  import scala.tools.reflect.ToolBox
  val rm = ru.runtimeMirror(this.getClass.getClassLoader)
  val toolbox = rm.mkToolBox()

  {
    val code = "(1 to 10).reduce(_+_)"
    val tree = toolbox.parse(code)
    val result = toolbox.eval(tree).asInstanceOf[Int]
    println(s"  code: $code")
    println(s"  result: $result")
  }

  println("=" * 40)

  {
    import java.util.{HashMap => JHashMap}
    import scala.collection.mutable

    SingleObject.scalaMap.put("scalaVersion", "3.0.0")
    SingleObject.javaMap.put("hello", "world")
    val moduleName = SingleObject.getClass.getName.dropRight(1)
    println(s"module name: $moduleName")

    val code =
      s"""
         |import labs.reflect.SingleObject
         |var s1 = SingleObject.javaMap.get("hello")
         |var s2 = SingleObject.scalaMap2.get(100)
         |println(s1)
         |println(s2)
         |
         |// 使用 full name 可以呼叫
         |val s3 = $moduleName.javaMap.get("hello")
         |val s4 = $moduleName.scalaMap2.get(100)
         |println(s3)
         |println(s4)
         |""".stripMargin

    val tree = toolbox.parse(code)
    println("tree: " + tree)
    toolbox.eval(tree)
  }

  println("=" * 40)

  {
    val code =
      s"""
         |println( labs.reflect.User("Vito") )
         |import labs.reflect._
         |println( "user: " + User("Vito") )
         |println( "user 2: " + new User2("Jack"))
         |""".stripMargin
    val tree = toolbox.parse(code)
    println("tree: " + tree)
    toolbox.eval(tree)
  }

}

object Reflection_Exprs extends App {

  import scala.reflect.runtime.{universe => ru}
  import scala.tools.reflect.ToolBox
  val rm = ru.runtimeMirror(this.getClass.getClassLoader)

  { // println
    val expr: ru.Expr[Unit] = ru.reify {
      val name = "everyone"
      println(s"Hello, $name!")
    }
    println("tree: " + expr.tree)
    println("showRaw(tree): " + ru.showRaw(expr.tree))
  }

  println("=" * 80)

  { // Collection
    val expr = ru.reify {
      Seq("a", "b", "c").foreach(println)
    }
    println("tree: " + expr.tree)
    println("showRaw(tree): " + ru.showRaw(expr.tree))
  }

  println("=" * 80)

  { // define a case class
    val expr = ru.reify {
      case class Person(id: String, var name: String) {
        override def toString: String = {
          s"Person($name) - $id"
        }
      }
    }
    println("tree:\n" + expr.tree)
    println("showRaw(tree):\n" + ru.showRaw(expr.tree))
  }

}

object Reflection_CombineAll extends App {

  val myList = List(1, 2, 3, 4, 5)
  val myMethod = "filter"
  val myHandler = "(a: Int) => {  println(a);  a >= 3 }"

  import scala.reflect.runtime.{universe => ru}
  import scala.tools.reflect.ToolBox

  val rm = ru.runtimeMirror(this.getClass.getClassLoader)
  val tb = rm.mkToolBox()

  def getTypeTag[T: ru.TypeTag](obj: T): ru.TypeTag[T] =
    ru.typeTag[T]

  def eval[T](source: String): T = {
    val tree: tb.u.Tree = tb.parse(source)
    tb.eval(tree).asInstanceOf[T]
  }

  // get method symbol from a type
  val tpe: ru.Type = ru.typeOf[List[Int]]   // getTypeTag(myList).tpe
  val methodName: ru.TermName = ru.TermName(myMethod)
  val methodSymbol: ru.MethodSymbol = tpe.member(methodName).asMethod

  // get method mirror from a instance
  val instanceMirror: ru.InstanceMirror = rm.reflect(myList)
  val methodMirror: ru.MethodMirror = instanceMirror.reflectMethod(methodSymbol)

  val filter: Int => Boolean = eval(myHandler)
  val result = methodMirror(filter).asInstanceOf[List[Int]]   // invoke method
  val resultTypeTag = getTypeTag(result)
  println( resultTypeTag.tpe.finalResultType )
  println( result.getClass )
  println( result )
}


// execute_from_source_code
object Reflection_Eval_ComplexCode extends App {

  val simpleStreamCode = """
                           |import akka.{Done, NotUsed}
                           |import akka.actor.ActorSystem
                           |import akka.stream.scaladsl.Source
                           |import scala.concurrent._
                           |implicit val system = ActorSystem("quickstart1")
                           |val source: Source[Int, NotUsed] = Source(1 to 100)
                           |val done: Future[Done] = source.runForeach(i => println(i))
                           |implicit val ec = system.dispatcher
                           |done.onComplete(_ => system.terminate())
        """.stripMargin

  import scala.reflect.runtime.{universe => ru}
  import scala.tools.reflect.ToolBox
  val rm = ru.runtimeMirror(this.getClass.getClassLoader)
  val toolbox = rm.mkToolBox()    // options = "-verbose"
  val tree = toolbox.parse(simpleStreamCode)
  toolbox.eval(tree)      // compiles and run, same as compile(tree)()
}
