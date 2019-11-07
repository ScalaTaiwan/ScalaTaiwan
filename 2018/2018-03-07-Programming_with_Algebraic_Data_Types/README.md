# Programming with Algebraic Data Types

Slides: [Algebraic_Data_Types.pdf](Algebraic_Data_Types.pdf) (200 kB)

### Other resources

 * **The Algebra of Algebraic Data Types** by _Chris Taylor_: [Part 1](https://chris-taylor.github.io/blog/2013/02/10/the-algebra-of-algebraic-data-types/), [Part 2](https://chris-taylor.github.io/blog/2013/02/11/the-algebra-of-algebraic-data-types-part-ii/), [Part 3](https://chris-taylor.github.io/blog/2013/02/13/the-algebra-of-algebraic-data-types-part-iii/), [Video](https://www.youtube.com/watch?v=YScIPA8RbVE)
 * [Simple Algebraic Data Types](https://bartoszmilewski.com/2015/01/13/simple-algebraic-data-types/) by _Bartosz Milewski_
 * [The algebra (and calculus!) of algebraic data types](https://codewords.recurse.com/issues/three/algebra-and-calculus-of-algebraic-data-types) by _Joel Burget_
 * [Designing with types](https://fsharpforfunandprofit.com/posts/designing-with-types-intro/) series
 * [A new Scala feature for making illegal states unrepresentable](http://blog.leifbattermann.de/2017/04/26/a-new-scala-feature-for-making-illegal-states-unrepresentable/)
 * [Chris Allen's videos of him programming live](https://www.youtube.com/channel/UCMNqLf5GI6mkAUo-LGS07ig/videos) in Haskell

Another example of algebraic data type: _algebraic graph_. It is a representation of graph as ADT which does not allow to create illegal graph. 

```scala
sealed trait Graph[A]
case object Empty extends Graph[Nothing]
case class Vertex(a: A) extends Graph[A]
case class Overlay[A](g1: Graph[A], g2: Graph[A]) extends Graph[A]
case class Connect[A](g1: Graph[A], g2: Graph[A]) extends Graph[A]
```

More information: https://github.com/snowleopard/alga
