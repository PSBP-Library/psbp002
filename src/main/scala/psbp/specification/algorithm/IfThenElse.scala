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
