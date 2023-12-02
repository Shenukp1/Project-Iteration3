
package testingUtilities;
import com.thelocalmarketplace.hardware.test.*;

/*
 * An interface to load arbitrary products into for tests and demonstration.
 * @Param String for product name
 * @Param String for product barcode and (plu code)+1.
 * @Param int for mass
 * @Param int for cost
 * @Param int for inventory/stock
 */
	public interface LoadProductDatabases {
		
		public InventoryProductGenerator milk = new InventoryProductGenerator("milk", "1234", 3000000, 4, 100);
		public  InventoryProductGenerator beans= new InventoryProductGenerator("beans","1221", 4000000, 1, 200);
		public  InventoryProductGenerator bacon= new InventoryProductGenerator("bacon","1233", 3000000, 7, 70);
		public  InventoryProductGenerator beer= new InventoryProductGenerator("beer", "1265",3000000, 15, 100);
		public  InventoryProductGenerator chips= new InventoryProductGenerator("chips","1209", 2000000, 6, 100);
		public  InventoryProductGenerator steak= new InventoryProductGenerator("steak", "1208",5000000, 15, 40);
		public  InventoryProductGenerator onions= new InventoryProductGenerator("onions", "1207",3000000, 2,40);
		 public  InventoryProductGenerator cheese= new InventoryProductGenerator("cheese", "1205",4000000, 10, 20);
		 public  InventoryProductGenerator bigProduct= new InventoryProductGenerator("Big Product", "1202",900000000, 10, 20);
		//public LoadProductDatabases() {	}

		
		
		
	}

