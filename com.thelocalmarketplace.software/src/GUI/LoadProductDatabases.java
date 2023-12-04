package GUI;


	public class LoadProductDatabases {
		
		static public testingGUIProductGenerator milkc = new testingGUIProductGenerator("milk", "1234", 3000000, 4, 100);
		static public testingGUIProductGenerator bananas = new testingGUIProductGenerator("bananas", "4111", 1000000, 1, 600);
		static public testingGUIProductGenerator cookie = new testingGUIProductGenerator("cookies", "5155", 1500000, 5, 50);
		static public testingGUIProductGenerator egg = new testingGUIProductGenerator("eggs", "4444", 1660000, 4, 650);
		static public testingGUIProductGenerator bag = new testingGUIProductGenerator("bag", "9999", 5000000, 1, 100);
		
		public static testingGUIProductGenerator getMilk() {return milkc;}
		public static testingGUIProductGenerator getBananas() {return bananas;}
		public static testingGUIProductGenerator getCookie() {return cookie;}
		public static testingGUIProductGenerator getegg() {return egg;}
}
