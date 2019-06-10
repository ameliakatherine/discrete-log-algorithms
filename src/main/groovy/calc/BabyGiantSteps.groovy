package calc

class BabyGiantSteps implements Calculation {
    BigInteger upperBound
    Map<BigInteger, Integer> babySteps = [:]

    BabyGiantSteps(BigInteger prime, BigInteger base, BigInteger residue) {
        this.prime = prime
        this.base = base
        this.residue = residue
        upperBound = sqrtCeil(prime)
        babySteps.put(base, 1)
    }

    BigInteger calculate() {
        //1. Compute and store all baby-steps
        computeBabySteps()

        //2. Sort baby-steps
        babySteps.sort()

        //3. Compute giant-steps until we find a match
        Map<BigInteger, BigInteger> giantStepIndexAndValue = findMatchingGiantStep()

        //4. Use the match to calculate the discrete logarithm
        BigInteger commonValue = giantStepIndexAndValue.keySet().first()
        Integer babyStepIndex = babySteps[commonValue]
        BigInteger giantStepIndex = giantStepIndexAndValue[commonValue]

        println "group operations: $groupOperations"
        //5. return the discrete logarithm
        return babyStepIndex + giantStepIndex
    }

    /**
     * Populate the field that maps baby-step value to its index. Each term is calculated
     * as base^i (mod prime) with 0<i<k
     */
    void computeBabySteps() {
        BigInteger lastNumber = base
        for (int i = 2; i < upperBound; i++) {
            BigInteger nextNumber = reduceModP(lastNumber * base, prime)
            groupOperations++
            babySteps.put(nextNumber, i)
            lastNumber = nextNumber
        }
    }

    /**
     * Find the first giant-step term that is the same as a baby-step value.
     * The giant-steps are calculate as residue*base^(-k)i with 0<i<k
     * @return the pair of matching value and index
     */
    Map<BigInteger, BigInteger> findMatchingGiantStep() {
        Map<BigInteger, BigInteger> response = [:]
        BigInteger inverseBase = base.modInverse(prime)
        BigInteger inverseBasePower = reducePowerModP(inverseBase, upperBound, prime)
        BigInteger lastNumber = reduceModP(inverseBasePower * residue, prime)
        groupOperations += 2
        for (int i = 1; i < upperBound; i++) {
            if (babySteps.keySet().contains(lastNumber)) {
                response.put(lastNumber, i * upperBound)
                return response
            }
            BigInteger nextNumber = reduceModP(lastNumber * inverseBasePower, prime)
            groupOperations++
            lastNumber = nextNumber
        }
        //if the problem was defined incorrectly and there is no match,
        //an empty list is returned
        return [:]
    }
}
