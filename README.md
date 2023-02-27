# Program Specification Based Programming

This is lesson 002 of a "Program Specification Based Programming" course.

All comments are welcome at luc.duponcheel[at]gmail.com.

## Introduction

Below the program concepts specified so far are briefly repeated.

```scala
trait Program[>-->[-_, +_], &&[+_, +_]]
    extends Function[>-->, &&],
      Algorithm[>-->, &&],
      DataStructure[>-->, &&]
```

where

```scala
private[psbp] trait Function[>-->[-_, +_], &&[+_, +_]]:

  // external declared

  def functionLift[Z, Y]: (Z => Y) => (Z >--> Y)

  def functionFromTuple2Lift[Z, Y, X]: (Tuple2[Z, Y] => X) => ((Z && Y) >--> X)
```

and

```scala
private[psbp] trait Algorithm[>-->[-_, +_], &&[+_, +_]]
    extends IfThenElse[>-->],
      LocalDefinition[>-->, &&],
      SequentialComposition[>-->]
```

where

```scala
private[psbp] trait IfThenElse[>-->[-_, +_]]:

  // external declared

  def If[Z, Y](`z>-->b`: Z >--> Boolean): Then[Z, Y]

  // internal declared

  private[psbp] trait Then[Z, Y]:
    def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y]

  private[psbp] trait Else[Z, Y]:
    def Else(`z>-f->y`: => Z >--> Y): Z >--> Y
```

and where

```scala
private[psbp] trait LocalDefinition[>-->[-_, +_], &&[+_, +_]]:

  // external declared

  def Let[Z, Y, X](`z>-->y`: Z >--> Y): In[Z, Y, X]

  // internal declared

  private[psbp] trait In[Z, Y, X]:
    def In(`(z&&y)>-->x`: => (Z && Y) >--> X): Z >--> X
```

and where

```scala
private[psbp] trait SequentialComposition[>-->[-_, +_]]:

  // external declared

  extension [Z, Y, X](`z>-->y`: Z >--> Y) def >-->(`y>-->x`: => Y >--> X): Z >--> X
```

and

```scala
private[psbp] trait DataStructure[>-->[-_, +_], &&[+_, +_]] extends Product[>-->, &&]
```

where

```scala
private[psbp] trait Product[>-->[-_, +_], &&[+_, +_]]:

  // external declared

  def `(z&&y)>-->z`[Z, Y]: (Z && Y) >--> Z

  def `(z&&y)>-->y`[Z, Y]: (Z && Y) >--> Y

  extension [Z, Y, X](`z>-->y`: Z >--> Y) def &&(`z>-->x`: => Z >--> X): Z >--> (Y && X)

  extension [Z, Y, X, W](`z>-->x`: Z >--> X)
    def &&&(`y>-->w`: => Y >--> W): (Z && Y) >--> (X && W)

  def `(z&&y&&x)>-->z`[Z, Y, X]: (Z && Y && X) >--> Z

  def `(z&&y&&x)>-->y`[Z, Y, X]: (Z && Y && X) >--> Y

  def `(z&&y&&x)>-->x`[Z, Y, X]: (Z && Y && X) >--> X

  def `(z&&y&&x)>-->(y&&x)`[Z, Y, X]: (Z && Y && X) >--> (Y && X)

  def `(z&&y&&x)>-->(z&&x)`[Z, Y, X]: (Z && Y && X) >--> (Z && X)

  def `(z&&y&&x)>-->(z&&y)`[Z, Y, X]: (Z && Y && X) >--> (Z && Y)

  // ...
```

The members of the `trait`'s were not defined yet.

Moreover some members that could have been added as defined members to the `trait`'s were not added yet.

## Content

### Adding defined member `` `z>-->z` `` to `Function`

```scala
package psbp.specification.function

private[psbp] trait Function[>-->[-_, +_], &&[+_, +_]]:

  // external declared

  def functionLift[Z, Y]: (Z => Y) => (Z >--> Y)

  def functionFromTuple2Lift[Z, Y, X]: (Tuple2[Z, Y] => X) => ((Z && Y) >--> X)

  // internal defined
  
  private[psbp] def `z>-->z`[Z]: Z >--> Z = functionLift { z => z }
```

On its own member `` `z>-->z` `` is not useful.

`` `z>-->z` `` will turn out to useful for defining of members of `trait`'s .

### Defining some members in `Product`

