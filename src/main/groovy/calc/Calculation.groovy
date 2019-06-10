package calc

/**
 * This trait is implemented by algorithms that solves a discrete logarithm problem
 */
trait Calculation {
    BigInteger prime
    BigInteger base
    BigInteger residue
    int groupOperations = 0

    abstract BigInteger calculate()

    /**
     * Take the ceiling of the square root of a bit integer
     * @param number the number to be square rooted
     * @return the ceiling of the square root of the number
     */
    static BigInteger sqrtCeil(BigInteger number) {
        Integer bitLength = (number.bitLength() / 2)
        BigInteger div = BigInteger.ZERO.setBit(bitLength)
        BigInteger div2 = div
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for (; ;) {
            BigInteger y = div.add(number.divide(div)).shiftRight(1)
            if (y == div2 || y == div) {
                return y + 1
                //we are finding the ceiling of the square root of primes so it's ok to always add one
                //if the number is a perfect square then this method will give sqrt + 1
            }
            div2 = div
            div = y
        }
    }

    /**
     * This method uses repeated squaring to solve the modular congruence
     *
     *              power
     *          base       (mod modulo).
     *
     * We use the fact that a number written in  binary is a product of powers of twos. First, we find the
     * largest x such that 2^x is less than the power and used repeated squaring to evaluate the base^{2^x}.
     * Then we repeat for power - 2^x and multiply the terms together.
     *
     * So if we wish to find 5^{51} (mod 7), we find that 51 = 32 + 16 + 2 + 1 and therefore evaluate
     * (5^2)^6 (mod 7) * (5^2)^5 (mod 7) * (5^2)^1 (mod 7) * (5^2)^0 (mod 7), reducing mod 7 after each operation.
     *
     * @param base the base of the exponentiation
     * @param power the power we wish
     * @param modulo
     * @return
     */
    BigInteger reducePowerModP(BigInteger base, BigInteger power, BigInteger modulo) {
        BigInteger remainingPower = power
        int bits = power.bitLength()
        BigInteger result = 1

        while (remainingPower > 0) {
            BigInteger index = 2
            BigInteger workingResult = base

            //repeatedly square up until the exponent will exceed the desired power if we continue
            while (index <= remainingPower) {
                workingResult = reduceModP(workingResult * workingResult, modulo)
                index*=2
            }
            result = reduceModP(result * workingResult, modulo)
            remainingPower = (remainingPower - Math.pow(2,(bits-1))) as BigInteger
            bits = remainingPower.bitLength()
        }
        return result
    }

    /**
     * @return residue as a least residue (mod reside)
     */
    BigInteger reduceModP(BigInteger residue, BigInteger modulo) {
        residue.mod(modulo)
    }

    /**
     * Returns all primes less than or equal to the smoothness bound
     * @param smoothnessBound upper bound for primes
     * @return list of smooth primes
     */
    static List<BigInteger> findSmoothPrimes(BigInteger smoothnessBound) {
        List<BigInteger> smoothPrimes = []
        BigInteger nextPrime = 2

        while (nextPrime <= smoothnessBound) {
            smoothPrimes << nextPrime
            nextPrime = nextPrime.nextProbablePrime()
        }
        return smoothPrimes
    }

    protected static final double LOG2 = Math.log(2.0)
    protected static final int MAX_DIGITS_2 = 977 // ~ MAX_DIGITS_EXP/LN(2)

    /**
     * Computes the natural logarithm of a BigInteger.
     *
     * Works for really big integers (practically unlimited), even when the argument
     * falls outside the <tt>double</tt> range
     *
     * Returns Nan if argument is negative, NEGATIVE_INFINITY if zero.
     *
     * @param number Argument
     * @return Natural logarithm, as in <tt>Math.log()</tt>
     */
    static double logBigInteger(BigInteger number) {
        if (number.signum() < 1)
            return number.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY
        int bitLength = number.bitLength() - MAX_DIGITS_2 // any value in 60..1023 works ok here
        if (bitLength > 0)
            number = number.shiftRight(bitLength)
        double res = Math.log(number.doubleValue())
        return bitLength > 0 ? res + bitLength * LOG2 : res
    }

    /**
     * Given a large prime, calculate the smoothness bound to be used in the index calculus
     * algorithm.
     * @param number
     * @return
     */
    BigInteger calculateSmoothnessBound(BigInteger number) {
        double log = logBigInteger(number)
        double powerTerm = Math.sqrt(log * Math.log(log)) * (1 / Math.sqrt(2))
        return Math.exp(powerTerm).round() as BigInteger
    }
}