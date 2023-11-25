package testingUtilities;

import java.math.BigDecimal;
import java.util.Map;


import java.util.HashMap;

/*
 * Represents a abstract amount of money the customer has to dispose.
 * 
 * 
 */
public class Wallet implements DollarsAndCurrency {
	/*
	 * orderedWallet carry's a BigDecimal denomination as its key like a dollar. The
	 * Integer is the number of that specific denomination in the map.
	 */
	public Map<BigDecimal, Integer> orderedWallet;
	public int numberOfDollars;
	public int numberOfCents;
	
	public Wallet() {}

	/*
	 * Simple constructor of a wallet object.
	 */
	public void singleDollars(int numberOfDollarsin) {
		numberOfDollars = numberOfDollarsin;
		orderedWallet = new HashMap();
		orderedWallet.put(dollar, numberOfDollars);
	}
	
	public void cash (int fiver, int ten, int twenty, int fifty, int onehundred) {
		numberOfDollars= fiver*5+ten*10+twenty*20+fifty*50+onehundred*100;
		
		orderedWallet = new HashMap();
		/*
		 * filling up the wallet with bills. might need to use an object instead of int, hopefully not
		 */
		orderedWallet.put(fiveDollars, fiver);
		orderedWallet.put(tenDollars, ten);
		orderedWallet.put(twentyDollars, twenty);
		orderedWallet.put(fiftyDollars, fifty);
		orderedWallet.put(oneHundredDollars, onehundred);
		
	}
	public void coins (int penny, int nickle, int dime, int quarter, int dollars) {
		numberOfCents= penny+nickle*5+dime*10+quarter*25+dollars*100;
		
		orderedWallet = new HashMap();
		/*
		 * filling up the wallet with bills. might need to use an object instead of int, hopefully not
		 */
		orderedWallet.put(onecent, penny);
		orderedWallet.put(fivecents, nickle);
		orderedWallet.put(tencents, dime);
		orderedWallet.put(twenty5cents, quarter);
		orderedWallet.put(dollar, dollars);
		
	}
	
	
}
