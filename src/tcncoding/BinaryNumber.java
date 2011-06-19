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

import java.util.List;

/**
 * Allows working with binary numbers
 * @author Oleksandr Natalenko aka post-factum
 */
public class BinaryNumber
{

    private int number = 0, alignment = 0;

    /**
     * Creates binary number from string sequence
     * @param _sequence string sequence of 0 and 1
     */
    public BinaryNumber(String _sequence)
    {
        number = Integer.parseInt(_sequence, 2);
        alignment = _sequence.length();
    }

    /**
     * Creates binary number from aligned string sequence
     * @param _sequence string sequence of 0 and 1
     * @param _alignment count of digits of binary number
     */
    public BinaryNumber(String _sequence, int _alignment)
    {
        number = Integer.parseInt(_sequence.substring(_sequence.length() - _alignment), 2);
        alignment = _alignment;
    }

    /**
     * Creates binary number from decimal value
     * @param _number integer number, that is converted to binary number
     */
    public BinaryNumber(int _number)
    {
        number = _number;
        alignment = Integer.toBinaryString(_number).length();
    }

    /**
     * Creates aligned binary number from decimal value
     * @param _number integer number, that will be converted to binary
     * @param _alignment fixed width of resulting binary number in digits
     */
    public BinaryNumber(int _number, int _alignment)
    {
	String sequence = Integer.toBinaryString(_number);
        if (sequence.length() >= _alignment)
            number = Integer.parseInt(sequence.substring(sequence.length() - _alignment), 2);
        else
        {
            String trailingZeroes = "";
            for (int i = 0; i < _alignment - sequence.length(); i++)
                trailingZeroes += "0";
            number = Integer.parseInt(trailingZeroes + sequence, 2);
        }   
        alignment = _alignment;
    }

    /**
     * Creates binary number from boolean array
     * @param _sequence boolean array, that represents binary number
     */
    public BinaryNumber(boolean[] _sequence)
    {
        int size = _sequence.length;
        for (int i = 0; i < size; i++)
	    if (_sequence[i])
		number += Math.pow(2, size - 1 - i);
        alignment = _sequence.length;
    }

    /**
     * Creates binary number from list of boolean values
     * @param _sequence list of boolean values
     */
    public BinaryNumber(List<Boolean> _sequence)
    {
	int size = _sequence.size();
        for (int i = 0; i < size; i++)
	    if (_sequence.get(i))
		number += Math.pow(2, size - 1 - i);
        alignment = _sequence.size();
    }

    /**
     * Creates binary number from aligned boolean array
     * @param _sequence boolean array, that represents binary number
     * @param _alignment count of meaningful digits in source array
     */
    public BinaryNumber(boolean[] _sequence, int _alignment) throws ArithmeticException
    {
        int preNumber = 0;
        int size = _sequence.length;
        for (int i = 0; i < size; i++)
	    if (_sequence[i])
		preNumber += Math.pow(2, size - 1 - i);
        String sequence = Integer.toBinaryString((int)preNumber);
        if (sequence.length() >= _alignment)
            number = Integer.parseInt(sequence.substring(sequence.length() - _alignment), 2);
        else
        {
            String trailingZeroes = "";
            for (int i = 0; i < _alignment - sequence.length(); i++)
                trailingZeroes += "0";
            number = Integer.parseInt(trailingZeroes + sequence, 2);
        }   
        alignment = _alignment;
    }
    
    /**
     * Returns string representation of binary number
     * @return string of zeroes and ones that represents binary number
     */
    public String getStringSequence()
    {
        String preFormatted = Integer.toBinaryString(number);
        int spacers = alignment - preFormatted.length();
        String formatted = "";
        if (spacers > 0)
        {
            for (int i = 0; i < spacers; i++)
                formatted += "0";
            formatted += preFormatted;
        } else
            formatted = preFormatted;
        return formatted;
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
        String sequence = Integer.toBinaryString(number);
        int trailingZeroes = 0;
        if (alignment > sequence.length())
            trailingZeroes = alignment - sequence.length();
        
	boolean[] out = new boolean[sequence.length() + trailingZeroes];
        for (int i = 0; i < trailingZeroes; i++)
            out[i] = false;
	for (int i = trailingZeroes; i < out.length; i++)
	    out[i] = sequence.charAt(i - trailingZeroes) == '1' ? true : false;
        
	return out;
    }

