/*Group P3-6***
Andy Tang 10139121
Ayman Inayatali Momin 30192494
Darpal Patel 30088795
Dylan Dizon 30173525
Ellen Bowie 30191922
Emil Huseynov 30171501
Ishita Udasi 30170034
Jason Very 30222040
Jesse Leinan 00335214
Joel Parker 30021079
Kear Sang Heng 30087289
Khadeeja Abbas 30180776
Kian Sieppert 30134666
Michelle Le 30145965
Raja Muhammed Omar 30159575
Sean Gilluley 30143052
Shenuk Perera 30086618
Simrat Virk 30000516
Sina Salahshour 30177165
Tristan Van Decker 30160634
Usharab Khan 30157960
YiPing Zhang 30127823*/
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
