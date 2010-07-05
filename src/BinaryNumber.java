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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author post-factum
 */
public class BinaryNumber
{

    private long number = 0;
    private List<Boolean> binary = new ArrayList<Boolean>();

    /**
     * Creates binary number from string sequence
     * @param _sequence string sequence of 0 and 1
     */
    public BinaryNumber(String _sequence)
    {
	int len = _sequence.length();

	//creates list
	for (int i = 0; i < len; i++)
	    switch (_sequence.charAt(i))
	    {
		case '0':
		    binary.add(false);
		    break;
		case '1':
		    binary.add(true);
		    break;
		default:
		    binary.add(true);
		    break;
	    }

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < len; i++)
	    if (binary.get(i))
		number += Math.pow(2, len - 1 - i);
    }

    /**
     * Creates binary number from aligned string sequence
     * @param _sequence string sequence of 0 and 1
     * @param _alignment count of digits of binary number
     */
    public BinaryNumber(String _sequence, int _alignment)
    {
	int len = _sequence.length();
	int lowBound, highBound;

	//creates list
	if (len < _alignment)
	{
	    for (int i = 0; i < _alignment - len; i++)
		binary.add(false);
	    lowBound = 0;
	    highBound = len;
	} else
	if (len > _alignment)
	{
	    lowBound = len - _alignment;
	    highBound = len;
	} else
	{
	    lowBound = 0;
	    highBound = len;
	}
	for (int i = lowBound; i < highBound; i++)
	    switch (_sequence.charAt(i))
	    {
	        case '0':
		    binary.add(false);
		    break;
	        case '1':
		    binary.add(true);
		    break;
		default:
		    binary.add(true);
		    break;
	    }

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < _alignment; i++)
	    if (binary.get(i))
		number += Math.pow(2, len - 1 - i);
    }

    /**
     * Creates binary number from decimal value
     * @param _number integer number, that is to to binary number
     */
    public BinaryNumber(long _number)
    {
	number = _number;
	String bin = Integer.toBinaryString((int) _number);

	//creates list
	for (int i = 0; i < bin.length(); i++)
	    switch (bin.charAt(i))
	    {
		case '0':
		    binary.add(false);
		    break;
		case '1':
		    binary.add(true);
		    break;
		default: break;
	    }
    }

    /**
     * Creates aligned binary number from decimal value
     * @param _number integer number, that will be converted to binary
     * @param _alignment fixed width of resulting binary number in digits
     */
    public BinaryNumber(long _number, int _alignment)
    {
	String sequence = Integer.toBinaryString((int)_number);
	int len = sequence.length();

	//creates list
	if (len < _alignment)
	{
	    for (int i = 0; i < _alignment - len; i++)
		binary.add(false);
	    for (int i = 0; i < len; i++)
		switch (sequence.charAt(i))
		{
		    case '0':
			binary.add(false);
			break;
		    case '1':
			binary.add(true);
			break;
		    default:
			binary.add(true);
			break;
		}
	} else
	if (len > _alignment)
	{
	    for (int i = len - _alignment; i < len; i++)
		switch (sequence.charAt(i))
		{
		    case '0':
			binary.add(false);
			break;
		    case '1':
			binary.add(true);
			break;
		    default:
			binary.add(true);
			break;
		}
	} else
	    for (int i = 0; i < len; i++)
		switch (sequence.charAt(i))
		{
		    case '0':
			binary.add(false);
			break;
		    case '1':
			binary.add(true);
			break;
		    default:
			binary.add(true);
			break;
		}

	number = _number;
    }

    /**
     * Creates binary number from boolean array
     * @param _sequence boolean array, that represents binary number
     */
    public BinaryNumber(boolean[] _sequence)
    {
	//creates list
	for (int i = 0; i < _sequence.length; i++)
	    binary.add(_sequence[i]);

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < _sequence.length; i++)
	    if (binary.get(i))
		number += Math.pow(2, _sequence.length - 1 - i);
    }

    /**
     * Creates binary number from list of boolean values
     * @param _sequence list of boolean values
     */
    public BinaryNumber(List<Boolean> _sequence)
    {
	//creates list
	binary = _sequence;

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < _sequence.size(); i++)
	    if (binary.get(i))
		number += Math.pow(2, _sequence.size() - 1 - i);
    }

    /**
     * Creates binary number from aligned boolean array
     * @param _sequence boolean array, that represents binary number
     * @param _align count of meaningful digits in source array
     */
    public BinaryNumber(boolean[] _sequence, int _align)
    {
	int len = _sequence.length;

	//creates list
	if (len < _align)
	{
	    for (int i = 0; i < _align - len; i++)
		binary.add(false);
	    for (int i = 0; i < len; i++)
		binary.add(_sequence[i]);
	} else
	if (len > _align)
	{
	    for (int i = len - _align; i < len; i++)
		binary.add(_sequence[i]);
	} else
	    for (int i = 0; i < len; i++)
		binary.add(_sequence[i]);

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < _align; i++)
	    if (binary.get(i))
		number += Math.pow(2, _align - 1 - i);
    }

    /**
     * Returns string representation of binary number
     * @return
     */
    public String getStringSequence()
    {
	String out = "";
	for (Boolean cb: binary)
	    if (cb)
		out += '1';
	    else
		out += '0';
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
    public boolean[] getBinaryArray()
    {
	boolean[] out = new boolean[binary.size()];
	for (int i = 0; i < out.length; i++)
	    out[i] = binary.get(i);
	return out;
    }

    /**
     * Returns boolean digit of given index
     * @return
     */
    private boolean getDigit(int index)
    {
	return binary.get(index);
    }

    /**
     *
     * @param _number binary number to sum with
     * @return
     */
    public BinaryNumber sum2(BinaryNumber _number)
    {
	int len2 = _number.getLength();
	List<Boolean> out = new ArrayList<Boolean>();

	if (len2 < binary.size())
	{
	    int head = binary.size() - len2;
	    for (int i = 0; i < head; i++)
		out.add(binary.get(i));
	    for (int i = head; i < binary.size(); i++)
		out.add(_number.getDigit(i - head) ^ binary.get(i));
	} else
	if (len2 > binary.size())
	{
	    int head = len2 - binary.size();
	    for (int i = 0; i < head; i++)
		out.add(_number.getDigit(i));
	    for (int i = head; i < len2; i++)
		out.add(_number.getDigit(i) ^ binary.get(i - head));
	} else
	    for (int i = 0; i < len2; i++)
		out.add(_number.getDigit(i) ^ binary.get(i));

	BinaryNumber res = new BinaryNumber(out);
	return res;
    }

    /**
     * Inverses binary number
     * @return
     */
    public BinaryNumber not2()
    {
	List<Boolean> out = new ArrayList<Boolean>();
	for (Boolean cb: binary)
	    out.add(!cb);
	BinaryNumber res = new BinaryNumber(out);
	return res;
    }

    /**
     * Shifts binary number to left
     * @return
     */
    public BinaryNumber shl2()
    {
	List<Boolean> out = new ArrayList<Boolean>();
	for (Boolean cb: binary)
	    out.add(cb);
	out.add(false);
	BinaryNumber res = new BinaryNumber(out);
	return res;
    }

    /**
     * Shifts binary number to left by several positions
     * @param _count digits to shift number on
     * @return
     */
    public BinaryNumber shl2(int _count)
    {
	List<Boolean> out = new ArrayList<Boolean>();
	for (Boolean cb: binary)
	    out.add(cb);
	for (int i = 0; i < _count; i++)
	    out.add(false);
	BinaryNumber res = new BinaryNumber(out);
	return res;
    }

    /**
     * Returns weight (count of 1) of binary number
     * @return
     */
    public int getWeight()
    {
	int out = 0;
	for (Boolean cb: binary)
	    if (cb)
		out++;
	return out;
    }

    /**
     * Returns length of binary number
     * @return
     */
    public int getLength()
    {
	return binary.size();
    }
}
