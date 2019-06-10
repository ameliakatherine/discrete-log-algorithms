import calc.BabyGiantSteps
import spock.lang.Specification
import spock.lang.Unroll

class CalculationSpec extends Specification {

    @Unroll
    void "Does the ceil square root function really work? let's see"() {
        given: "a bunch of numbers"
        BigInteger squareRoot = BabyGiantSteps.sqrtCeil(number)

        expect: "the square roots are properly calculated"
        squareRoot == trueSquareRoot

        where:
        number                        | trueSquareRoot
        65 as BigInteger              | 9
        198723 as BigInteger          | 446
        250834879281923 as BigInteger | 15837768
    }

    @Unroll
    void "Can we reduce mod p? stay tuned to find out"() {
        given: "a modular congruence"
        BabyGiantSteps calculation = new BabyGiantSteps(prime, 2 as BigInteger, 0 as BigInteger)
        BigInteger leastResidue = calculation.reduceModP(residue, prime)

        expect: "The least residue is properly calculated"
        leastResidue == trueLeastResidue

        where:
        prime            | residue                 | trueLeastResidue
        59 as BigInteger | 60 as BigInteger        | 1
        7 as BigInteger  | 52 as BigInteger        | 3
        83 as BigInteger | 102948231 as BigInteger | 11
    }

    @Unroll
    void "Modular exponentiation is correclty calculated"() {
        given: "a modular congruence"
        BabyGiantSteps calculation = new BabyGiantSteps(prime, 2 as BigInteger, 0 as BigInteger)
        BigInteger leastResidue = calculation.reducePowerModP(residue, power, prime)

        expect: "the least residue is properly calculated"
        leastResidue == trueLeastResidue

        where:
        prime                 | residue | power           | trueLeastResidue
        7G                    | 6G      | 4G              | 1G
        83G                   | 81G     | 11G             | 27G
        33331G                | 27983G  | 18798G          | 11058G
        12764787846358441471G | 13G     | 19837481873476G | 5467997445388831343G
    }

    @Unroll
    void "We can find smooth primes up to a specified bound"() {
        given: "an object that implements the calc.Calculation trait"
        List<Integer> smoothPrimes = BabyGiantSteps.findSmoothPrimes(number)

        expect: "expect the primes are found"
        smoothPrimes == truePrimeList

        where:
        number           | truePrimeList
        4 as BigInteger  | [2, 3] as List<BigInteger>
        12 as BigInteger | [2, 3, 5, 7, 11] as List<BigInteger>
        52 as BigInteger | [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47] as List<BigInteger>
    }
}
