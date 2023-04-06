package com.knoldus

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class ReaderWriterLockSpec extends AnyFlatSpec with Matchers {
  "withReadLock" should "execute the operation and return a Try with the result" in {
    val rwLock = new ReaderWriterLock
    val data = List(1, 2, 3, 4)

    rwLock.withReadLock {
      data.sum
    } shouldEqual Success(10)
  }

  it should "release the lock after executing the operation" in {
    val rwLock = new ReaderWriterLock

    rwLock.withReadLock {
      Thread.sleep(500)
    }

    // Attempt to acquire the read lock again after a delay
    rwLock.withReadLock {
      1 + 1
    } shouldEqual Success(2)
  }

  "withWriteLock" should "execute the operation and return a Try with the result" in {
    val rwLock = new ReaderWriterLock
    val data = collection.mutable.Buffer(1, 2, 3, 4)

    rwLock.withWriteLock {
      data += 5
      data
    } shouldEqual Success(Seq(1, 2, 3, 4, 5))
  }

  it should "release the lock after executing the operation" in {
    val rwLock = new ReaderWriterLock

    rwLock.withWriteLock {
      Thread.sleep(500)
    }

    // Attempt to acquire the write lock again after a delay
    rwLock.withWriteLock {
      1 + 1
    } shouldEqual Success(2)
  }

  it should "not allow concurrent writes" in {
    val rwLock = new ReaderWriterLock
    val data = collection.mutable.Buffer(1, 2, 3, 4)

    val t1 = new Thread {
      override def run(): Unit = {
        // This block will block until the write lock is released
        rwLock.withWriteLock {
          Thread.sleep(5000)
        }
      }
    }

    val t2 = new Thread {
      override def run(): Unit = {
        // This write should fail because the write lock is still held by the previous block
        rwLock.withWriteLock {
          data += 5
          data
        } shouldBe a[Failure[_]]
      }
    }

    // Start the first thread and wait for it to finish
    t1.start()
    t1.join()

    // Start the second thread
    t2.start()
    t2.join()
  }


  it should "allow concurrent reads" in {
    val rwLock = new ReaderWriterLock
    val data = List(1, 2, 3, 4)

    // This block will hold the read lock for 5 seconds
    rwLock.withReadLock {
      Thread.sleep(5000)
    }

    // This read should succeed because the lock is held for reading, not writing
    rwLock.withReadLock {
      data.sum
    } shouldEqual Success(10)
  }
}

