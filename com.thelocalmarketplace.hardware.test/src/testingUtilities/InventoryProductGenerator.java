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
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

public class InventoryProductGenerator {

	// fields for a barcode
	public Numeral[] numeral = new Numeral[4];
	public String itemName;
	public Mass itemMass;
	public Barcode itemBarcode;
	public BarcodedItem barcodedItem;
	public BarcodedProduct barcodedProduct;
	public BigDecimal bigDecimalMass;

	// fields for a plu item
	public Numeral[] numeralPlu = new Numeral[4];
	public PriceLookUpCode pluCode;
	public PLUCodedProduct pluCodedProduct;
	public PLUCodedItem pluCodeditem;
	public Product productSuper; // needed to keep track of inventory of plu and barcode items. Because they
	public Item item;								// could be mixed

	public InventoryProductGenerator(String productName, String codeLength, int productMass, int price, int inventory) {
		//for pluCode
		String temp = "";
		//to parse the codelength parameter
		char parsedString;
		
		bigDecimalMass = new BigDecimal(String.valueOf(productMass));
		this.itemName = productName;
		this.itemMass = new Mass(bigDecimalMass);

		//parses string into numeral barcode and plucode
		for (int i = 0; i < codeLength.length(); i++) {

			parsedString = codeLength.charAt(i);

			this.numeral[i] = Numeral.valueOf((byte) (Character.getNumericValue(parsedString) % 10));
			this.numeralPlu[i] = Numeral.valueOf((byte) ((Character.getNumericValue(parsedString) + 1) % 10));
			temp += String.valueOf((Character.getNumericValue(parsedString) + 1) % 10);
		
		}
		// initialize plu fields
		pluCode = new PriceLookUpCode(temp);
		pluCodedProduct = new PLUCodedProduct(pluCode, productName, price);
		pluCodeditem = new PLUCodedItem(pluCode, itemMass);
		// initialize barcoded fields
		itemBarcode = new Barcode(numeral);
		barcodedProduct = new BarcodedProduct(itemBarcode, productName, price, productMass);
		barcodedItem = new BarcodedItem(itemBarcode, itemMass);
		item = barcodedItem;
		// make a super product for the inventory

		// input into database
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(this.itemBarcode, this.barcodedProduct);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(this.pluCode, this.pluCodedProduct);
		ProductDatabases.INVENTORY.put(this.barcodedProduct, inventory);
		ProductDatabases.INVENTORY.put(this.pluCodedProduct, inventory);
	}

}
