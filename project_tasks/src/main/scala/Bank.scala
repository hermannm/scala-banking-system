class Bank(val allowedAttempts: Integer = 3) {

    private val transactionQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        val transaction: Transaction = new Transaction(
            transactionQueue,
            processedTransactions,
            from,
            to,
            amount,
            allowedAttempts
        );

        transactionQueue.push(transaction);

        processTransactions;
    }

    private def processTransactions: Unit = {
        if (!transactionQueue.isEmpty) {
            val transaction: Transaction = transactionQueue.pop;

            transaction.run;

            if (transaction.status == TransactionStatus.PENDING) {
                transactionQueue.push(transaction);
                processTransactions;
            } else {
                processedTransactions.push(transaction);
                processTransactions;
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
