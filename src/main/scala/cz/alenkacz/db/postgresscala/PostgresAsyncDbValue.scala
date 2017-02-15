package cz.alenkacz.db.postgresscala
import java.net.InetAddress
import java.sql.Time
import java.time.Instant
import java.util.UUID

import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

import scala.collection.mutable.ArrayBuffer

class PostgresAsyncDbValue(value: Any) extends DbValue {
  override def string: String = value.asInstanceOf[String]

  override def int: Int = value.asInstanceOf[Int]

  override def bigInt: BigInt = value.asInstanceOf[BigInt]

  override def double: Double = value.asInstanceOf[Double]

  override def float: Float = value.asInstanceOf[Float]

  override def long: Long = value.asInstanceOf[Long]

  override def bool: Boolean = value.asInstanceOf[Boolean]

  override def short: Short = value.asInstanceOf[Short]

  override def inetAddress: InetAddress = value.asInstanceOf[InetAddress]

  override def uuid: UUID = value.asInstanceOf[UUID]

  override def instant: Instant = value match {
    case d: DateTime => Instant.ofEpochMilli(d.getMillis)
    case d: LocalDateTime => Instant.ofEpochMilli(value.asInstanceOf[LocalDateTime].toDateTime(DateTimeZone.UTC).getMillis)
    case _ => throw new IllegalArgumentException("Unsupported datetime type - expecting DateTime or LocalDatetime from joda library")
  }

  override def time: Time = value.asInstanceOf[Time]

  override def bytes: Array[Byte] = value.asInstanceOf[Array[Byte]]

  override def any: Any = value

  override def stringOpt: Option[String] = Option(string)

  override def strings: Array[String] = value.asInstanceOf[ArrayBuffer[String]].toArray

  override def intOpt: Option[Int] = Option(int)

  override def ints: Array[Int] = value.asInstanceOf[Array[Int]]

  override def bigIntOpt: Option[BigInt] = Option(bigInt)

  override def bigInts: Array[BigInt] = value.asInstanceOf[Array[BigInt]]

  override def doubleOpt: Option[Double] = Option(double)

  override def doubles: Array[Double] = value.asInstanceOf[Array[Double]]

  override def floatOpt: Option[Float] = Option(float)

  override def floats: Array[Float] = value.asInstanceOf[Array[Float]]

  override def longOpt: Option[Long] = Option(long)

  override def longs: Array[Long] = value.asInstanceOf[Array[Long]]

  override def boolOpt: Option[Boolean] = Option(bool)

  override def bools: Array[Boolean] = value.asInstanceOf[Array[Boolean]]

  override def shortOpt: Option[Short] = Option(short)

  override def shorts: Array[Short] = value.asInstanceOf[Array[Short]]

  override def inetAddressOpt: Option[InetAddress] = Option(inetAddress)

  override def uuidOpt: Option[UUID] = Option(uuid)

  override def instantOpt: Option[Instant] = Option(instant)

  override def timeOpt: Option[Time] = Option(time)
}
