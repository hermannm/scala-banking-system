import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    def withdraw(amount: Double): Either[Unit, String] = this.synchronized{
        if (amount < 0) return Right("Cannot withdraw a negative amount")
        if (amount > this.getBalanceAmount) return Right("Not enough funds in account")
        Left(balance.amount -= amount)
    }
    def deposit (amount: Double): Either[Unit, String] = this.synchronized{
        if (amount < 0) return Right("You cannot deposit a negative amount")
        Left(balance.amount += amount)
    }
    def getBalanceAmount: Double       = balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }


}
