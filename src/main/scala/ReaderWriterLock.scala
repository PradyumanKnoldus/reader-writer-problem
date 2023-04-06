package com.knoldus

import java.util.concurrent.locks.{ReadWriteLock, ReentrantReadWriteLock}
import scala.util.Try

class ReaderWriterLock {
  // Create a new ReentrantReadWriteLock with fairness enabled
  private val lock: ReadWriteLock = new ReentrantReadWriteLock(true)

  // Acquire the read lock and execute the operation, returning a Try that encapsulates the result
  def withReadLock[T](operation: => T): Try[T] = {
    val readLock = lock.readLock()
    readLock.lock() // Acquire the read lock
    val result = Try(operation)
    readLock.unlock()
    result
  }

  // Acquire the write lock and execute the operation, returning a Try that encapsulates the result
  def withWriteLock[T](operation: => T): Try[T] = {
    val writeLock = lock.writeLock()
    writeLock.lock() // Acquire the write lock
    val result = Try(operation)
    writeLock.unlock()
    result
  }
}
