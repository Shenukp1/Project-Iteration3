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
package GUI;


	public class LoadProductDatabases {
		
		static public testingGUIProductGenerator milkc = new testingGUIProductGenerator("milk", "1234", 3000, 4, 100);
		static public testingGUIProductGenerator bananas = new testingGUIProductGenerator("bananas", "4111", 1000, 1, 600);
		static public testingGUIProductGenerator cookie = new testingGUIProductGenerator("cookies", "5155", 1500, 5, 50);
		static public testingGUIProductGenerator egg = new testingGUIProductGenerator("eggs", "4444", 1660, 4, 650);
		static public testingGUIProductGenerator bag = new testingGUIProductGenerator("bag", "9999", 500, 1, 100);
		
		public static testingGUIProductGenerator getMilk() {return milkc;}
		public static testingGUIProductGenerator getBananas() {return bananas;}
		public static testingGUIProductGenerator getCookie() {return cookie;}
		public static testingGUIProductGenerator getegg() {return egg;}
}
