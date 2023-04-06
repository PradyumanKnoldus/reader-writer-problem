# Reader Writer Problem

This code demonstrates the implementation of a simple Reader-Writer lock using the ReentrantReadWriteLock class in Java.

The ReaderWriterLock class encapsulates the lock implementation and provides two methods, withReadLock and withWriteLock, that can be used to execute read and write operations, respectively. These methods acquire the read or write lock, execute the operation, and release the lock, ensuring that multiple threads can read the data simultaneously, while only one thread can write to it at a time.

The Driver object creates an executor with a fixed thread pool size of 10 and a blocking queue to hold the tasks. It creates two Runnable objects, one for reading data and another for writing data, and adds them to the queue in a specific order. The Runnable objects use the withReadLock and withWriteLock methods of the ReaderWriterLock class to perform the read and write operations, respectively.

The Driver object then starts the executor and processes the queue. The executor runs indefinitely, executing the Runnable objects in the order they were added to the queue. Once all the tasks in the queue are processed, the executor is shut down.

To run the code, simply run the Driver object as a Scala application. The output will display the data being read and written by the threads.
