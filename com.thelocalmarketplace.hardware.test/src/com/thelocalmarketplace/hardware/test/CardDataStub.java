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
package com.thelocalmarketplace.hardware.test;

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
