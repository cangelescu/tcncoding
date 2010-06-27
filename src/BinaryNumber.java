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
     * @param _sequence string sequence of 0 and 1
     */
    public BinaryNumber(String _sequence)
    {
	alignment = _sequence.length();
	//gets meaningful part of string
	String bin = "";
	for (int i = 0; i < digits - _sequence.length(); i++)
	    bin += "0";
	bin += _sequence;

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (bin.charAt(i) == '1')
		value += Math.pow(2, digits - 1 - i);
	number = value;

	//creates array structure
	for (int i = 0; i < digits; i++)
	{
	    switch (bin.charAt(i))
	    {
		case '0':
		    binary[i] = false;
		    break;

		case '1':
		    binary[i] = true;
		    break;

		default: break;
	    }
	}
    }

    /**
     * Creates binary number from aligned string sequence
     * @param _sequence string sequence of 0 and 1
     * @param _align count of meaningful digits in source string
     */
    public BinaryNumber(String _sequence, int _align)
    {
	alignment = _align;
	//gets meaningful part of string
	String bin = "";
	for (int i = 0; i < digits - _align; i++)
	    bin += "0";
	for (int i = _sequence.length() - _align; i < _align; i++)
	    bin += _sequence.charAt(i);

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (bin.charAt(i) == '1')
		value += Math.pow(2, digits - 1 - i);
	number = value;

	//creates array structure
	for (int i = 0; i < digits; i++)
	{
	    switch (bin.charAt(i))
	    {
		case '0':
		    binary[i] = false;
		    break;

		case '1':
		    binary[i] = true;
		    break;

		default: break;
	    }
	}
    }

    /**
     * Creates binary number from decimal value
     * @param _number integer number, that will be converted to binary
     */
    public BinaryNumber(long _number)
    {
	number = _number;

	//gets meaningful part of string
	String bin = Integer.toBinaryString((int) _number);
	for (int i = 0; i < digits - bin.length(); i++)
	{
	    binary[i] = false;
	}
	alignment = bin.length();

	//creates array structure
	for (int i = digits - bin.length(); i < digits; i++)
	{
	    switch (bin.charAt(i - digits + bin.length()))
	    {
		case '0':
		    binary[i] = false;
		    break;

		case '1':
		    binary[i] = true;
		    break;

		default: break;
	    }
	}
    }

    /**
     * Creates aligned binary number from decimal value
     * @param _number integer number, that will be converted to binary
     * @param _align fixed width of resulting binary number in digits
     */
    public BinaryNumber(long _number, int _align)
    {
	number = _number;
	alignment = _align;

	//gets meaningful part of string
	String got = Integer.toBinaryString((int) _number);
	String bin = "";
	for (int i = 0; i < alignment - got.length(); i++)
	    bin += "0";
	bin += got;

	for (int i = 0; i < digits - _align; i++)
	{
	    binary[i] = false;
	}

	//creates array structure
	for (int i = digits - _align; i < digits; i++)
	{
	    switch (bin.charAt(i - digits + _align))
	    {
		case '0':
		    binary[i] = false;
		    break;

		case '1':
		    binary[i] = true;
		    break;

		default: break;
	    }
	}
    }

    /**
     * Creates binary number from boolean array
     * @param _sequence boolean array, that represents binary number
     */
    public BinaryNumber(boolean[] _sequence)
    {
	//gets meaningful part of string
	for (int i = 0; i < digits - _sequence.length; i++)
	{
	    binary[i] = false;
	}
	alignment = _sequence.length;

	//creates array structure
	for (int i = digits - _sequence.length; i < digits; i++)
	    binary[i] = _sequence[i - digits + _sequence.length];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (binary[i])
		value += Math.pow(2, digits - 1 - i);
	number = value;
    }

    //
    /**
     * Creates binary number from aligned boolean array
     * @param _sequence boolean array, that represents binary number
     * @param _align count of meaningful digits in source array
     */
    public BinaryNumber(boolean[] _sequence, int _align)
    {
	alignment = _align;
	//gets meaningful part of string
	for (int i = 0; i < digits - _align; i++)
	{
	    binary[i] = false;
	}

	//creates array structure
	for (int i = digits - _align; i < digits; i++)
	    binary[i] = _sequence[i - digits + _align];

	//calculates integer (decimal) value of binary number
	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (binary[i])
		value += Math.pow(2, digits - 1 - i);
	number = value;
    }

    /**
     * Returns aligned string representation of binary number
     * @return
     */
    public String getStringSequence()
    {
	String out = "";
	for (int i = digits - alignment; i < digits; i++)
	{
	    if (binary[i])
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
	return number;
    }

    /**
     * Returns boolean array, that represents binary number
     * @return
     */
    public boolean[] getFullBinaryArray()
    {
	return binary;
    }

    /**
     * Returns meaningful part of binary number as boolean array
     * @return
     */
    public boolean[] getAlignedBinaryArray()
    {
	int k = digits - alignment;
	boolean[] out = new boolean[digits - k];
	for (int i = k; i < digits; i++)
	    out[i - k] = binary[i];
	return out;
    }

    //converts integer array to decimal value
    private long binaryToInt(boolean[] _binary)
    {
	long out = 0;
	for (int i = 0; i < digits; i++)
	    if (_binary[i])
		out += Math.pow(2, digits - 1 - i);
	return out;
    }

    /**
     *
     * @param _number binary number to sum with
     * @return
     */
    public BinaryNumber sum2(BinaryNumber _number)
    {
	boolean[] in2 = _number.getFullBinaryArray();
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits; i++)
	    out[i] = in2[i] ^ binary[i];
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, Math.max(_number.alignment, alignment));
	return res;
    }

    /**
     * Inverses binary number
     * @return
     */
    public BinaryNumber not2()
    {
	boolean[] out = new boolean[digits];
	for (int i = digits - alignment; i < digits; i++)
	    out[i] = !binary[i];
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, alignment);
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
	    out[i] = binary[i + 1];
	}
	out[digits - 1] = false;
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, alignment + 1);
	return res;
    }

    /**
     * Shifts binary number to left by several positions
     * @param _align number of positions to shift by
     * @return
     */
    public BinaryNumber shl2(int _align)
    {
	boolean[] out = new boolean[digits];
	for (int i = 0; i < digits - _align; i++)
	    out[i] = binary[i + _align];
	for (int i = digits - _align; i < digits; i++)
	    out[i] = false;
	long initValue = binaryToInt(out);
	BinaryNumber res = new BinaryNumber(initValue, alignment + _align);
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
	    if (binary[i])
		out++;
	return out;
    }

    /**
     * Returns alignment of binary number
     * @return
     */
    public int getAlignment()
    {
	return alignment;
    }
}
