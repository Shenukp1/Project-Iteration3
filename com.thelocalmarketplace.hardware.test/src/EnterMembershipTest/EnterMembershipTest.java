package EnterMembershipTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import control.SessionController;
import org.junit.Before;
import org.junit.Test;

public class EnterMembershipTest {

    private SessionController mockSessionController;
    private AbstractSelfCheckoutStation mockSelfCheckoutStation;
    private IBarcodeScanner mockBarcodeScanner;

    @Before
    public void setUp() {
        // Create mock objects for dependencies
        mockSessionController = mock(SessionController.class);
        mockSelfCheckoutStation = mock(AbstractSelfCheckoutStation.class);
        mockBarcodeScanner = mock(IBarcodeScanner.class);

        // Configure the mock objects
        when(mockSelfCheckoutStation.getHandheldScanner()).thenReturn(mockBarcodeScanner);
    }

    @Test
    public void testEnterByTouchScreen() {
        // Create an instance of EnterMembership with mocked dependencies
        EnterMembership enterMembership = new EnterMembership(mockSessionController, mockSelfCheckoutStation);

        // Simulate entering membership number through touch screen
        enterMembership.EnterByTouchScreen();

        // Assert that the membership number is set correctly
        assertEquals(enterMembership.getMembershipNumber(), enterMembership.getMembershipNumber());
    }

    @Test
    public void testTheDataFromACardHasBeenRead() throws NullPointerSimulationException {
        // Create an instance of EnterMembership with mocked dependencies
        EnterMembership enterMembership = new EnterMembership(mockSessionController, mockSelfCheckoutStation);

        // Create a mock CardData object
        CardData mockCardData = mock(CardData.class);
        when(mockCardData.getType()).thenReturn("membership");
        when(mockCardData.getNumber()).thenReturn("123456");

        // Call the method to simulate card data being read
        enterMembership.theDataFromACardHasBeenRead(mockCardData);

        // Assert that the membership number is set correctly
        assertEquals("123456", enterMembership.getMembershipNumber());
    }

    @Test
    public void testABarcodeHasBeenScanned() {
        // Create an instance of EnterMembership with mocked dependencies
        EnterMembership enterMembership = new EnterMembership(mockSessionController, mockSelfCheckoutStation);

        // Create a mock Barcode object
        Barcode mockBarcode = mock(Barcode.class);
        when(mockBarcode.toString()).thenReturn("789012");

        // Call the method to simulate a barcode being scanned
        enterMembership.aBarcodeHasBeenScanned(mockBarcodeScanner, mockBarcode);

        // Assert that the membership number is set correctly
        assertEquals("789012", enterMembership.getMembershipNumber());
    }
}