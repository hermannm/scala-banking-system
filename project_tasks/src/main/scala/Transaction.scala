import scala.collection.mutable
import scala.util.{Either, Left, Right}

object TransactionStatus extends Enumeration {
  val UNSTARTED, PENDING, SUCCESS, FAILED = Value
}

class TransactionQueue {

    // Add datastructure to contain the transactions
    var queued_Transactions = new mutable.SynchronizedQueue[Transaction]()

    // Remove and return the first element from the queue
    def pop: Transaction = queued_Transactions.dequeue()

    // Return whether the queue is empty
    def isEmpty: Boolean = queued_Transactions.isEmpty

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = {
      queued_Transactions.enqueue(t)
    }

    // Return the first element from the queue without removing it
    def peek: Transaction = queued_Transactions.front

    // Return an iterator to allow you to iterate over the queue
    // Note: Not thread safe, do not modify queue while iterating
    def iterator: Iterator[Transaction] = queued_Transactions.iterator
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

    var status: TransactionStatus.Value = TransactionStatus.UNSTARTED
    var attempt = 0

    override def run: Unit = {
        status.synchronized {
            if (status != TransactionStatus.UNSTARTED)
                return
            status = TransactionStatus.PENDING
        }

        def doTransaction: Either[Unit, String] = {
            from.withdraw(amount) match {
                case Left(_) => {
                    to.deposit(amount) match {
                        case Left(_) => {
                            // Success!
                            Left(())
                        }
                        case error => {
                            // Put the withdrawn money back
                            from.deposit(amount)
                            error
                        }
                    }
                }
                case error => {
                    // If withdrawing failed, we stop this attempt
                    error
                }
            }
        }

        // If the transaction is not yet SUCCESS or FAILED, keep trying to perform it.
        // Other transactions can complete while we wait, making our transaction evetually succeed.
        while (status == TransactionStatus.PENDING) {
            doTransaction match {
                case Left(_) => {
                    println("Good");
                    status = TransactionStatus.SUCCESS
                }
                case error => {
                    attempt += 1
                    if (attempt >= allowedAttemps)
                        status = TransactionStatus.FAILED
                }
            }
            Thread.sleep(50) // Let other transactions be processed while we wait
        }
    }
}
