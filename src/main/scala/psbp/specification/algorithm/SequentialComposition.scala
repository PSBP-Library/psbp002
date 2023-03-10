package psbp.specification.algorithm

private[psbp] trait SequentialComposition[>-->[-_, +_]]:

  // external declared

  extension [Z, Y, X](`z>-->y`: Z >--> Y) def >-->(`y>-->x`: => Y >--> X): Z >--> X
