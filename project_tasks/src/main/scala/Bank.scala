class Bank(val allowedAttempts: Integer = 3) {

    private val transactionQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    private var processing = false;

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        val transaction: Transaction = new Transaction(
            transactionQueue,
            processedTransactions,
            from,
            to,
            amount,
            allowedAttempts
        );

        // Add the transaction to the queue, and start queue processing if not already running.
        // Importantly, the processing queue can not shut down between we add and check running state.
        this.synchronized {
            transactionQueue.push(transaction)

            if (!processing) {
                processing = true
                new Thread {
                    override def run = processTransactions
                }.start
            }
        }
    }

    private def processTransactions: Unit = {
        while (true) {

            // After this synchronized block, we either have more work to do, or have shut down the thread
            val transaction: Transaction = this.synchronized {
                if(transactionQueue.isEmpty) {
                    processing = false
                    return
                }
                transactionQueue.pop
            }

            transaction.status match {
                case TransactionStatus.UNSTARTED => {
                    new Thread(transaction).start
                    transactionQueue.push(transaction)
                }
                case TransactionStatus.PENDING => transactionQueue.push(transaction)
                case _ => processedTransactions.push(transaction)
            }
            Thread.sleep(1)
        }
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
