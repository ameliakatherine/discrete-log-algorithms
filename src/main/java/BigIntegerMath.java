import java.math.BigInteger;
import java.util.Random;

public class BigIntegerMath {
	static final BigInteger ZERO = new BigInteger("0");
	static final BigInteger ONE = new BigInteger("1");
	static final BigInteger TWO = new BigInteger("2");
	static final BigInteger THREE = new BigInteger("3");
	static final BigInteger negONE = new BigInteger("-1");
	
	//a^-1 mod m when gcd(a, m)=1
	public static BigInteger modInv(BigInteger a, BigInteger m) {
      if(a.compareTo(ZERO) == 0)
    	  return ZERO;
     //System.out.print("\n\nWhat is a= "+a+"\n"); bug fixed
      return a.modInverse(m);
   }
	
	//the least non-negative residues modulo m (the remainder)
   	public static BigInteger lnr(BigInteger a, BigInteger b) {
      BigInteger temp=a.mod(b);
      if(temp.compareTo(ZERO)<0)
    	  temp.add(b);

      return temp;
   }
   	
	//the Square root
	//got model from text book then created big integer version
	public static BigInteger sqrt(BigInteger n){
		BigInteger r = BigInteger.ZERO;
		BigInteger m = r.setBit(2 * n.bitLength());
		BigInteger nr;

		do {
			nr = r.add(m);
			if (nr.compareTo(n) != 1) {
				n = n.subtract(nr);
				r = nr.add(m);
			}
			r = r.shiftRight(1);
			m = m.shiftRight(2);
		} while (m.bitCount() != 0);

		return r;
	}
   
	//Chinese remainder theorem 
   	//k= (a-b)*n^-1 mod m  and x =b+nk
   	//it going return Chinese remainder theorem of to array1 and array2 
	public static BigInteger[] solveCRT(BigInteger[] logarithm1,BigInteger[] logarithm2, BigInteger[] modulus) {
		
		BigInteger[] result = new BigInteger[logarithm1.length], residue=new BigInteger[2];	
		BigInteger solution = ZERO, M=ONE;
		
		// get the product of the modulus
		for (int i = 0; i < modulus.length; i++)M = M.multiply(modulus[i]);
		
		for(int ix=0; ix<logarithm1.length;ix++){
			
			residue[0]=logarithm1[ix];residue[1]=logarithm2[ix];
			
			// compute Chinese Remainder Theorem
			for (int i = 0; i < residue.length; i++) {
				BigInteger Mi = M.divide(modulus[i]);
				solution = solution.add( residue[i].multiply(Mi).multiply(Mi.modInverse(modulus[i])));
			}
			// store the result in array
			result[ix] = lnr(solution, M);
			//reset solution 
			solution=ZERO;
		}//end of for loop
		
		return result;
	}
	
    
	//Euler's criterion
	public static boolean eulerCriterion(BigInteger g,BigInteger p){
		BigInteger seven= new BigInteger("7");;
		BigInteger eight= new BigInteger("8");;

		if(g.equals(TWO)){
			if(p.mod(eight).equals(ONE)||p.mod(eight).equals(seven)) 
				return true;
			else 
				return false;
		}

		if(g.modPow((p.subtract(ONE).divide(TWO)), p).equals(ONE))
			return true;

		return false;
	}
	
	//brute force factoring with respect to factor base
	//going from lease to greatest. 
	public static int[] factorWRTFB(int fb[], BigInteger n){	
		int expCount=0,ix=0,exp[]= new int [fb.length];
			
        while(ix< fb.length && !(n.equals(ONE)) ){
            while(n.mod(BigInteger.valueOf(fb[ix])).equals(ZERO)){
            	expCount++;
            	n=n.divide(BigInteger.valueOf(fb[ix]));
            }
            exp[ix]=expCount;
            ix++;expCount=0;
        }
        /*if it was not smooth then we set the first element 
        to negative on to tell main program it was not smooth*/
        if(!(n.equals(ONE)))exp[0]=-1;
     
        //if get this far the it is a smooth factor base
        return exp;
	}
	
	//brute force factoring with respect to factor base
	//going from greatest to least.
	public static int[] factorWRTFBGTOL(int fb[], BigInteger n){	
		int expCount=0,ix=fb.length-1,exp[]= new int [fb.length];
			
        while(ix>= 0 && !(n.equals(ONE) )){
            while(n.mod(BigInteger.valueOf(fb[ix])).equals(ZERO)){
            	expCount++;
            	n=n.divide(BigInteger.valueOf(fb[ix]));
            }
            exp[ix]=expCount;
            ix--;expCount=0;
        }
        /*if it was not smooth then we set the first element 
        to negative on to tell main program it was not smooth*/
        if(!(n.equals(ONE)))exp[0]=-1;
        //if get this far the it is a smooth factor base
        return exp;
	}

	//Test to see if a g^x mod p if g is primitive root 
    public static boolean primitiveRootTest(BigInteger root, BigInteger p){ 
        BigInteger remainder = root.modPow(p.subtract(BigInteger.ONE), p);
        return ( remainder.equals(ONE) ) ? true : false;
    }
    //uses to much memory for large 
    public static void SieveOfEratosthenes(int n){
    	boolean[] prime=new boolean[n];
    	int i=0, squareRootN=(int) Math.floor(Math.sqrt(n)), count=0;
    	
    	for(i=2;i<n;i++){ prime[i]=true;}
    	
    	for(int divisor=2;divisor<=squareRootN;divisor++){
    		if(prime[divisor]){
    			for(i=2*divisor;i<n;i=i+divisor){
    				prime[i]=false;
    			}
    		}
    	}
    	
    	for(i=2;i<n;i++){
    		if(prime[i]){
    			//System.out.print(i+", ");
    			++count;
    		}
    	}
    	System.out.println("count= "+count);
    }
    

    public static BigInteger pollardRho(BigInteger n){
    	if (n.compareTo(THREE)<=0)
			throw new IllegalArgumentException("Integer must be larger than three!");
		Random rand=new Random();
		BigInteger power=BigInteger.valueOf(1);
		BigInteger residue=lnr(BigInteger.valueOf(rand.nextInt()),n); 
		BigInteger test=residue.subtract(ONE);
		BigInteger gcd=test.gcd(n);
		while (true) {
			while (gcd.equals(ONE)) { 
				power=power.add(ONE); 
				residue=residue.modPow(power,n); 
				test=residue.subtract(ONE); 
				gcd=test.gcd(n);
			}
			if (gcd.equals(n)) {
				power=BigInteger.valueOf(1);
				residue=lnr(BigInteger.valueOf(rand.nextInt()),n);
				test=residue.subtract(ONE);
				gcd=test.gcd(n);
			} 
			else return gcd;
		}
	}
		
    	
}
