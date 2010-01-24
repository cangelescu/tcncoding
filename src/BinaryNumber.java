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

public class BinaryNumber {

    private final int digits = 16;
    private long number;
    private boolean[] binary = new boolean[digits];
    int alignment;

    //creates binary number from string sequence
    public BinaryNumber(String sequence)
    {
	this.alignment = sequence.length();
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

    //creates binary number from aligned string sequence
    public BinaryNumber(String sequence, int align)
    {
	this.alignment = align;
	//gets meaningful part of string
	String bin = "";
	for (int i = 0; i < digits - align; i++)
	    bin += "0";
	for (int i = sequence.length() - align; i < align; i++)
	    bin += sequence.charAt(i);

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
    public BinaryNumber(long number)
    {
	this.number = number;

	//gets meaningful part of string
	String bin = Integer.toBinaryString((int) number);
	for (int i = 0; i < digits - bin.length(); i++)
	{
	    this.binary[i] = false;
	}
	this.alignment = bin.length();

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

    //creates aligned binary number from integer (decimal) value
    public BinaryNumber(long number, int align)
    {
	this.number = number;
	this.alignment = align;

	//gets meaningful part of string
	String got = Integer.toBinaryString((int) number);
	String bin = "";
	for (int i = 0; i < alignment - got.length(); i++)
	    bin += "0";
	bin += got;

	for (int i = 0; i < digits - align; i++)
	{
	    this.binary[i] = false;
	}

	//creates array structure
	for (int i = digits - align; i < digits; i++)
	{
	    switch (bin.charAt(i - digits + align))
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
    public BinaryNumber(boolean[] number)
    {
	//gets meaningful part of string
	for (int i = 0; i < digits - number.length; i++)
	{
	    this.binary[i] = false;
	}
	this.alignment = number.length;

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

    //creates binary number from aligned boolean array
    public BinaryNumber(boolean[] number, int align)
    {
	this.alignment = align;
	//gets meaningful part of string
	for (int i = 0; i < digits - align; i++)
	{
	    this.binary[i] = false;
	}

	//creates array structure
	for (int i = digits - align; i < digits; i++)
	    this.binary[i] = number[i - digits + align];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		value += Math.pow(2, digits - 1 - i);
	this.number = value;
    }

    //returns aligned string representation of binary number
    public String getStringSequence()
    {
	String out = "";
	for (int i = digits - this.alignment; i < digits; i++)
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
    public boolean[] getFullBinaryArray()
    {
	return this.binary;
    }

    //returns self-aligned binary array
    public boolean[] getAlignedBinaryArray()
    {
	int k = digits - this.alignment;
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
    public BinaryNumber sum2(BinaryNumber number)
    {
	boolean[] in2 = number.getFullBinaryArray();
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits; i++)
	    out[i] = in2[i] ^ this.binary[i];
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, Math.max(number.alignment, this.alignment));
	return res;
    }

    //inverses number
    public BinaryNumber not2()
    {
	boolean[] out = new boolean[digits];
	for (int i = digits - this.alignment; i < digits; i++)
	    out[i] = !this.binary[i];
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, this.alignment);
	return res;
    }

    //shifts number to left direction for one position
    public BinaryNumber shl2()
    {
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits - 1; i++)
	{
	    out[i] = this.binary[i + 1];
	}
	out[digits - 1] = false;
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, this.alignment + 1);
	return res;
    }

    //shifts number to left direction for several positions
    public BinaryNumber shl2(int align)
    {
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits - align; i++)
	    out[i] = this.binary[i + align];
	for (int i = digits - align; i < digits; i++)
	    out[i] = false;
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, this.alignment + align);
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
