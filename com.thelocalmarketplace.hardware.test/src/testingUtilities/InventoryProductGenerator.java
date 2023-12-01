package testingUtilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

public class InventoryProductGenerator {
	
	
		//fields for a barcode
		public Numeral[] numeral = new Numeral [4];
		public String itemName;
		public Mass itemMass;
		public Barcode itemBarcode;
		public BarcodedItem barcodedItem;
		public BarcodedProduct barcodedProduct;
		public BigDecimal bigDecimalMass; 
		
		//fields for a plu item
		public Numeral[] numeralPlu = new Numeral [4];
		public PriceLookUpCode pluCode;
		public PLUCodedProduct pluCodedProduct;
		public PLUCodedItem pluCodeditem;
		
		public InventoryProductGenerator(String productName, String codeLength, int productMass, int price, int inventory)  {
			
			
			String temp ="";
		
			char parsedString;
			// Get the current time in milliseconds
	        long currentTimeMillis = System.currentTimeMillis();

	        // Convert the time value to an integer
	        int currentTimeAsInt = (int) currentTimeMillis;
			 bigDecimalMass = new BigDecimal (String.valueOf(productMass));
			this.itemName=productName;
			this.itemMass=new Mass(bigDecimalMass);
		
		for(int i=0; i<codeLength.length();i++) {
			
			parsedString = codeLength.charAt(i);
			//codeNumber %=10;
			
			this.numeral[i]=Numeral.valueOf((byte)(Character.getNumericValue(parsedString)%10));
			this.numeralPlu[i]=Numeral.valueOf((byte)((Character.getNumericValue(parsedString)+1)%10));
			temp += String.valueOf((Character.getNumericValue(parsedString)+1)%10);
			//randomNumberSeed+=(int)(Math.random()*100);
		}
		//initialize plu fields
		pluCode= new PriceLookUpCode(temp);
		pluCodedProduct=new PLUCodedProduct(pluCode, productName, price);
		pluCodeditem= new PLUCodedItem(pluCode, itemMass);
		//initialize barcoded fields
		itemBarcode= new Barcode(numeral);
		barcodedProduct = new BarcodedProduct(itemBarcode, productName, price, productMass);
		barcodedItem = new BarcodedItem(itemBarcode, itemMass);
		//input into databases
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(this.itemBarcode, this.barcodedProduct);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(this.pluCode, this.pluCodedProduct);
		ProductDatabases.INVENTORY.put(this.barcodedProduct, inventory);
		
		}
		
	
	}



