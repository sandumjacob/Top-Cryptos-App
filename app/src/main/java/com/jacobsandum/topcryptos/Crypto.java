package com.jacobsandum.topcryptos;

import java.util.Comparator;

/**
 * Created by Jacob S on 2/5/2018.
 */

public class Crypto {

    public String name;
    public String symbol;
    public double priceUSD;
    public double marketCapUSD;
    public double percentChange1HR;
    public double percentChange24HR;
    public double percentChange7D;

    public Crypto(String name, String symbol, double priceUSD, double marketCapUSD,
                  double percentChange1HR, double percentChange24HR, double percentChange7D) {
        this.name = name;
        this.symbol = symbol;
        this.priceUSD = priceUSD;
        this.marketCapUSD = marketCapUSD;
        this.percentChange1HR = percentChange1HR;
        this.percentChange24HR = percentChange24HR;
        this.percentChange7D = percentChange7D;
    }

    public Crypto() {
        this.name = "";
        this.symbol = "";
        this.priceUSD = 0.0;
        this.marketCapUSD = 0.0;
        this.percentChange1HR = Double.NEGATIVE_INFINITY;
        this.percentChange24HR = Double.NEGATIVE_INFINITY;
        this.percentChange7D = Double.NEGATIVE_INFINITY;
    }

    @Override
    public String toString() {
        String output = "Name: " + this.name + "\n" +
                " Symbol: " + this.symbol + "\n" +
                " Price USD: " + this.priceUSD + "\n" +
                " Market Cap USD: " + this.marketCapUSD + "\n" +
                " 1 Hour Percent Change: " + this.percentChange1HR + "\n" +
                " 24 Hour Percent Change: " + this.percentChange24HR + "\n" +
                " 7 Day Percent Change: " + this.percentChange7D + "\n";


        return output;

    }

}
