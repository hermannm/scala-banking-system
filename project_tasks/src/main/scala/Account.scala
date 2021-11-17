class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    def withdraw(amount: Double): Either[Unit, String] = this.synchronized{
        if (amount < 0) return Right("Cannot withdraw a negative amount")
        if (amount > this.getBalanceAmount) return Right("Not enough funds in account")
        balance.amount -= amount
        Left(())
    }
    def deposit (amount: Double): Either[Unit, String] = this.synchronized{
        if (amount < 0) return Right("You cannot deposit a negative amount")
        balance.amount += amount
        Left(())
    }
    def getBalanceAmount: Double       = balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }


}
