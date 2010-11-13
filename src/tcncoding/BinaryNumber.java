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

package tcncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows working with binary numbers
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
     * @param _number integer number, that is converted to binary number
     */
    public BinaryNumber(long _number)
    {
	number = _number;
	String bin = Integer.toBinaryString((int) _number);

	//creates list
	for (int i = 0; i < bin.length(); i++)
	    binary.add(bin.charAt(i) != '0');
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
	    binary.add(sequence.charAt(i) != '0');

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
     * @param _alignment count of meaningful digits in source array
     */
    public BinaryNumber(boolean[] _sequence, int _alignment)
    {
	int len = _sequence.length;
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
	    binary.add(_sequence[i]);

	//calculates integer (decimal) value of binary number
	for (int i = 0; i < _alignment; i++)
	    if (binary.get(i))
		number += Math.pow(2, _alignment - 1 - i);
    }

    /**
     * Returns string representation of binary number
     * @return string of zeroes and ones that represents binary number
     */
    public String getStringSequence()
    {
	String out = "";
	for (Boolean cb: binary)
	    out += cb ? '1' : '0';
	return out;
    }

    /**
     * Returns decimal value of binary number
     * @return integer representation of binary number
     */
    public long toInt()
    {
	return number;
    }

    /**
     * Returns boolean array, that represents binary number
     * @return array representation of binary number
     */
    public boolean[] getBinaryArray()
    {
	boolean[] out = new boolean[binary.size()];
	for (int i = 0; i < out.length; i++)
	    out[i] = binary.get(i);
	return out;
    }

    /**
     * Truncates one digit from right side of number
     * @return truncated binary number
     */
    public BinaryNumber truncRight()
    {
	return new BinaryNumber(binary.subList(0, binary.size() - 1));
    }

    /**
     * Truncates several digits from right side of number
     * @param _count count of digits to remove
     * @return truncated binary number
     */
    public BinaryNumber truncRight(int _count)
    {
	return new BinaryNumber(binary.subList(0, binary.size() - _count));
    }

    /**
     * Truncates several digits from left side of number
     * @param _count count of digits to remove
     * @return truncated binary number
     */
    public BinaryNumber truncLeft(int _count)
    {
	return new BinaryNumber(binary.subList(_count, binary.size()));
    }

    /**
     * Returns boolean digit of given index
     * @param index number of digit to get
     * @return boolean representation of bit with given index
     */
    public boolean getDigit(int index)
    {
	return binary.get(index);
    }

    /**
     * Sums current binary number with given and returns new binary number
     * @param _number binary number to sum with
     * @return binary number, that is the sum of current binary number and given
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
     * @return binary number with inversed bits
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
     * @return shifted binary number
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
     * @return shifted binary number
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
     * @return integer value of binary number's weight
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
     * @return integer value of bits count
     */
    public int getLength()
    {
	return binary.size();
    }
}
