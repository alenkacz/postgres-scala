package cz.alenkacz.db.postgresscala
import java.net.InetAddress
import java.sql.Time
import java.time.{Instant, LocalTime}
import java.util.UUID

import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

import scala.collection.mutable.ArrayBuffer

class PostgresAsyncDbValue(value: Any) extends DbValue {
  override def string: String = value.asInstanceOf[String]

  def throwOnNull(value: Any): Any =
    if (value == null)
      throw new IllegalArgumentException("Trying to get value, got null instead. Use methods returning option for NULLable columns")
    else
      value

  override def int: Int = throwOnNull(value).asInstanceOf[Int]

  override def bigInt: Long = throwOnNull(value).asInstanceOf[Long]

  override def double: Double = throwOnNull(value).asInstanceOf[Double]

  override def float: Float = throwOnNull(value).asInstanceOf[Float]

  override def long: Long = throwOnNull(value).asInstanceOf[Long]

  override def bool: Boolean = throwOnNull(value).asInstanceOf[Boolean]

  override def short: Short = throwOnNull(value).asInstanceOf[Short]

  override def inetAddress: InetAddress = value.asInstanceOf[InetAddress]

  override def uuid: UUID = value.asInstanceOf[UUID]

  override def instant: Instant = value match {
    case d: DateTime => Instant.ofEpochMilli(d.getMillis)
    case d: LocalDateTime => Instant.ofEpochMilli(value.asInstanceOf[LocalDateTime].toDateTime(DateTimeZone.UTC).getMillis)
    case null => throw new IllegalArgumentException("Trying to get instant, got null instead. Use instantOpt for NULLable columns")
    case d => throw new IllegalArgumentException(s"Unsupported datetime type - expecting DateTime or LocalDatetime from joda library, actual ${d.getClass.getName}")
  }

  override def time: Time = {
    val yodaTime = throwOnNull(value).asInstanceOf[org.joda.time.LocalTime]
    Time.valueOf(LocalTime.of(yodaTime.getHourOfDay, yodaTime.getMinuteOfHour, yodaTime.getSecondOfMinute))
  }

  override def bytes: Seq[Byte] = value.asInstanceOf[Array[Byte]].toSeq

  override def any: Any = value

  override def stringOpt: Option[String] = Option(string)

  override def strings: Seq[String] = value.asInstanceOf[ArrayBuffer[String]].toList

  override def intOpt: Option[Int] = Option(value).map(_ => int)

  override def ints: Seq[Int] = value.asInstanceOf[ArrayBuffer[Int]].toList

  override def bigIntOpt: Option[Long] = Option(value).map(_ => bigInt)

  override def bigInts: Seq[Long] = value.asInstanceOf[ArrayBuffer[Long]].toList

  override def doubleOpt: Option[Double] = Option(value).map(_ => double)

  override def doubles: Seq[Double] = value.asInstanceOf[ArrayBuffer[Double]].toList

  override def floatOpt: Option[Float] = Option(value).map(_ => float)

  override def floats: Seq[Float] = value.asInstanceOf[ArrayBuffer[Float]].toList

  override def longOpt: Option[Long] = Option(value).map(_ => long)

  override def longs: Seq[Long] = value.asInstanceOf[ArrayBuffer[Long]].toList

  override def boolOpt: Option[Boolean] = Option(value).map(_ => bool)

  override def bools: Seq[Boolean] = value.asInstanceOf[ArrayBuffer[Boolean]].toList

  override def shortOpt: Option[Short] = Option(value).map(_ => short)

  override def shorts: Seq[Short] = value.asInstanceOf[ArrayBuffer[Short]].toList

  override def inetAddressOpt: Option[InetAddress] = Option(inetAddress)

  override def uuidOpt: Option[UUID] = Option(uuid)

  override def instantOpt: Option[Instant] = if (value == null) None else Option(instant)

  override def timeOpt: Option[Time] = Option(value).map(_ => time)

  override def inetAddresses: Seq[InetAddress] = value.asInstanceOf[ArrayBuffer[InetAddress]].toList

  override def uuids: Seq[UUID] = value.asInstanceOf[ArrayBuffer[UUID]].toList
}
