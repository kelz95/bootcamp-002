package com.bootcamp.helper;

public class TaxCalculator {

   private double IVA_RATE;

   public TaxCalculator(double IVA_RATE) { this.IVA_RATE = IVA_RATE; }

   public double calculateIva(double price) {
       if(validatePrice(price)) {
           return validatePrice(price) * IVA_RATE / 100;
       }
       return 0;
   }

   public boolean validatePrice(double price) {
       return price > 0;
   }
}
