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

import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class SourceDecoderControllerTest {

    public SourceDecoderControllerTest() {
    }

    @Test
    public void testITC2()
    {
        List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
        inputSequence.add(new BinaryNumber("01101"));
        inputSequence.add(new BinaryNumber("01010"));
        inputSequence.add(new BinaryNumber("01100"));
        inputSequence.add(new BinaryNumber("11001"));
        inputSequence.add(new BinaryNumber("10000"));
        inputSequence.add(new BinaryNumber("00001"));

        SourceDecoderController decoder = new SourceDecoderController(inputSequence, SourceCoderController.SourceCoderCode.MTK2, true);
        decoder.doDecode();
        String result = decoder.getMessage();
        boolean ok = result.equals("ПРИВЕТ");
	assertEquals(true, ok);
    }

    @Test
    public void testITC5()
    {
        List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
        inputSequence.add(new BinaryNumber("1010000"));
        inputSequence.add(new BinaryNumber("1010010"));
        inputSequence.add(new BinaryNumber("1011001"));
        inputSequence.add(new BinaryNumber("1010111"));
        inputSequence.add(new BinaryNumber("1000101"));
        inputSequence.add(new BinaryNumber("1010100"));

        SourceDecoderController decoder = new SourceDecoderController(inputSequence, SourceCoderController.SourceCoderCode.MTK5, true);
        decoder.doDecode();
        String result = decoder.getMessage();
        boolean ok = result.equals("привет");
	assertEquals(true, ok);
    }

    @Test
    public void testKOI8U()
    {
        List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
        inputSequence.add(new BinaryNumber("11010000"));
        inputSequence.add(new BinaryNumber("11010010"));
        inputSequence.add(new BinaryNumber("11011001"));
        inputSequence.add(new BinaryNumber("11010111"));
        inputSequence.add(new BinaryNumber("11000101"));
        inputSequence.add(new BinaryNumber("11010100"));

        SourceDecoderController decoder = new SourceDecoderController(inputSequence, SourceCoderController.SourceCoderCode.KOI8U, true);
        decoder.doDecode();
        String result = decoder.getMessage();
        boolean ok = result.equals("привет");
	assertEquals(true, ok);
    }

    @Test
    public void testMorse()
    {
        List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
        inputSequence.add(new BinaryNumber("0110"));
        inputSequence.add(new BinaryNumber("010"));
        inputSequence.add(new BinaryNumber("00"));
        inputSequence.add(new BinaryNumber("011"));
        inputSequence.add(new BinaryNumber("0"));
        inputSequence.add(new BinaryNumber("1"));

        SourceDecoderController decoder = new SourceDecoderController(inputSequence, SourceCoderController.SourceCoderCode.MORSE, true);
        decoder.doDecode();
        String result = decoder.getMessage();
        boolean ok = result.equals("ПРИВЕТ");
	assertEquals(true, ok);
    }

    @Test
    public void testShannon()
    {
        List<BinaryNumber> inputSequence = new ArrayList<BinaryNumber>();
        inputSequence.add(new BinaryNumber("110011"));
        inputSequence.add(new BinaryNumber("10011"));
        inputSequence.add(new BinaryNumber("0110"));
        inputSequence.add(new BinaryNumber("1010"));
        inputSequence.add(new BinaryNumber("0100"));
        inputSequence.add(new BinaryNumber("0111"));

        SourceDecoderController decoder = new SourceDecoderController(inputSequence, SourceCoderController.SourceCoderCode.SHANNON, true);
        decoder.doDecode();
        String result = decoder.getMessage();
        boolean ok = result.equals("ПРИВЕТ");
	assertEquals(true, ok);
    }
}