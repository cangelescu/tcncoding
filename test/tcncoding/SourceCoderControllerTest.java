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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class SourceCoderControllerTest {

    public SourceCoderControllerTest() {
    }

    @Test
    public void testITC2()
    {
        String message = "привет";
        SourceCoderController coder = new SourceCoderController(SourceCoderController.SourceCoderCode.MTK2, message);
        coder.doEncode();
        String result = coder.getStringSequence();
        boolean ok = result.equals("011010101001100110011000000001");
	assertEquals(true, ok);
    }

    @Test
    public void testITC5()
    {
        String message = "привет";
        SourceCoderController coder = new SourceCoderController(SourceCoderController.SourceCoderCode.MTK5, message);
        coder.doEncode();
        String result = coder.getStringSequence();
        boolean ok = result.equals("101000010100101011001101011110001011010100");
	assertEquals(true, ok);
    }

    @Test
    public void testKOI8U()
    {
        String message = "привет";
        SourceCoderController coder = new SourceCoderController(SourceCoderController.SourceCoderCode.KOI8U, message);
        coder.doEncode();
        String result = coder.getStringSequence();
        boolean ok = result.equals("110100001101001011011001110101111100010111010100");
	assertEquals(true, ok);
    }

    @Test
    public void testMorse()
    {
        String message = "привет";
        SourceCoderController coder = new SourceCoderController(SourceCoderController.SourceCoderCode.MORSE, message);
        coder.doEncode();
        String result = coder.getStringSequence();
        boolean ok = result.equals("01100100001101");
	assertEquals(true, ok);
    }

    @Test
    public void testShannonFano()
    {
        String message = "привет";
        SourceCoderController coder = new SourceCoderController(SourceCoderController.SourceCoderCode.SHANNON, message);
        coder.doEncode();
        String result = coder.getStringSequence();
        boolean ok = result.equals("110011100110110101001000111");
	assertEquals(true, ok);
    }
}