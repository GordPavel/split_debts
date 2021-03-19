package my.org.splitwise

import java.math.BigDecimal
import java.util.function.BinaryOperator
import java.util.function.BinaryOperator.minBy
import kotlin.collections.MutableMap.MutableEntry

class SplitwiseService {
    fun splitWise(party: Collection<Spending>): List<Transaction> {
        val spends = party
            .groupingBy { it.payer }
            .fold(BigDecimal.ZERO) { accumulator, spent ->
                accumulator.add(spent.payees.sumOf { it.first })
            }
        val consumes = party
            .asSequence()
            .flatMap { it.payees }
            .groupingBy { it.second }
            .fold(BigDecimal.ZERO) { accumulator, consume ->
                accumulator.add(consume.first)
            }
        val balances = (spends.asSequence() + consumes.asSequence())
            .groupBy({ it.key }) { it.value }
            .mapValues { (_, spentAndConsume) -> spentAndConsume[0] - spentAndConsume[1] }
        assert(balances.values.sumOf { it } == BigDecimal.ZERO) { "Common balance should be zero" }

        val (payees, payers) = balances.asSequence().partition { it.value >= BigDecimal.ZERO }
        val payeesIterator = payees
            .asSequence()
            .map { it.key entry it.value }
            .filter { it.value != BigDecimal.ZERO }
            .sortedByDescending { it.value }
            .iterator()
        val payersIterator = payers
            .asSequence()
            .map { it.key entry it.value.abs() }
            .sortedByDescending { it.value }
            .iterator()

        val transactions = mutableListOf<Transaction>()
        var transactionsCounter = 0
        if (payersIterator.hasNext() && payeesIterator.hasNext()) {
            var payer = payersIterator.next()
            var payee = payeesIterator.next()
            while (payer.value != BigDecimal.ZERO && payee.value != BigDecimal.ZERO) {
                val (person, minimalAmount) = minimalAmountGetter.apply(payee, payer)
                transactions.add(
                    Transaction(
                        (++transactionsCounter).toString(),
                        payer.key.partyId,
                        payer.key,
                        payee.key,
                        minimalAmount
                    )
                )
                payer.value -= minimalAmount
                payee.value -= minimalAmount
                if (person == payee.key && payeesIterator.hasNext()) payee = payeesIterator.next()
                else if (payersIterator.hasNext()) payer = payersIterator.next()
            }
        }

        return transactions.toList()
    }
}

private infix fun <A, B> A.entry(that: B): Entry<A, B> = Entry(this, that)

private val minimalAmountGetter: BinaryOperator<Entry<Person, BigDecimal>> = minBy { amount1, amount2 ->
    amount1.value.compareTo(amount2.value)
}

private class Entry<K, V>(override val key: K, override var value: V) : MutableEntry<K, V> {
    override fun setValue(newValue: V): V {
        val oldValue = value
        value = newValue
        return oldValue
    }
}