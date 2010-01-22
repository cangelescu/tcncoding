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
    private boolean[] binary = new boolean[digits];

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
		    this.binary[i] = false;
		    break;

		case '1':
		    this.binary[i] = true;
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
	    this.binary[i] = false;
	}

	//creates array structure
	for (int i = digits - bin.length(); i < digits; i++)
	{
	    switch (bin.charAt(i - digits + bin.length()))
	    {
		case '0':
		    this.binary[i] = false;
		    break;

		case '1':
		    this.binary[i] = true;
		    break;

		default: break;
	    }
	}
    }

    //creates binary number from boolean array
    public binaryNumber(boolean[] number)
    {
	//gets meaningful part of string
	for (int i = 0; i < digits - number.length; i++)
	{
	    this.binary[i] = false;
	}

	//creates array structure
	for (int i = digits - number.length; i < digits; i++)
	    this.binary[i] = number[i - digits + number.length];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		value += Math.pow(2, digits - 1 - i);
	this.number = value;
    }

    //returns fixed-width string representation of binary number
    public String getString(int align)
    {
	String out = "";
	int k = 0;
	if (align == 0)
	{
	    while (!this.binary[k])
		k++;
	} else
	    k = digits - align;
	for (int i = k; i < digits; i++)
	{
	    if (this.binary[i])
		out += '1';
	    else
		out += '0';
	}
	return out;
    }

    //returns decimal value of binary number
    public long toInt()
    {
	return this.number;
    }

    //returns array of zeroes and ones
    public boolean[] toBinaryArray()
    {
	return this.binary;
    }

    //returns fixed-width integer array
    public boolean[] toBinaryArray(int align)
    {
	int k = 0;
	if (align == 0)
	{
	    while (!this.binary[k])
		k++;
	} else
	    k = digits - align;
	boolean[] out = new boolean[digits - k];
	for (int i = k; i < this.digits; i++)
	    out[i - k] = this.binary[i];
	return out;
    }


    //converts integer array to decimal value
    private long binaryToInt(boolean[] binary)
    {
	long out = 0;
	for (int i = 0; i < digits; i++)
	    if (binary[i])
		out += Math.pow(2, digits - 1 - i);
	return out;
    }

    //sums two numbers by module 2
    public binaryNumber sum2(binaryNumber number)
    {
	boolean[] in2 = number.toBinaryArray();
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits; i++)
	    out[i] = in2[i] ^ this.binary[i];
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //inverses number
    public binaryNumber not2()
    {
	boolean[] out = new boolean[digits];
	int k = 0;
	while (!this.binary[k])
	    k++;
	for (int i = k; i < digits; i++)
	    out[i] = !this.binary[i];
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //inverses aligned part of number
    public binaryNumber not2(int align)
    {
	boolean[] out = new boolean[digits];
	for (int i = digits - align; i < digits; i++)
	    out[i] = !this.binary[i];

	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //shifts number to left direction for one position
    public binaryNumber shl2()
    {
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits - 1; i++)
	{
	    out[i] = this.binary[i + 1];
	}
	out[digits - 1] = false;
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //shifts number to left direction for several positions
    public binaryNumber shl2(int align)
    {
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits - align; i++)
	    out[i] = this.binary[i + align];
	for (int i = digits - align; i < digits; i++)
	    out[i] = false;
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    //gets weight of binary number
    public int getWeight()
    {
	int out = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		out++;
	return out;
    }

}
