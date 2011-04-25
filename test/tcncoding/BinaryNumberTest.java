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
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class BinaryNumberTest {

    /**
     *
     */
    public BinaryNumberTest() {
    }

    /**
     * Test of BinaryNumber class constructor.
     */
    @Test
    public void test000Constructor()
    {
	BinaryNumber instance = new BinaryNumber("101101");
	long expResult = 45L;
	long result = instance.toInt();
	assertEquals(expResult, result);
    }

    /**
     * Test of BinaryNumber class constructor.
     */
    @Test
    public void test001Constructor()
    {
	List<Boolean> bits = new ArrayList<Boolean>();
	bits.add(true);
	bits.add(false);
	bits.add(true);
	bits.add(true);
	bits.add(false);
	bits.add(true);
	BinaryNumber instance = new BinaryNumber(bits);
	long expResult = 45L;
	long result = instance.toInt();
	assertEquals(expResult, result);
    }

    /**
     * Test of BinaryNumber class constructor.
     */
    @Test
    public void test002Constructor()
    {
	boolean[] bits = {true, false, true, true, false, true};
	BinaryNumber instance = new BinaryNumber(bits);
	long expResult = 45L;
	long result = instance.toInt();
	assertEquals(expResult, result);
    }

    /**
     * Test of BinaryNumber class constructor.
     */
    @Test
    public void test003Constructor()
    {
	BinaryNumber instance = new BinaryNumber(45);
	long expResult = 45L;
	long result = instance.toInt();
	assertEquals(expResult, result);
    }
    
    /**
     * Test of BinaryNumber class constructor.
     */
    @Test
    public void test004Constructor()
    {
	BinaryNumber instance = new BinaryNumber("010011101", 5);
	String expResult = "11101";
	String result = instance.getStringSequence();
	assertEquals(true, result.equals(expResult));
    }

    /**
     * Test of getStringSequence method, of class BinaryNumber.
     */
    @Test
    public void testGetStringSequence()
    {
	BinaryNumber instance = new BinaryNumber("10110");
	String expResult = "10110";
	String result = instance.getStringSequence();
	assertEquals(expResult, result);
    }

    /**
     * Test of toInt method, of class BinaryNumber.
     */
    @Test
    public void testToInt()
    {
	BinaryNumber instance = new BinaryNumber("1011001");
	long expResult = 89L;
	long result = instance.toInt();
	assertEquals(expResult, result);
    }

    /**
     * Test of getBinaryArray method, of class BinaryNumber.
     */
    @Test
    public void testGetBinaryArray()
    {
	BinaryNumber instance = new BinaryNumber("101101");
	boolean[] expResult = {true, false, true, true, false, true};
	boolean[] result = instance.getBinaryArray();
	boolean ok = true;
	for (int i = 0; i < result.length; i++)
	    if (expResult[i] != result[i])
	    {
		ok = false;
		break;
	    }
	assertEquals(true, ok);
    }

    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_0args_OK()
    {
	BinaryNumber instance = new BinaryNumber("101");
	BinaryNumber expResult = new BinaryNumber("10");
	BinaryNumber result = instance.truncRight();
	assertEquals(expResult.toInt(), result.toInt());
    }
    
    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_0args_FAIL()
    {
	BinaryNumber instance = new BinaryNumber("1");
	BinaryNumber expResult = new BinaryNumber("1");
	BinaryNumber result = instance.truncRight();
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_int_OK()
    {
	int _count = 2;
	BinaryNumber instance = new BinaryNumber("10111011");
	BinaryNumber expResult = new BinaryNumber("101110");
	BinaryNumber result = instance.truncRight(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_int_FAIL1()
    {
	int _count = 0;
	BinaryNumber instance = new BinaryNumber("10111011");
	BinaryNumber expResult = new BinaryNumber("10111011");
	BinaryNumber result = instance.truncRight(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }
    
    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_int_FAIL2()
    {
	int _count = 1;
	BinaryNumber instance = new BinaryNumber("1");
	BinaryNumber expResult = new BinaryNumber("1");
	BinaryNumber result = instance.truncRight(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }
    
    /**
     * Test of truncRight method, of class BinaryNumber.
     */
    @Test
    public void testTruncRight_int_FAIL3()
    {
	int _count = 5;
	BinaryNumber instance = new BinaryNumber("10111");
	BinaryNumber expResult = new BinaryNumber("10111");
	BinaryNumber result = instance.truncRight(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }
    
    /**
     * Test of truncLeft method, of class BinaryNumber.
     */
    @Test
    public void testTruncLeft()
    {
	int _count = 2;
	BinaryNumber instance = new BinaryNumber("10111011");
	BinaryNumber expResult = new BinaryNumber("111011");
	BinaryNumber result = instance.truncLeft(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of getDigit method, of class BinaryNumber.
     */
    @Test
    public void testGetDigit()
    {
	int index = 3;
	BinaryNumber instance = new BinaryNumber("10100111");
	boolean expResult = false;
	boolean result = instance.getDigit(index);
	assertEquals(expResult, result);
    }

    /**
     * Test of sum2 method, of class BinaryNumber.
     */
    @Test
    public void testSum2()
    {
	BinaryNumber _number = new BinaryNumber(10);
	BinaryNumber instance = new BinaryNumber(20);
	BinaryNumber expResult = new BinaryNumber(30);
	BinaryNumber result = instance.sum2(_number);
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of not2 method, of class BinaryNumber.
     */
    @Test
    public void testNot2()
    {
	BinaryNumber instance = new BinaryNumber("10110");
	BinaryNumber expResult = new BinaryNumber("01001");
	BinaryNumber result = instance.not2();
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of shl2 method, of class BinaryNumber.
     */
    @Test
    public void testShl2_0args()
    {
	BinaryNumber instance = new BinaryNumber("111001");
	BinaryNumber expResult = new BinaryNumber("1110010");
	BinaryNumber result = instance.shl2();
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     * Test of shl2 method, of class BinaryNumber.
     */
    @Test
    public void testShl2_int()
    {
	int _count = 3;
	BinaryNumber instance = new BinaryNumber("111001");
	BinaryNumber expResult = new BinaryNumber("111001000");
	BinaryNumber result = instance.shl2(_count);
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     *
     */
    @Test
    public void testLsum2()
    {
	BinaryNumber n1 = new BinaryNumber("1010110");
	BinaryNumber n2 = new BinaryNumber("101");
        BinaryNumber expResult = new BinaryNumber("110");
	BinaryNumber result = n1.lsum2(n2);
	assertEquals(expResult.toInt(), result.toInt());
    }

    /**
     *
     */
    @Test
    public void testDivmod2_1()
    {
        BinaryNumber instance = new BinaryNumber("1011011");
        BinaryNumber divider = new BinaryNumber("1101");
        BinaryNumber expResult = new BinaryNumber("111");
        assertEquals(expResult.toInt(), instance.divmod2(divider).toInt());
    }

    /**
     *
     */
    @Test
    public void testDivmod2_2()
    {
        BinaryNumber instance = new BinaryNumber("111011001110");
        BinaryNumber divider = new BinaryNumber("10111");
        BinaryNumber expResult = new BinaryNumber("1111");
        assertEquals(expResult.toInt(), instance.divmod2(divider).toInt());
    }

    /**
     * Test of getWeight method, of class BinaryNumber.
     */
    @Test
    public void testGetWeight()
    {
	BinaryNumber instance = new BinaryNumber("101101101");
	int expResult = 6;
	int result = instance.getWeight();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLength method, of class BinaryNumber.
     */
    @Test
    public void testGetLength()
    {
	BinaryNumber instance = new BinaryNumber("1011010101");
	int expResult = 10;
	int result = instance.getLength();
	assertEquals(expResult, result);
    }
    
    /**
     * 
     */
    @Test
    public void testLeaveRightOK()
    {
        BinaryNumber instance = new BinaryNumber("1011011101001");
        BinaryNumber result = instance.leaveRight(4);
        String expResult = "1001";
        assertEquals(true, expResult.equals(result.getStringSequence()));
    }

    /**
     * 
     */
    @Test
    public void testLeaveRightFAIL1()
    {
        BinaryNumber instance = new BinaryNumber("01");
        BinaryNumber result = instance.leaveRight(4);
        String expResult = "01";
        assertEquals(true, expResult.equals(result.getStringSequence()));
    }
    
    /**
     * 
     */
    @Test
    public void testLeaveRightFAIL2()
    {
        BinaryNumber instance = new BinaryNumber("01011101");
        BinaryNumber result = instance.leaveRight(0);
        String expResult = "01011101";
        assertEquals(true, expResult.equals(result.getStringSequence()));
    }
}