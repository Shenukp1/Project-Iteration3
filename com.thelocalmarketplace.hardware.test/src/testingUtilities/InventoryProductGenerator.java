package testingUtilities;

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
		
		//fields for a plu item
		public Numeral[] numeralPlu = new Numeral [4];
		public PriceLookUpCode pluCode;
		public PLUCodedProduct pluCodedProduct;
		public PLUCodedItem pluCodeditem;
		
		public InventoryProductGenerator(String productName, int productMass, int price, int inventory)  {
			
			int randomNumber;  
			String temp ="";
			// Get the current time in milliseconds
	        long currentTimeMillis = System.currentTimeMillis();

	        // Convert the time value to an integer
	        int currentTimeAsInt = (int) currentTimeMillis;
			BigInteger tempInt = new BigInteger (String.valueOf(productMass));
			this.itemName=productName;
			this.itemMass=new Mass(tempInt);
		
		for(int i=0; i<4;i++) {
		  
			currentTimeMillis = System.nanoTime();
			randomNumber =(int) ((100*Math.random()*currentTimeAsInt));
			randomNumber %=10;
			this.numeral[i]=Numeral.valueOf((byte)randomNumber);
			this.numeralPlu[i]=Numeral.valueOf((byte)((randomNumber+1)%10));
			temp += String.valueOf((randomNumber+1)%10);
		}
		pluCode= new PriceLookUpCode(temp);
		pluCodedProduct=new PLUCodedProduct(this.pluCode, productName, price);
		pluCodeditem= new PLUCodedItem(pluCode, itemMass);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(this.itemBarcode, this.barcodedProduct);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(this.pluCode, this.pluCodedProduct);
		ProductDatabases.INVENTORY.put(this.barcodedProduct, inventory);
		
		}
		
	
	}



