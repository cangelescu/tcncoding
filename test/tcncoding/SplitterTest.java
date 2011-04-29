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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class SplitterTest {

    /**
     *
     */
    public SplitterTest() {
    }

    /**
     * Test of doSplitting method, of class Splitter.
     */
    @Test
    public void testDoSplitting()
    {
	BinaryNumber test1 = new BinaryNumber("11011010001101");
	BinaryNumber test2 = new BinaryNumber("01011");
	BinaryNumber test3 = new BinaryNumber("100111");
	String[] expResult = {"0001", "1011", "0100", "0110", "1010", "1110", "0111"};
	List<BinaryNumber> testList = new ArrayList<BinaryNumber>();
	testList.add(test1);
	testList.add(test2);
	testList.add(test3);
	Splitter instance = new Splitter(testList, 4);
	List<BinaryNumber> blocks = instance.getSplittingBlocks();
	boolean ok = true;
	for (int i = 0; i < blocks.size(); i++)
	    if (!blocks.get(i).getStringSequence().equals(expResult[i]))
	    {
		ok = false;
		break;
	    }
	assertEquals(true, ok);
    }

    /**
     * Test of doRecovering method, of class Splitter.
     */
    @Test
    public void testDoRecovering()
    {
	BinaryNumber test1 = new BinaryNumber("11011010001101");
	BinaryNumber test2 = new BinaryNumber("01011");
	BinaryNumber test3 = new BinaryNumber("100111");
	String[] expResult = {"1101101000", "1101010", "111", "00111"};
	List<BinaryNumber> testList = new ArrayList<BinaryNumber>();
	testList.add(test1);
	testList.add(test2);
	testList.add(test3);
	List<Integer> map = new ArrayList<Integer>();
	map.add(10);
	map.add(7);
	map.add(3);
	map.add(5);
	Splitter instance = new Splitter(testList, map);
	List<BinaryNumber> blocks = instance.getRecoveringBlocks();
	boolean ok = true;
	for (int i = 0; i < blocks.size(); i++)
	    if (!blocks.get(i).getStringSequence().equals(expResult[i]))
	    {
		ok = false;
		break;
	    }
	assertEquals(true, ok);
    }

    /**
     * Test of getLeadingZeroesCount method, of class Splitter.
     */
    @Test
    public void testGetLeadingZeroesCount()
    {
	BinaryNumber test1 = new BinaryNumber("11011010001101");
	BinaryNumber test2 = new BinaryNumber("01011");
	BinaryNumber test3 = new BinaryNumber("100111");
	List<BinaryNumber> testList = new ArrayList<BinaryNumber>();
	testList.add(test1);
	testList.add(test2);
	testList.add(test3);
	Splitter instance = new Splitter(testList, 4);
	instance.getSplittingBlocks();
	int expResult = 3;
	int result = instance.getLeadingZeroesCount();
	assertEquals(expResult, result);
    }

}