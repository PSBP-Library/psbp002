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
