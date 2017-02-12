package cz.alenkacz.db.postgresscala

trait DbValue {
  def string(): String
  def int(): Int
  def bigInt(): BigInt
  def double(): Double
  def float(): Float
}
