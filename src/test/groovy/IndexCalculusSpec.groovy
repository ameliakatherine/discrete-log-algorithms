import calc.IndexCalculus
import spock.lang.Specification
import spock.lang.Unroll

class IndexCalculusSpec extends Specification {

    @Unroll
    void "If a number is smooth, we'll factorise it"() {
        given: "an index calculus calculation"
        IndexCalculus calculation = new IndexCalculus(513 as BigInteger, 2 as BigInteger, 0 as BigInteger)
        Map<BigInteger, BigInteger> factorMap = [:]
        factorMap.putAll(map)
        expect: "The smoothness bound is properly calculated"
        calculation.smoothnessBound == 11

        and: "If a number is 11-smooth, it is factorised correctly"
        calculation.factoriseIfSmooth(number) == factorMap


        where:
        number                | map
        13 * 2 as BigInteger  | [:] //if a number isn't smooth an empty map is returned
        41 * 29 as BigInteger | [:]
        27 as BigInteger      | [2G: 0G, 3G: 3G, 5G: 0G, 7G: 0G, 11G: 0G]
        555660 as BigInteger  | [2G: 2G, 3G: 4G, 5G: 1G, 7G: 3G, 11G: 0G]
    }

    void "We can find smooth relations"() {
        given: "an index calculus calculation"
        IndexCalculus calculation = new IndexCalculus(521 as BigInteger, 2 as BigInteger, 13 as BigInteger)

        expect: "The smoothness bound is properly calculated"
        calculation.smoothnessBound == 11

        and: "If a number is 11-smooth, it is factorised correctly"
        calculation.findSmoothRelations().size() == 11
    }
}
