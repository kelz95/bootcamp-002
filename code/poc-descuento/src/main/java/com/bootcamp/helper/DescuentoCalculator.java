package com.bootcamp.helper;

public class DescuentoCalculator {

   private double DESC_RATE;

   public DescuentoCalculator(double DESC_RATE) {
       if(validateDescuento(DESC_RATE)){
           this.DESC_RATE = DESC_RATE / 100;
       } else {
           this.DESC_RATE = 0;
       }
   }

   public double calculateDescuento(double price) {
       if(DESC_RATE == 0){
           return price;
       }
       if(validatePrice(price)) {
           return price - (price * DESC_RATE);
       }
       return 0;
   }

   public boolean validatePrice(double price) {
       return price > 0;
   }

   public boolean validateDescuento(double descuento) {
       return descuento < 100 && descuento > 0;
    }
}
