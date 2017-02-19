package cz.alenkacz.db.postgresscala

import java.net.InetAddress
import java.sql.Time
import java.time.Instant
import java.util.UUID

trait DbValue {
  def string: String
  def stringOpt: Option[String]
  def strings: Seq[String]
  def int: Int
  def intOpt: Option[Int]
  def ints: Seq[Int]
  def bigInt: Long
  def bigIntOpt: Option[Long]
  def bigInts: Seq[Long]
  def double: Double
  def doubleOpt: Option[Double]
  def doubles: Seq[Double]
  def float: Float
  def floatOpt: Option[Float]
  def floats: Seq[Float]
  def long: Long
  def longOpt: Option[Long]
  def longs: Seq[Long]
  def bool: Boolean
  def boolOpt: Option[Boolean]
  def bools: Seq[Boolean]
  def short: Short
  def shortOpt: Option[Short]
  def shorts: Seq[Short]
  def inetAddress: InetAddress
  def inetAddresses: Seq[InetAddress]
  def inetAddressOpt: Option[InetAddress]
  def uuid: UUID
  def uuids: Seq[UUID]
  def uuidOpt: Option[UUID]
  def instant: Instant
  def instantOpt: Option[Instant]
  def time: Time
  def timeOpt: Option[Time]
  def bytes: Seq[Byte]
  def any: Any
}
