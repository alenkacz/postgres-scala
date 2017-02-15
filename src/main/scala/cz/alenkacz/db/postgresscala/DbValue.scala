package cz.alenkacz.db.postgresscala

import java.net.InetAddress
import java.sql.Time
import java.time.Instant
import java.util.UUID

trait DbValue {
  def string: String
  def stringOpt: Option[String]
  def strings: Array[String]
  def int: Int
  def intOpt: Option[Int]
  def ints: Array[Int]
  def bigInt: BigInt
  def bigIntOpt: Option[BigInt]
  def bigInts: Array[BigInt]
  def double: Double
  def doubleOpt: Option[Double]
  def doubles: Array[Double]
  def float: Float
  def floatOpt: Option[Float]
  def floats: Array[Float]
  def long: Long
  def longOpt: Option[Long]
  def longs: Array[Long]
  def bool: Boolean
  def boolOpt: Option[Boolean]
  def bools: Array[Boolean]
  def short: Short
  def shortOpt: Option[Short]
  def shorts: Array[Short]
  def inetAddress: InetAddress
  def inetAddressOpt: Option[InetAddress]
  def uuid: UUID
  def uuidOpt: Option[UUID]
  def instant: Instant
  def instantOpt: Option[Instant]
  def time: Time
  def timeOpt: Option[Time]
  def bytes: Array[Byte]
  def any: Any
}
