package com.thelocalmarketplace.software.test;

import com.jjjwelectronics.card.Card.CardData;

public class CardDataStub implements CardData {
    private String type;
    private String number;
    private String cardholder;

    public CardDataStub(String type, String number, String cardholder) {
        this.type = type;
        this.number = number;
        this.cardholder = cardholder;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public String getCardholder() {
        return cardholder;
    }

    @Override
    public String getCVV() {
		return cardholder;
        // Implement if needed
    }
   
}
