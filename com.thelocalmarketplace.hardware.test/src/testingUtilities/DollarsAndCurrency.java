//Joel Parker 30021079
//Dylan Dizon 30173525
//Akash Grewal 30179657
//Nicholas McCamis 30192610 

package testingUtilities;

import java.math.BigDecimal;
import java.util.Currency;

import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;

/*
 * The fields here seem to be useful to grab for many classes. So this is better than a abstract class.
 * 
 *
 */
public interface DollarsAndCurrency {
	//Some or all of these 'banknotes' might be redundant for our simulation purposes
	public static BigDecimal fiveHundredDollars  = new BigDecimal("500");
	public static BigDecimal oneHundredDollars  = new BigDecimal("100");
	public static BigDecimal fiftyDollars = new BigDecimal("50");
	public static BigDecimal twentyDollars  = new BigDecimal("20");
	public static BigDecimal tenDollars  = new BigDecimal("10");
	public static BigDecimal fiveDollars = new BigDecimal("5");
	public static BigDecimal [] bankNoteDenominations = {fiveDollars, tenDollars, twentyDollars, fiftyDollars};

	
	
	public static BigDecimal dollar = new BigDecimal("1.00");
	public static BigDecimal twenty5cents = new BigDecimal("0.25");
	public static BigDecimal tencents = new BigDecimal("0.1");
	public static BigDecimal fivecents = new BigDecimal("0.05");
	public static BigDecimal onecent = new BigDecimal("0.01");
	public static BigDecimal [] coinDenominations = {dollar, twenty5cents, tencents, fivecents, onecent};
	public static Currency canada=Currency.getInstance("CAD");
	
	Banknote five =new Banknote(canada, fiveDollars);
	Banknote ten =new Banknote(canada, tenDollars);
	Banknote twenty =new Banknote(canada, twentyDollars);
	Banknote fifty =new Banknote(canada, fiftyDollars);
	
	Coin penny= new Coin(canada, onecent);
	Coin nickle= new Coin(canada, fivecents);
	Coin dime= new Coin(canada, tencents);
	Coin quarter= new Coin(canada, twenty5cents);
	Coin dollars = new Coin(canada, dollar);
	
	//european currency invalid
	public static BigDecimal euro = new BigDecimal("1");
	public static Currency Europe=Currency.getInstance("EUR");
	Coin euros= new Coin(Europe, euro);
	
	//european currency invalid
	public static BigDecimal euros5 = new BigDecimal("5");
	
	public Banknote euros_5 = new Banknote(Europe, fiveDollars);

	
}
