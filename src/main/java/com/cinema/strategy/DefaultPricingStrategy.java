package com.cinema.strategy;
import org.springframework.stereotype.Component;
@Component public class DefaultPricingStrategy implements PricingStrategy{
 public double calculate(double price,int count){
  double total=price*count;
  if(count>=3) total-=price*0.5;
  return total;
 }
}