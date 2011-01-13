/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

 This program is free software; you can redistribute it and/or modify
 it under the terms of the Universal Program License as published by
 Oleksandr Natalenko aka post-factum; see file COPYING for details.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

 You should have received a copy of the Universal Program
 License along with this program; if not, write to
 pfactum@gmail.com

*/

package tcncoding;

/**
 * Formats numbers into human-readable form
 * @author Oleksandr Natalenko aka post-factum
 */
public class DecimalFormatter {

    private int precision;

    /**
     * Creates decimal formatter
     * @param _precision number of digits after point
     */
    public DecimalFormatter(int _precision)
    {
	precision = _precision;
    }

    /**
     * Formats given number and returns string like a*10^b
     * @param _value number to format
     * @return string human-readable representation of number
     */
    public String formatValue(double _value)
    {
	//gets absolute value of given number with precision rounding trick
	double absoluteValue = Double.valueOf(String.format("%f", Math.abs(_value)).replace(',', '.'));
	//calculates rounded value of log10
	int power = absoluteValue == 0 ? 0 : (int)Math.floor(Math.log10(absoluteValue));
	//calculates number's mantissa
	double mantissa = absoluteValue == 0 ? 0 : absoluteValue / Math.pow(10, power);
	//formes resulting string including number sign
	String numberSignum = _value < 0 ? "-" : "";
	String numberMantissa = String.format("%1." + precision + "f", mantissa);
	String numberCharacteristic = power == 0 ? "" : "*10^" + power;
	return numberSignum + numberMantissa + numberCharacteristic;
    }
}