```scala
package psbp.specification.dataStructure

import psbp.specification.algorithm.{SequentialComposition}

private[psbp] trait Product[>-->[-_, +_]: SequentialComposition, &&[+_, +_]]:

  // external declared

  def `(z&&y)>-->z`[Z, Y]: (Z && Y) >--> Z

  def `(z&&y)>-->y`[Z, Y]: (Z && Y) >--> Y

  extension [Z, Y, X](`z>-->y`: Z >--> Y) def &&(`z>-->x`: => Z >--> X): Z >--> (Y && X)

  // external defined

  extension [Z, Y, X, W](`z>-->x`: Z >--> X)
    def &&&(`y>-->w`: => Y >--> W): (Z && Y) >--> (X && W) =
      (`(z&&y)>-->z` >--> `z>-->x`) && (`(z&&y)>-->y` >--> `y>-->w`)

  def `(z&&y&&x)>-->z`[Z, Y, X]: (Z && Y && X) >--> Z =
    `(z&&y)>-->z` >--> `(z&&y)>-->z`

  def `(z&&y&&x)>-->y`[Z, Y, X]: (Z && Y && X) >--> Y =
    `(z&&y)>-->z` >--> `(z&&y)>-->y`

  def `(z&&y&&x)>-->x`[Z, Y, X]: (Z && Y && X) >--> X =
    `(z&&y)>-->y`

  def `(z&&y&&x)>-->(y&&x)`[Z, Y, X]: (Z && Y && X) >--> (Y && X) =
    `(z&&y&&x)>-->y` && `(z&&y)>-->y`

  def `(z&&y&&x)>-->(z&&x)`[Z, Y, X]: (Z && Y && X) >--> (Z && X) =
    `(z&&y&&x)>-->z` && `(z&&y)>-->y`

  def `(z&&y&&x)>-->(z&&y)`[Z, Y, X]: (Z && Y && X) >--> (Z && Y) =
    `(z&&y&&x)>-->z` && `(z&&y&&x)>-->y`

  // ...
```

Some members can be defined in terms of members of `SequentialComposition` and simpler members of `Product` itself.

### Defining member `Let` in `LocalDefinition`

```scala
package psbp.specification.algorithm

import psbp.specification.function.{Function}

import psbp.specification.dataStructure.{Product}

private[psbp] trait LocalDefinition[
    >-->[-_, +_]: [>-->[-_, +_]] =>> Function[>-->, &&]
                : SequentialComposition
                : [>-->[-_, +_]] =>> Product[>-->, &&],
    &&[+_, +_]
]:

  private lazy val summonedFunction = summon[Function[>-->, &&]]

  import summonedFunction.{`z>-->z`}

  // external defined

  def Let[Z, Y, X](`z>-->y`: Z >--> Y): In[Z, Y, X] =
    new:
      override def In(`(z&&y)>-->x`: => (Z && Y) >--> X): Z >--> X =
        (`z>-->z` && `z>-->y`) >--> `(z&&y)>-->x`

  // internal declared

  private[psbp] trait In[Z, Y, X]:
    def In(`(z&&y)>-->x`: => (Z && Y) >--> X): Z >--> X
```

Member `Let` can be defined in terms of members of `Function`, `SequentialComposition` and `Product`.

Most notably member `` `z>-->z` `` of `Function` is used.

### Defining member `If` in `IfThenElse`

Member `If` in `IfThenElse` can be defined in terms of member `Let` of `LocalDefinition` and a newly added declared 
member `Or` of `IfThenElse` itself.

```scala
package psbp.specification.algorithm

private[psbp] trait IfThenElse[
    >-->[-_, +_]: [>-->[-_, +_]] =>> LocalDefinition[>-->, &&],
    &&[+_, +_]
]:

  private lazy val summonedLocalDefinition = summon[LocalDefinition[>-->, &&]]

  import summonedLocalDefinition.{Let}

  // external defined

  def If[Z, Y](`z>-->b`: Z >--> Boolean): Then[Z, Y] =
    new:
      override def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y] =
        new:
          override def Else(`z>-f->y`: => Z >--> Y): Z >--> Y =
            Let {
              `z>-->b`
            } In {
              `z>-t->y` Or `z>-f->y`
            }

  // internal declared

  extension [Z, Y](`z>-t->y`: => Z >--> Y)
    private[psbp] def Or(`z>-f->y`: => Z >--> Y): (Z && Boolean) >--> Y

  private[psbp] trait Then[Z, Y]:
    def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y]

  private[psbp] trait Else[Z, Y]:
    def Else(`z>-f->y`: => Z >--> Y): Z >--> Y
```

### Updating `Algorithm`


`IfThenElse` now has an extra argument `&&` and therefore `Algorithm` needs to be updated accordingly.

```scala
package psbp.specification.algorithm

private[psbp] trait Algorithm[>-->[-_, +_], &&[+_, +_]]
    extends IfThenElse[>-->, &&],
      LocalDefinition[>-->, &&],
      SequentialComposition[>-->]
```

## Conclusion

We have defined some `trait` members in terms of other ones.

