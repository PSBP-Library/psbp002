# Program Specification Based Programming

This is lesson 001 of a "Program Specification Based Programming" course.

All comments are welcome at luc.duponcheel[at]gmail.com.

## Introduction

Programs are values of type `Z >--> Y` that are defined in terms of program concepts that are specified in
`trait Program`. 

The program concepts can

- be implemented in various ways without changing the program definitions,
- be extended by need,
- be effectful ones.

### Functions and Programs

Below some primitive functions are defined.

```scala
package examples

private[examples] val isZeroFunction: BigInt => Boolean = z => z == 0

private[examples] def oneFunction[Z]: Z => BigInt = _ => 1

private[examples] val subtractOneFunction: BigInt => BigInt = z => z - 1

private[examples] val multiplyFunction: Tuple2[BigInt, BigInt] => BigInt = (z, y) => z * y
```

Below the program concept

- functions can be lifted to programs

is specified.

```scala
package psbp.specification.function

private[psbp] trait Function[>-->[-_, +_], &&[+_, +_]]:

  // external declared

  def functionLift[Z, Y]: (Z => Y) => (Z >--> Y)

  def functionFromTuple2Lift[Z, Y, X]: (Tuple2[Z, Y] => X) => ((Z && Y) >--> X)
```

### Algorithms and Programs

The code below defines `factorialFunction`.

```scala
package examples

def factorialFunction: BigInt => BigInt =
  z =>
    if isZeroFunction(z)
    then oneFunction(z)
    else
      val y = factorialFunction(subtractOneFunction(z))
      multiplyFunction((z, y))
```

Below the algorithmic program concepts

- if-then-else

and

- local definition

and

- sequential composition

are specified.

```scala
package psbp.specification.algorithm

private[psbp] trait IfThenElse[>-->[-_, +_]]:

  // external defined

  def If[Z, Y](`z>-->b`: Z >--> Boolean): Then[Z, Y]

  // internal declared

  private[psbp] trait Then[Z, Y]:
    def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y]

  private[psbp] trait Else[Z, Y]:
    def Else(`z>-f->y`: => Z >--> Y): Z >--> Y
```

and

```scala
package psbp.specification.algorithm

private[psbp] trait LocalDefinition[>-->[-_, +_], &&[+_, +_]]:

  // external defined

  def Let[Z, Y, X](`z>-->y`: Z >--> Y): In[Z, Y, X]

  // internal declared

  private[psbp] trait In[Z, Y, X]:
    def In(`(z&&y)>-->x`: => (Z && Y) >--> X): Z >--> X
```

and 

```scala
package psbp.specification.algorithm

private[psbp] trait SequentialComposition[>-->[-_, +_]]:

  // external defined

  extension [Z, Y, X](`z>-->y`: Z >--> Y) def >-->(`y>-->x`: => Y >--> X): Z >--> X
```

Below the algorithmic program concepts specified so far are summarized.

```scala
package psbp.specification.algorithm

trait Algorithm[>-->[-_, +_], &&[+_, +_]]
    extends IfThenElse[>-->],
      LocalDefinition[>-->, &&],
      SequentialComposition[>-->]
```

Finally, below the program concepts specified so far are summarized.

```scala
package psbp.specification.program

import psbp.specification.function.{Function}

import psbp.specification.algorithm.{Algorithm}

trait Program[>-->[-_, +_], &&[+_, +_]] extends Function[>-->, &&], Algorithm[>-->, &&]
```

### `factorialProgram`

The code below defines `factorialProgram` using the program concepts specified so far.

```scala
package examples.specification

import psbp.specification.program.{Program}

def factorialProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =

  lazy val summonedProgram: Program[>-->, &&] = summon[Program[>-->, &&]]

  import summonedProgram.{Let, If}

  If(isZeroProgram) Then {
    oneProgram
  } Else {
    Let {
      subtractOneProgram >--> factorialProgram
    } In {
      multiplyProgram
    }
  }
```

The primitive programs used so far are

```scala
package examples.specification

import psbp.specification.program.{Program}

import examples.{isZeroFunction, oneFunction, subtractOneFunction, multiplyFunction}

def isZeroProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> Boolean =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(isZeroFunction)

def oneProgram[
    Z,
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: Z >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(oneFunction)

def subtractOneProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(subtractOneFunction)

def multiplyProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: (BigInt && BigInt) >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionFromTuple2Lift
  functionFromTuple2Lift(multiplyFunction)
```

### Data Structures

Below the product data structure program concepts 

- projection
- construction

are specified.

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

Below the data structure program concepts specified so far are summarized.

```scala
package psbp.specification.dataStructure

private[psbp] trait DataStructure[>-->[-_, +_], &&[+_, +_]] extends Product[>-->, &&]
```

Finally, below the program concepts specified so far are summarized.

