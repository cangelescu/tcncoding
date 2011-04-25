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
public class ChannelCoderControllerTest {

    /**
     *
     */
    public ChannelCoderControllerTest() {
    }

    /**
     *
     */
    @Test
    public void testParityBit()
    {
        BinaryNumber source = new BinaryNumber("010011");
        List<BinaryNumber> numbers = new ArrayList<BinaryNumber>();
        numbers.add(source);
        ChannelCoderController coder = new ChannelCoderController(numbers, ChannelCoderController.ChannelCoderCode.PARITY_BIT, true);
        coder.doEncode();
        String result = coder.getSequence().get(0).getStringSequence();
        boolean ok = result.equals("0100111");
	assertEquals(true, ok);
    }

    /**
     *
     */
    @Test
    public void testInversed()
    {
        BinaryNumber source = new BinaryNumber("010011");
        List<BinaryNumber> numbers = new ArrayList<BinaryNumber>();
        numbers.add(source);
        ChannelCoderController coder = new ChannelCoderController(numbers, ChannelCoderController.ChannelCoderCode.INVERSED, true);
        coder.doEncode();
        String result = coder.getSequence().get(0).getStringSequence();
        boolean ok = result.equals("010011101100");
	assertEquals(true, ok);
    }

    /**
     *
     */
    @Test
    public void testManchester()
    {
        BinaryNumber source = new BinaryNumber("010011");
        List<BinaryNumber> numbers = new ArrayList<BinaryNumber>();
        numbers.add(source);
        ChannelCoderController coder = new ChannelCoderController(numbers, ChannelCoderController.ChannelCoderCode.MANCHESTER, true);
        coder.doEncode();
        String result = coder.getSequence().get(0).getStringSequence();
        boolean ok = result.equals("011001011010");
	assertEquals(true, ok);
    }

    /**
     *
     */
    @Test
    public void testHamming()
    {
        BinaryNumber source = new BinaryNumber("0100");
        List<BinaryNumber> numbers = new ArrayList<BinaryNumber>();
        numbers.add(source);
        ChannelCoderController coder = new ChannelCoderController(numbers, ChannelCoderController.ChannelCoderCode.HAMMING, true);
        coder.doEncode();
        String result = coder.getSequence().get(0).getStringSequence();
        boolean ok = result.equals("0100101");
	assertEquals(true, ok);
    }

    /**
     *
     */
    @Test
    public void testCyclic()
    {
        BinaryNumber source = new BinaryNumber("10011");
        List<BinaryNumber> numbers = new ArrayList<BinaryNumber>();
        numbers.add(source);
        ChannelCoderController coder = new ChannelCoderController(numbers, ChannelCoderController.ChannelCoderCode.CYCLIC, true);
        coder.doEncode();
        String result = coder.getSequence().get(0).getStringSequence();
        boolean ok = result.equals("10011100");
	assertEquals(true, ok);
    }
}