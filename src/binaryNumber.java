/*

 Copyright (C) 2009-2010 Oleksandr Natalenko aka post-factum

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

public class binaryNumber {

    private final int digits = 16;
    private long number;
    private long[] binary = new long[digits];

    //creates binary number from string sequence
    public binaryNumber(String sequence)
    {
	//gets meaningful part of string
	String bin = "";
	for (int i = 0; i < digits - sequence.length(); i++)
	    bin += "0";
	bin += sequence;

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (bin.charAt(i) == '1')
		value += Math.pow(2, digits - 1 - i);
	this.number = value;

	//creates array structure
	for (int i = 0; i < digits; i++)
	{
	    switch (bin.charAt(i))
	    {
		case '0':
		    this.binary[i] = 0;
		    break;

		case '1':
		    this.binary[i] = 1;
		    break;

		default: break;
	    }
	}
    }

    //creates binary number from integer (decimal) value
    public binaryNumber(long number)
    {
	this.number = number;

	//gets meaningful part of string
	String bin = Integer.toBinaryString((int) number);
	for (int i = 0; i < digits - bin.length(); i++)
	{
	    this.binary[i] = 0;
	}

	//creates array structure
	for (int i = digits - bin.length(); i < digits; i++)
	{
	    switch (bin.charAt(i - digits + bin.length()))
	    {
		case '0':
		    this.binary[i] = 0;
		    break;

		case '1':
		    this.binary[i] = 1;
		    break;

		default: break;
	    }
	}
    }

    //returns fixed-width string representation of binary number
    public String getString(int align)
    {
	String out = "";
	int k = 0;
	if (align == 0)
	{
	    while (this.binary[k] == 0)
		k++;
	} else
	    k = digits - align;
	for (int i = k; i < digits; i++)
	    out += String.valueOf(this.binary[i]);
	return out;
    }

    //returns decimal value of binary number
    public long toInt()
    {
	return this.number;
    }

    //returns array of zeroes and ones
    public long[] toIntArray()
    {
	return this.binary;
    }

    //returns fixed-width integer array
    public long[] toIntArray(int align)
    {
	int k = 0;
	if (align == 0)
	{
	    while (this.binary[k] == 0)
		k++;
	} else
	    k = digits - align;
	long[] out = new long[digits - k];
	for (int i = k; i < this.digits; i++)
	    out[i - k] = this.binary[i];
	return out;
    }


    //converts integer array to decimal value
    private long binaryToInt(long[] binary)
    {
	long out = 0;
	for (int i = 0; i < digits; i++)
	    if (binary[i] == 1)
		out += Math.pow(2, digits - 1 - i);
	return out;
    }

    //sums two numbers by module 2
    public binaryNumber sum2(binaryNumber number)
    {
	long in2[] = number.toIntArray();
	long out[] = new long[digits];
	for (int i = 0; i < digits; i++)
	{
	    if (in2[i] == 0 && this.binary[i] == 0)
	    {
		out[i] = 0;
	    } else
	    if (in2[i] == 1 && this.binary[i] == 0)
	    {
		out[i] = 1;
	    } else
	    if (in2[i] == 0 && this.binary[i] == 1)
	    {
		out[i] = 1;
	    } else
	    if (in2[i] == 1 && this.binary[i] == 1)
	    {
		out[i] = 0;
	    }
	}
	
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //inverses number
    public binaryNumber not2()
    {
	long out[] = new long[digits];
	int k = 0;
	while (this.binary[k] == 0)
	    k++;
	for (int i = k; i < digits; i++)
	{
	    if (this.binary[i] == 0)
		out[i] = 1;
	    else
		out[i] = 0;
	}

	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //inverses aligned part of number
    public binaryNumber not2(int align)
    {
	long out[] = new long[digits];
	for (int i = digits - align; i < digits; i++)
	{
	    if (this.binary[i] == 0)
		out[i] = 1;
	    else
		out[i] = 0;
	}

	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //shifts number to left direction for one position
    public binaryNumber shl2()
    {
	long out[] = new long[digits];
	for (int i = 0; i < digits - 1; i++)
	{
	    out[i] = this.binary[i + 1];
	}
	out[digits - 1] = 0;
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //shifts number to left direction for several positions
    public binaryNumber shl2(int align)
    {
	long out[] = new long[digits];
	for (int i = 0; i < digits - align; i++)
	{
	    out[i] = this.binary[i + align];
	}
	for (int i = digits - align; i < digits; i++)
	{
	    out[i] = 0;
	}
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //gets weight of binary number
    public int getWeight()
    {
	int out = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i] == 1)
		out++;
	return out;
    }

}
