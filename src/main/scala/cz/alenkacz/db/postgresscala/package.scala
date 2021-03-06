package cz.alenkacz.db

package object postgresscala {
  private object EmptyParameterPlaceholder

  implicit class SqlStringInterpolator(val s: StringContext) extends AnyVal {

    private def parameterPlaceholder(parameter: Any) = parameter match {
      case EmptyParameterPlaceholder => ""
      case Nil                       => "?"
      case t: Traversable[_]         => t.map(t => "?").mkString(", ")
      case _                         => "?"
    }

    def paramsList(params: Seq[Any]): Seq[Any] = params.flatMap {
      case Nil               => Seq("{}")
      case t: Traversable[_] => t
      case p                 => Seq(p)
    }

    def sql(params: Any*): SqlQuery =
      new SqlQuery(
        s.parts
          .zipAll(params, "", EmptyParameterPlaceholder)
          .foldLeft(new StringBuilder) {
            case (sb, (sqlQueryPart, nextParameter)) =>
              sb ++= sqlQueryPart
              sb ++= parameterPlaceholder(nextParameter)
          }
          .result(),
        paramsList(params)
      )
  }

}
