package calc


import java.security.SecureRandom

/**
 * This is (clearly) unfinished.
 */
class IndexCalculus implements Calculation {

    BigInteger smoothnessBound
    List<BigInteger> smoothPrimes

    IndexCalculus(BigInteger prime, BigInteger base, BigInteger residue) {
        this.prime = prime
        this.base = base
        this.residue = residue
        smoothnessBound = calculateSmoothnessBound(prime)
        smoothPrimes = findSmoothPrimes(smoothnessBound)
    }

    /**
     * Finds smooth relations of the base raised to random indices modulo the prime. We find the number of relations
     * equal to the smoothness bound, and then stop.
     * @return a map from the random index to the factorised smooth number.
     */
    Map<BigInteger, Map<BigInteger, BigInteger>> findSmoothRelations() {
        SecureRandom randomGenerator = new SecureRandom()
        Map<BigInteger, Map<BigInteger, BigInteger>> indexAndResidue = [:]

        while (indexAndResidue.size() < smoothnessBound) {
            BigInteger randomIndex = Math.abs(randomGenerator.nextInt(300))
            BigInteger residue = reducePowerModP(base, randomIndex, prime)
            groupOperations++
            Map<BigInteger, BigInteger> primeFactors = factoriseIfSmooth(residue)
            if (primeFactors != [:]) {
                indexAndResidue.put(randomIndex, primeFactors)
            }
        }
        return indexAndResidue
    }

    /**
     * If a number is smooth, as determined by the smoothness bound field, then a map is returned with its factorisation.
     * The factorisation is recorded in a map as [prime factor : power] so that if we have a smoothness bound of 8 and the
     * number 2^3*3^8*7, then the map returned is [2:3, 3:8, 5:0, 7:1]
     * @param number the number to be factorised
     * @return the factorisation of the number if it is smooth, or an empty list
     */
    Map<BigInteger, BigInteger> factoriseIfSmooth(BigInteger number) {
        if (number <= 1) {
            return [:]
        }
        Map<BigInteger, BigInteger> primeAndPower = [:]
        smoothPrimes.each { prime ->
            BigInteger power = 0
            while (!(number % prime)) {
                number /= prime
                power++
            }
            primeAndPower.put(prime, power)
        }
        if (number != 1) {
            return [:]
        } else {
            return primeAndPower
        }
    }

    @Override
    BigInteger calculate() {
        Map<BigInteger, Map<BigInteger, BigInteger>> smoothRelations = findSmoothRelations()
        return 0
    }
}


