package cz.alenkacz.db.postgresscala

trait DbValue {
  def string(): String
  def int(): Int
}
