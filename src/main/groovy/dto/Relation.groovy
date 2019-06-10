package dto

class Relation {
    BigInteger residue
    FactorisedNumber factorisedNumber

    List<BigInteger> asList(){
        List<BigInteger> factorPowers = []
        factorisedNumber.factors.each {factor ->
            factorPowers << factor.power
        }
        factorPowers << residue
    }
}
