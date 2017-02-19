package cz.alenkacz.db.postgresscala
import java.net.InetAddress
import java.sql.Time
import java.time.{Instant, LocalTime}
import java.util.UUID

import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

import scala.collection.mutable.ArrayBuffer

class PostgresAsyncDbValue(value: Any) extends DbValue {
  override def string: String = value.asInstanceOf[String]

  override def int: Int = value.asInstanceOf[Int]

  override def bigInt: Long = value.asInstanceOf[Long]

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

  override def time: Time = {
    val yodaTime = value.asInstanceOf[org.joda.time.LocalTime]
    Time.valueOf(LocalTime.of(yodaTime.getHourOfDay, yodaTime.getMinuteOfHour, yodaTime.getSecondOfMinute))
  }

  override def bytes: Seq[Byte] = value.asInstanceOf[Array[Byte]].toSeq

  override def any: Any = value

  override def stringOpt: Option[String] = Option(string)

  override def strings: Seq[String] = value.asInstanceOf[ArrayBuffer[String]].toList

  override def intOpt: Option[Int] = Option(int)

  override def ints: Seq[Int] = value.asInstanceOf[ArrayBuffer[Int]].toList

  override def bigIntOpt: Option[Long] = Option(bigInt)

  override def bigInts: Seq[Long] = value.asInstanceOf[ArrayBuffer[Long]].toList

  override def doubleOpt: Option[Double] = Option(double)

  override def doubles: Seq[Double] = value.asInstanceOf[ArrayBuffer[Double]].toList

  override def floatOpt: Option[Float] = Option(float)

  override def floats: Seq[Float] = value.asInstanceOf[ArrayBuffer[Float]].toList

  override def longOpt: Option[Long] = Option(long)

  override def longs: Seq[Long] = value.asInstanceOf[ArrayBuffer[Long]].toList

  override def boolOpt: Option[Boolean] = Option(bool)

  override def bools: Seq[Boolean] = value.asInstanceOf[ArrayBuffer[Boolean]].toList

  override def shortOpt: Option[Short] = Option(short)

  override def shorts: Seq[Short] = value.asInstanceOf[ArrayBuffer[Short]].toList

  override def inetAddressOpt: Option[InetAddress] = Option(inetAddress)

  override def uuidOpt: Option[UUID] = Option(uuid)

  override def instantOpt: Option[Instant] = Option(instant)

  override def timeOpt: Option[Time] = Option(time)

  override def inetAddresses: Seq[InetAddress] = value.asInstanceOf[ArrayBuffer[InetAddress]].toList

  override def uuids: Seq[UUID] = value.asInstanceOf[ArrayBuffer[UUID]].toList
}
