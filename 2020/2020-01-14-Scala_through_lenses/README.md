# Scala Through Lenses

### Resources

 * [Optics beyond Lenses with Monocle](https://scalac.io/scala-optics-lenses-with-monocle/) by _Michał Sitko__
 * **Lenses, Folds, and Traversals** by _Edward Kmett_: [Video](https://www.youtube.com/watch?v=cefnmjtAolY), [Slides](http://comonad.com/haskell/Lenses-Folds-and-Traversals-NYC.pdf)
 * **Functor is to Lens as Applicative is to Biplate** by _Russell O'Connor_: [Reddit](https://www.reddit.com/r/haskell/comments/g56xw/functor_is_to_lens_as_applicative_is_to_biplate/), [PDF](http://arxiv.org/pdf/1103.2841.pdf)

### Implementations

 * [Monocle](https://julien-truffaut.github.io/Monocle/) (most popular library)
 * [Goggles](https://github.com/kenbot/goggles) (optics using string interpolator)
 * [Quicklens](https://github.com/softwaremill/quicklens) (ad hoc optics using macros)
 * [Scalaz Lens](http://eed3si9n.com/learning-scalaz/Lens.html) (uses encoding described in O'Connor's paper)
 * [Circe Optics](https://circe.github.io/circe/optics.html) (optics for JSON)
 * [Haskell lens](https://hackage.haskell.org/package/lens) + [wiki](https://github.com/ekmett/lens/wiki) (Edward Kmett's implementation; notice that `Control.Lens.Lens` type is expressed without depending on `lens` library)
