package cz.alenkacz.db.postgresscala

import com.github.mauricio.async.db.RowData

class PostgresAsyncRow(rowData: RowData) extends Row {
  def apply(columnNumber: Int): DbValue = new DbValue(rowData(columnNumber))
  def apply(columnName: String): DbValue = new DbValue(rowData(columnName))
  def rowNumber: Int = rowData.rowNumber

  def length: Int = rowData.length
}
