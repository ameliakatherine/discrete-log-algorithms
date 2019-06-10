import calc.TwoKangaroo
import spock.lang.Specification
import spock.lang.Unroll

class TwoKangarooSpec extends Specification {
    void "Let's make an object"() {
        given: "a TwoKangaroo calculation"
        TwoKangaroo calculation = new TwoKangaroo(17G, 2G, 3G)

        expect: "everything is properly assigned"
        calculation.prime == 17
        calculation.base == 2
        calculation.residue == 3
        calculation.largestPower == 4
    }

    void "Can we do a calculation?"() {
        given: "a TwoKangaroo calculation"
        TwoKangaroo calculation = new TwoKangaroo(59G, 3G, 19G)

        when: "we do the calc"
        BigInteger result = calculation.calculate()

        then: "the calculation method throws no errors"
        noExceptionThrown()

        and: "we get the correct result"
        result == 17
    }

    void "Sometimes the kangaroos get caught in infinite loops and no solution is reached"() {
        TwoKangaroo calculation = new TwoKangaroo(33331G, 3G, 20470G)

        when: "we do the calc"
        BigInteger result = calculation.calculate()

        then: "the solution isn't reached"
        result == null
    }

    @Unroll
    void "Now lets try a variety of number sizes"() {
        given: "a TwoKangaroo calculation"
        TwoKangaroo calculation = new TwoKangaroo(prime, base, residue)

        when: "we do the calc"
        BigInteger result = calculation.calculate()

        then: "the calculation method throws no errors"
        result == trueSolution

        where:
        prime             | base | residue           | trueSolution
        2341G             | 7G   | 583G              | 312
        104729G           | 12G  | 103896G           | 86472
        1500450271G       | 15G  | 681641065G        | 74301
        583740086009G     | 3G   | 320660271632G     | 7287482734
        //5111111111111119G | 3G   | 4815996339760291G | 283746193847283G
        //takes over 4 minutes to run final case
    }
}
