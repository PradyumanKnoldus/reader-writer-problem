package com.knoldus

import java.util.concurrent.{Executors, LinkedBlockingQueue}
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Random, Success}

object Driver extends App {

  private val readWriteLock = new ReaderWriterLock()
  private val data = ListBuffer[Int]()

  // Create an executor with a fixed thread pool size of 10, and a blocking queue to hold the tasks
  private val executor = Executors.newFixedThreadPool(10)
  private val queue = new LinkedBlockingQueue[Runnable]()

  // Runnable for reading data
  private val reader = new Runnable {
    override def run(): Unit = {
      // Acquire the read lock, read the data, and release the lock
      readWriteLock.withReadLock {
        data
      } match {
        case Success(data) => println(s"Reading: Data in $data")
        case Failure(ex) => println(s"Read operation failed with exception: $ex")
      }
    }
  }

  // Runnable for writing data
  private val writer = new Runnable {
    override def run(): Unit = {
      // Acquire the write lock, update the data, and release the lock
      readWriteLock.withWriteLock {
        val randomNumber = Random.nextInt(100)
        data += randomNumber
      } match {
        case Success(_) => println(s"Writing: Updated Data!")
        case Failure(ex) => println(s"Write operation failed with exception: $ex")
      }
    }
  }

  // Add the reader and writer threads to the queue
  queue.put(reader)
  queue.put(writer)
  queue.put(reader)
  queue.put(reader)
  queue.put(writer)
  queue.put(writer)
  queue.put(reader)
  queue.put(reader)
  queue.put(writer)
  queue.put(reader)

  // Start the executor and process the queue
  executor.execute(() => {
    while (true) {
      val task = queue.take()
      task.run()
    }
  })

  // Shutdown the executor
  executor.shutdown()

}
