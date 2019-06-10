package dto

class FactorisedNumber {
    List<PowerTerm> factors

    BigInteger evaluate() {
        BigInteger number = 1
        factors.each { factor ->
            number*=factors.evaluate()
        }
        return number
    }
}
