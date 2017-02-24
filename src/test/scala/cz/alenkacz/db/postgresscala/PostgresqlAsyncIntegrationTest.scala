package cz.alenkacz.db.postgresscala

import java.net.InetAddress
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import cz.alenkacz.db.postgresscala._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlAsyncIntegrationTest extends AsyncFlatSpec with Matchers with TestPostgresConnection {
  createTestTable()


  "Postgres scala" should "return result for basic string query" in {
    connection.execute("INSERT INTO abc (s, sa) VALUES ('a', '{\"a\", \"b\"}')").flatMap(_ => sql"SELECT s,sa FROM abc".query(r => (r(0).string, r(1).strings))).flatMap(actual => {
      actual.head should be(("a", Seq("a", "b")))
    })
  }

  it should "return result for basic int query" in {
    connection.execute("INSERT INTO abc (i, ia) VALUES (1, '{1,2}')").flatMap(_ => sql"SELECT i,ia FROM abc WHERE i IS NOT NULL".query(r => (r(0).int, r(1).ints))).flatMap(actual => {
      actual.head should be((1, Seq(1, 2)))
    })
  }

  it should "return result for basic bigint query" in {
    connection.execute("INSERT INTO abc (bi, bia) VALUES (1, '{1,2}')").flatMap(_ => sql"SELECT bi,bia FROM abc WHERE bi IS NOT NULL".query(r => (r(0).bigInt, r(1).bigInts))).flatMap(actual => {
      actual.head should be((1L, Seq(1L, 2L)))
    })
  }

  it should "return result for basic double query" in {
    connection.execute("INSERT INTO abc (d, da) VALUES (1.1, '{1.1,2.2}')").flatMap(_ => sql"SELECT d,da FROM abc WHERE d IS NOT NULL".query(r => (r(0).double, r(1).doubles))).flatMap(actual => {
      actual.head should be((1.1d, Seq(1.1d, 2.2d)))
    })
  }

  it should "return result for basic float query" in {
    connection.execute("INSERT INTO abc (f, fa) VALUES (1.1, '{1.1,2.2}')").flatMap(_ => sql"SELECT f,fa FROM abc WHERE f IS NOT NULL".query(r => (r(0).float, r(1).floats))).flatMap(actual => {
      actual.head should be((1.1f, Seq(1.1f, 2.2f)))
    })
  }

  it should "return result for basic bool query" in {
    connection.execute("INSERT INTO abc (b, ba) VALUES (true, '{true,false}')").flatMap(_ => sql"SELECT b,ba FROM abc WHERE b IS NOT NULL".query(r => (r(0).bool, r(1).bools))).flatMap(actual => {
      actual.head should be((true, Seq(true, false)))
    })
  }

  it should "return result for basic short query" in {
    connection.execute("INSERT INTO abc (sh, sha) VALUES (1, '{1,2}')").flatMap(_ => sql"SELECT sh,sha FROM abc WHERE sh IS NOT NULL".query(r => (r(0).short, r(1).shorts))).flatMap(actual => {
      actual.head should be((1, Seq(1, 2)))
    })
  }

  it should "return result for basic inet address query" in {
    connection.execute("INSERT INTO abc (ina, inaa) VALUES ('192.168.1.1', '{\"192.168.1.1\",\"192.168.1.2\"}')").flatMap(_ => sql"SELECT ina,inaa FROM abc WHERE ina IS NOT NULL".query(r => (r(0).inetAddress, r(1).inetAddresses))).flatMap(actual => {
      actual.head should be((InetAddress.getByName("192.168.1.1"), Seq(InetAddress.getByName("192.168.1.1"), InetAddress.getByName("192.168.1.2"))))
    })
  }

  it should "return result for basic uuid query" in {
    connection.execute("INSERT INTO abc (u, ua) VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '{\"a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11\",\"a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12\"}')").flatMap(_ => sql"SELECT u, ua FROM abc WHERE u IS NOT NULL".query(r => (r(0).uuid, r(1).uuids))).flatMap(actual => {
      actual.head should be((UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), Seq(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"))))
    })
  }

  it should "return result for basic bytes query" in {
    connection.execute("INSERT INTO abc (bytearray) VALUES (decode('013d7d16d7ad4fefb61bd95b765c8ceb', 'hex'))").flatMap(_ => sql"SELECT bytearray FROM abc WHERE bytearray IS NOT NULL".query(r => r(0).bytes)).flatMap(actual => {
      actual.head should be(Seq(1, 61, 125, 22, -41, -83, 79, -17, -74, 27, -39, 91, 118, 92, -116, -21).map(_.toByte))
    })
  }

  it should "return result for basic datetime query" in {
    connection.execute("INSERT INTO abc (ts, timeOnly) VALUES ('2017-1-1', '01:00:00')").flatMap(_ => sql"SELECT ts, timeOnly FROM abc WHERE ts IS NOT NULL".query(r => (r(0).instant, r(1).time))).flatMap(actual => {
      actual.head should be((Instant.parse("2017-01-01T00:00:00Z"), Time.valueOf("01:00:00")))
    })
  }

  it should "return instantOpt when instant value is missing" in {
    sql"SELECT ts FROM abc WHERE ts IS NULL LIMIT 1".query(r => r(0).instantOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return stringOpt when value is missing" in {
    sql"SELECT s FROM abc WHERE s IS NULL LIMIT 1".query(r => r(0).stringOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return intOpt when value is missing" in {
    sql"SELECT i FROM abc WHERE i IS NULL LIMIT 1".query(r => r(0).intOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return bigIntOpt when value is missing" in {
    sql"SELECT bi FROM abc WHERE bi IS NULL LIMIT 1".query(r => r(0).bigIntOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return doubleOpt when value is missing" in {
    sql"SELECT d FROM abc WHERE d IS NULL LIMIT 1".query(r => r(0).doubleOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return floatOpt when value is missing" in {
    sql"SELECT f FROM abc WHERE f IS NULL LIMIT 1".query(r => r(0).floatOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return boolOpt when value is missing" in {
    sql"SELECT b FROM abc WHERE b IS NULL LIMIT 1".query(r => r(0).boolOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return shortOpt when value is missing" in {
    sql"SELECT sh FROM abc WHERE sh IS NULL LIMIT 1".query(r => r(0).shortOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return inetaddressOpt when value is missing" in {
    sql"SELECT ina FROM abc WHERE ina IS NULL LIMIT 1".query(r => r(0).inetAddressOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return uuidOpt when value is missing" in {
    sql"SELECT u FROM abc WHERE u IS NULL LIMIT 1".query(r => r(0).uuidOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  it should "return timeOpt when value is missing" in {
    sql"SELECT timeOnly FROM abc WHERE timeOnly IS NULL LIMIT 1".query(r => r(0).timeOpt).flatMap(actual => {
      actual.head should be(None)
    })
  }

  def createTestTable() = {
    Await.result(connection.execute("""CREATE TABLE IF NOT EXISTS abc (
           id serial PRIMARY KEY,
           s text NULL,
           sa text[] NULL,
           i int NULL,
           ia int[] NULL,
           bi bigint NULL,
           bia bigint[] NULL,
           d double precision NULL,
           da double precision[] NULL,
           f real NULL,
           fa real[] NULL,
           b bool NULL,
           ba bool[] NULL,
           sh smallint NULL,
           sha smallint[] NULL,
           ina inet NULL,
           inaa inet[] NULL,
           u uuid NULL,
           ua uuid[] NULL,
           bytearray bytea NULL,
           ts timestamp NULL,
           timeOnly time NULL
         );"""), Duration(3, TimeUnit.SECONDS))
  }
}

case class TestObject(testString: String, testStringArray: Seq[String])
