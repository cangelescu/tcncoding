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

/**
 *
 * @author post-factum
 */
public class BinaryNumber
{

    private final int digits = 16;
    private long number;
    private boolean[] binary = new boolean[digits];
    private int alignment;

    /**
     * Creates binary number from string sequence
     * @param sequence string sequence of 0 and 1
     */
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

    /**
     * Creates binary number from aligned string sequence
     * @param sequence string sequence of 0 and 1
     * @param align count of meaningful digits in source string
     */
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

    /**
     * Creates binary number from decimal value
     * @param number integer number, that will be converted to binary
     */
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

    /**
     * Creates aligned binary number from decimal value
     * @param number integer number, that will be converted to binary
     * @param align fixed width of resulting binary number in digits
     */
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

    /**
     * Creates binary number from boolean array
     * @param sequence boolean array, that represents binary number
     */
    public BinaryNumber(boolean[] sequence)
    {
	//gets meaningful part of string
	for (int i = 0; i < digits - sequence.length; i++)
	{
	    this.binary[i] = false;
	}
	this.alignment = sequence.length;

	//creates array structure
	for (int i = digits - sequence.length; i < digits; i++)
	    this.binary[i] = sequence[i - digits + sequence.length];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		value += Math.pow(2, digits - 1 - i);
	this.number = value;
    }

    //
    /**
     * Creates binary number from aligned boolean array
     * @param sequence boolean array, that represents binary number
     * @param align count of meaningful digits in source array
     */
    public BinaryNumber(boolean[] sequence, int align)
    {
	this.alignment = align;
	//gets meaningful part of string
	for (int i = 0; i < digits - align; i++)
	{
	    this.binary[i] = false;
	}

	//creates array structure
	for (int i = digits - align; i < digits; i++)
	    this.binary[i] = sequence[i - digits + align];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		value += Math.pow(2, digits - 1 - i);
	this.number = value;
    }

    /**
     * Returns aligned string representation of binary number
     * @return
     */
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

    /**
     * Returns decimal value of binary number
     * @return
     */
    public long toInt()
    {
	return this.number;
    }

    /**
     * Returns boolean array, that represents binary number
     * @return
     */
    public boolean[] getFullBinaryArray()
    {
	return this.binary;
    }

    /**
     * Returns meaningful part of binary number as boolean array
     * @return
     */
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

    /**
     *
     * @param number binary number to sum with
     * @return
     */
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

    /**
     * Inverses binary number
     * @return
     */
    public BinaryNumber not2()
    {
	boolean[] out = new boolean[digits];
	for (int i = digits - this.alignment; i < digits; i++)
	    out[i] = !this.binary[i];
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, this.alignment);
	return res;
    }

    /**
     * Shifts binary number to left
     * @return
     */
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

    /**
     * Shifts binary number to left by several positions
     * @param align number of positions to shift by
     * @return
     */
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

    /**
     * Returns weight (count of 1) of binary number
     * @return
     */
    public int getWeight()
    {
	int out = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i])
		out++;
	return out;
    }

    /**
     * Returns alignment of binary number
     * @return
     */
    public int getAlignment()
    {
	return this.alignment;
    }
}