```scala
package psbp.specification.program

import psbp.specification.function.{Function}

import psbp.specification.algorithm.{Algorithm}

import psbp.specification.dataStructure.{DataStructure}

trait Program[>-->[-_, +_], &&[+_, +_]]
    extends Function[>-->, &&],
      Algorithm[>-->, &&],
      DataStructure[>-->, &&]
```

### `fibonacciProgram01`

The code below defines `fibonacciProgram01` using the program concepts specified so far.

```scala
package examples.specification

import psbp.specification.program.{Program}

import examples.specification.{
  isZeroProgram,
  oneProgram,
  isOneProgram,
  subtractOneProgram,
  subtractTwoProgram,
  addProgram
}

def fibonacciProgram01[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =

  lazy val summonedProgram: Program[>-->, &&] = summon[Program[>-->, &&]]

  import summonedProgram.{Let, If, `(z&&y)>-->z`, `(z&&y&&x)>-->(y&&x)`}

  If(isZeroProgram) Then {
    oneProgram
  } Else {
    If(isOneProgram) Then {
      oneProgram
    } Else {
      Let {
        subtractOneProgram >--> fibonacciProgram01
      } In {
        Let {
          `(z&&y)>-->z` >--> subtractTwoProgram >--> fibonacciProgram01
        } In {
          `(z&&y&&x)>-->(y&&x)` >--> addProgram
        }
      }
    }
  }
```

The primitive programs used so far are

```scala
package examples.specification

import psbp.specification.program.{Program}

import examples.{
  isZeroFunction,
  isOneFunction,
  oneFunction,
  subtractOneFunction,
  subtractTwoFunction,
  addFunction,
  multiplyFunction
}

def isZeroProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> Boolean =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(isZeroFunction)

def isOneProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> Boolean =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(isOneFunction)  

def oneProgram[
    Z,
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: Z >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(oneFunction)

def subtractOneProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(subtractOneFunction)

def subtractTwoProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionLift
  functionLift(subtractTwoFunction)

def addProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: (BigInt && BigInt) >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionFromTuple2Lift
  functionFromTuple2Lift(addFunction)

def multiplyProgram[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: (BigInt && BigInt) >--> BigInt =
  lazy val summonedProgram = summon[Program[>-->, &&]]
  import summonedProgram.functionFromTuple2Lift
  functionFromTuple2Lift(multiplyFunction)
```

The primitive functions used so far are

```scala
package examples

private[examples] val isZeroFunction: BigInt => Boolean = z => z == 0

private[examples] val isOneFunction: BigInt => Boolean = z => z == 1

private[examples] def oneFunction[Z]: Z => BigInt = _ => 1

private[examples] val subtractOneFunction: BigInt => BigInt = z => z - 1

private[examples] val subtractTwoFunction: BigInt => BigInt = z => z - 2

private[examples] val addFunction: Tuple2[BigInt, BigInt] => BigInt = (z, y) => z + y

private[examples] val multiplyFunction: Tuple2[BigInt, BigInt] => BigInt = (z, y) => z * y
```

### `fibonacciProgram02`

The code below also defines `fibonacciProgram02` using the program concepts specified so far.

```scala
package examples.specification

import psbp.specification.program.{Program}

import examples.specification.{
  isZeroProgram,
  oneProgram,
  isOneProgram,
  subtractOneProgram,
  subtractTwoProgram,
  addProgram
}

def fibonacciProgram02[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =

  lazy val summonedProgram: Program[>-->, &&] = summon[Program[>-->, &&]]

  import summonedProgram.{Let, If, &&}

  If(isZeroProgram) Then {
    oneProgram
  } Else {
    If(isOneProgram) Then {
      oneProgram
    } Else {
      ((subtractOneProgram >--> fibonacciProgram02) &&
        (subtractTwoProgram >--> fibonacciProgram02)) >-->
        addProgram
    }
  }
```

### `fibonacciProgram03`

The code below also defines `fibonacciProgram03` using the program concepts specified so far.

```scala
package examples.specification

import psbp.specification.program.{Program}

import examples.specification.{
  isZeroProgram,
  oneProgram,
  isOneProgram,
  subtractOneProgram,
  subtractTwoProgram,
  addProgram
}

def fibonacciProgram03[
    >-->[-_, +_]: [_[-_, +_]] =>> Program[>-->, &&],
    &&[+_, +_]
]: BigInt >--> BigInt =

  lazy val summonedProgram: Program[>-->, &&] = summon[Program[>-->, &&]]

  import summonedProgram.{Let, If, &&, &&&}

  If(isZeroProgram) Then {
    oneProgram
  } Else {
    If(isOneProgram) Then {
      oneProgram
    } Else {
      (subtractOneProgram && subtractTwoProgram) >-->
        (fibonacciProgram03 &&& fibonacciProgram03) >-->
        addProgram
    }
  }
```

## Conclusion

We have specified some program concepts and defined some programs using them.

The choice which programs are primitive is up to you to decide.



