package calc

class TwoKangaroo implements Calculation {

    Map<BigInteger, BigInteger> stepSizes = [:]
    int largestPower
    long maxDistinguishedPoint

    Map<BigInteger, BigInteger> tameKangarooSteps = [:]
    Map<BigInteger, BigInteger> wildKangarooSteps = [:]

    TwoKangaroo(BigInteger prime, BigInteger base, BigInteger residue) {
        this.base = base
        this.residue = residue
        this.prime = prime
        largestPower = prime.bitLength() - 1
        maxDistinguishedPoint = calculateDistinguishedPoint(prime)
    }

    @Override
    BigInteger calculate() {
        //calculate step sizes for the powers of 2
        calculateStepSizes()
        //Set the two kangaroos off, computing alternating terms until a collision occurs
        BigInteger collision = setKangaroosOff(prime / 2 as BigInteger, 0G)
        //From the collision, calculate the solution
        BigInteger index = calculateIndex(collision)
        println "group operations: $groupOperations"
        return index
    }

    private BigInteger calculateIndex(BigInteger roo) {
        if(roo == null) {
            return null
        }

        BigInteger wildRooIndex = wildKangarooSteps[roo]
        BigInteger tameRooIndex = tameKangarooSteps[roo]

        return (tameRooIndex - wildRooIndex).mod(prime - 1)
    }

    /**
     * @param number the value that the kangaroo is currently on
     * @return the power of two that will be the kangaroo's step size
     */
    BigInteger randFunc(BigInteger number) {
        number % largestPower
    }

    /**
     * For each power of two less than or equal to the highest power,
     * compute the corresponding step size.
     */
    void calculateStepSizes() {
        for (int i = 0; i <= largestPower; i++) {
            stepSizes.put(i as BigInteger, reducePowerModP(base, (2.power(i)) as BigInteger, prime))
        }
    }

    BigInteger setKangaroosOff(BigInteger tameRooIndex, BigInteger wildRooIndex) {
        BigInteger tameRoo = reducePowerModP(base, tameRooIndex, prime)
        BigInteger wildRoo = residue

        for (BigInteger i = 1; i < prime / 2; i++) {
            if (tameRoo < maxDistinguishedPoint) {
                tameKangarooSteps.put(tameRoo, tameRooIndex)
                if (wildKangarooSteps.keySet().contains(tameRoo)) {
                    return tameRoo
                }

            }
            if (wildRoo < maxDistinguishedPoint) {
                wildKangarooSteps.put(wildRoo, wildRooIndex)
                if (tameKangarooSteps.keySet().contains(wildRoo)) {
                    return wildRoo
                }
            }

            BigInteger tameRooStep = randFunc(tameRoo)
            BigInteger wildRooStep = randFunc(wildRoo)

            tameRoo = reduceModP(tameRoo * stepSizes[tameRooStep], prime)
            wildRoo = reduceModP(wildRoo * stepSizes[wildRooStep], prime)

            tameRooIndex += 2.power(tameRooStep).mod(prime - 1)
            wildRooIndex += 2.power(wildRooStep).mod(prime - 1)
            groupOperations += 2
        }
        return null
    }

    long calculateDistinguishedPoint(BigInteger number) {
        sqrtCeil(number) * logBigInteger(number)
    }
}
