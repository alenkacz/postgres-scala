package cz.alenkacz.db.postgresscala

trait Row extends IndexedSeq[DbValue] {
  def apply(columnNumber: Int): DbValue
  def apply(columnName: String): DbValue
  def rowNumber: Int

}