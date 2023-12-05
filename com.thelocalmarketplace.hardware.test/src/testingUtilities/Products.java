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

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

public class Products   {
	//public BarcodedProduct(Barcode barcode, String description, long price, double expectedWeightInGrams) 
	//these seem to be near the minimum fields to declare a item
		public Numeral[] beansNumeral = new Numeral [4];
		public Barcode beanBarcode;
		public BarcodedItem beanBarcodeItem;
		public BarcodedItem bigItem; //to overload scale
		public BarcodedProduct beanBarcodedProduct;
		public BarcodedProduct bigProduct;
		public Mass beansMass;
		public Mass bigItemMass;
		public Mass membershipCardMass;
		public Item Beans;
		public BigInteger bigIBeanMass = new BigInteger("5000000");
		public BigInteger excessiveMass= new BigInteger("5000000000000");
		public BarcodedItem bag;
		public Item bags;
		public BarcodedItem membershipCard;
		//big integer is needed to declare item mass. can use string but not integers
		
		//this datatype has several constructors but no integers. why!
		
		//add product to database. because static might be redundant
		//public ProductDatabases productDatabase1;	
		
	//the thought is that the constructor makes the items to be bought..
	//simple for first iteration but might need double arrays to keep track of item stock. 	
		public Products (){
			//need each number in array to be declared separately, until there is a simpler way...
		beansNumeral [0]=Numeral.one;
		beansNumeral [1]=Numeral.two;
		beansNumeral [2]=Numeral.three;
		beansNumeral [3]=Numeral.four;
		
		beansMass= new Mass(bigIBeanMass);
		beanBarcode=new Barcode (beansNumeral);
		beanBarcodeItem= new BarcodedItem (beanBarcode, beansMass);
		membershipCard = new BarcodedItem(beanBarcode, beansMass);
		
		beanBarcodedProduct=new BarcodedProduct(beanBarcode, "beans", 5,bigIBeanMass.intValue());
		
		bigItemMass = new Mass(excessiveMass);
		bigItem = new BarcodedItem(beanBarcode, bigItemMass);
		bigProduct=new BarcodedProduct(beanBarcode, "big product", 5,50000);
		
		//example of upcasting for bags item. Work around because item has no accessible constructor i think.
		bag= new BarcodedItem (beanBarcode, beansMass);
		bags= bag;
		//one field implementation for a product database. might need the others written below
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(beanBarcode, beanBarcodedProduct);}
		
		
			
			
			
		}
		
		/**
		 * The known PLU-coded products, indexed by PLU code.
		 */
		//public static final Map<PriceLookUpCode, PLUCodedProduct> PLU_PRODUCT_DATABASE = new HashMap<>();

		/**
		 * The known barcoded products, indexed by barcode.
		 */
		//public static final Map<Barcode, BarcodedProduct> BARCODED_PRODUCT_DATABASE = new HashMap<>();

		/**
		 * A count of the items of the given product that are known to exist in the
		 * store. Of course, this does not account for stolen items or items that were
		 * not correctly recorded, but it helps management to track inventory.
		 */
		//public static final Map<Product, Integer> INVENTORY = new HashMap<>();
		
