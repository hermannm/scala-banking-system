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
            this.synchronized {
                if(transactionQueue.isEmpty) {
                    processing = false
                    return
                }
            }
            val transaction: Transaction = transactionQueue.pop

            transaction.run

            if (transaction.status == TransactionStatus.PENDING) {
                transactionQueue.push(transaction)
            } else {
                processedTransactions.push(transaction)
            }            
        }
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