    /**
     * Truncates one digit from right side of number
     * @return truncated binary number
     */
    public BinaryNumber truncRight()
    {
        if (alignment < 2)
            return new BinaryNumber(number, alignment);
        else
            return new BinaryNumber(number >>> 1, alignment - 1);
    }

    /**
     * Truncates several digits from right side of number
     * @param _count count of digits to remove
     * @return truncated binary number
     */
    public BinaryNumber truncRight(int _count)
    {
        if (_count >= alignment || _count < 1 || alignment < 2)
            return new BinaryNumber(number, alignment);
        else
           return new BinaryNumber(number >>> _count, alignment - _count);
    }

    /**
     * Truncates several digits from left side of number
     * @param _count count of digits to remove
     * @return truncated binary number
     */
    public BinaryNumber truncLeft(int _count)
    {
        int shift = (int)log2(Integer.numberOfLeadingZeros(number)) + _count;
        return new BinaryNumber((number << shift) >>> shift, alignment - _count);
    }
    
    /**
     * Returns binary number with specified count of digits from the right side
     * @param _count count of digits to leave
     * @return binary number with specified count of digits
     */
    public BinaryNumber leaveRight(int _count)
    {
        if (_count < 1)
            return new BinaryNumber(number, alignment);
        else
        {
            int shift = 32 - _count;
            return new BinaryNumber((number << shift) >>> shift, _count);
        }
    }
    
    /**
     * Returns binary number leading zeroes
     * @return count of leading zeroes
     */
    public int getLeadingZeroes()
    {
        int highestIndex = (int)log2(Integer.highestOneBit(number)) + 1;
        int index = alignment - highestIndex;
        return (index > 0) ? index : 0;
    }

    /**
     * Returns boolean digit of given index
     * @param index number of digit to get
     * @return boolean representation of bit with given index
     */
    public boolean getDigit(int index)
    {
	return getStringSequence().charAt(index) == '1' ? true : false;
    }

    /**
     * Sums current binary number with given and returns new binary number
     * @param _number binary number to sum with
     * @return binary number, that is the sum of current binary number and given
     */
    public BinaryNumber sum2(BinaryNumber _number)
    {
	return new BinaryNumber(number ^ _number.number, Math.max(alignment, _number.alignment));
    }

    /**
     * Inverses binary number
     * @return binary number with inversed bits
     */
    public BinaryNumber not2()
    {
	return new BinaryNumber(~number, alignment);
    }

    /**
     * Shifts binary number to left
     * @return shifted binary number
     */
    public BinaryNumber shl2()
    {
	return new BinaryNumber(number << 1, alignment + 1);
    }

    /**
     * Shifts binary number to left by several positions
     * @param _count digits to shift number on
     * @return shifted binary number
     */
    public BinaryNumber shl2(int _count)
    {
	return new BinaryNumber(number << _count, alignment + _count);
    }

    private double log2(double _x)
    {
        return Math.log(_x) / Math.log(2);
    }
    
    /**
     * Sums two binary numbers from left
     * @param _number number to sum with
     * @return sum of two number
     */
    public BinaryNumber lsum2(BinaryNumber _number)
    {
        int index1 = Integer.highestOneBit(number);
        int index2 = Integer.highestOneBit(_number.number);
        int n1, n2, res;
        if (index1 > index2)
        {
            n1 = number;
            n2 = _number.number << (int)(log2(index1) - log2(index2));
        } else
        if (index2 > index1)
        {
            n1 = number << (int)(log2(index2) - log2(index1));
            n2 = _number.number;
        } else
        {
            n1 = number;
            n2 = _number.number;
        }
        res = n1 ^ n2;
        return new BinaryNumber(res);
    }
    
    /**
     * Divides binary number and returns reminder
     * @param _denominator binary denominator to divide by
     * @return binary reminder of division
     */
    public BinaryNumber divmod2(BinaryNumber _denominator)
    {
        BinaryNumber dividend = new BinaryNumber(number);
        do {
            dividend = dividend.lsum2(_denominator);
        }  while (dividend.getLength() >= _denominator.getLength() && dividend.toInt() != 0);
        return dividend;
    }

    /**
     * Returns weight (count of 1) of binary number
     * @return integer value of binary number's weight
     */
    public int getWeight()
    {
	return Integer.bitCount(number);
    }

    /**
     * Returns length of binary number
     * @return integer value of bits count
     */
    public int getLength()
    {
	return alignment;
    }
}
