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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class DecimalFormatterTest {

    public DecimalFormatterTest() {
    }

    /**
     * Test of formatValue method, of class DecimalFormatter.
     */
    @Test
    public void test000FormatValue()
    {
	double _value = 0.0;
	System.out.println("formatValue: " + _value);
	DecimalFormatter instance = new DecimalFormatter(2);
	String expResult = "0,00";
	String result = instance.formatValue(_value);
	assertEquals(expResult, result);
    }

    /**
     * Test of formatValue method, of class DecimalFormatter.
     */
    @Test
    public void test001FormatValue()
    {
	double _value = 0.000023;
	System.out.println("formatValue: " + _value);
	DecimalFormatter instance = new DecimalFormatter(2);
	String expResult = "2,30*10^-5";
	String result = instance.formatValue(_value);
	assertEquals(expResult, result);
    }

    /**
     * Test of formatValue method, of class DecimalFormatter.
     */
    @Test
    public void test002FormatValue()
    {
	double _value = -0.003;
	System.out.println("formatValue: " + _value);
	DecimalFormatter instance = new DecimalFormatter(2);
	String expResult = "-3,00*10^-3";
	String result = instance.formatValue(_value);
	assertEquals(expResult, result);
    }

    /**
     * Test of formatValue method, of class DecimalFormatter.
     */
    @Test
    public void test003FormatValue()
    {
	double _value = 1536.22;
	System.out.println("formatValue: " + _value);
	DecimalFormatter instance = new DecimalFormatter(2);
	String expResult = "1,54*10^3";
	String result = instance.formatValue(_value);
	assertEquals(expResult, result);
    }

    /**
     * Test of formatValue method, of class DecimalFormatter.
     */
    @Test
    public void test004FormatValue()
    {
	double _value = -667.345;
	System.out.println("formatValue: " + _value);
	DecimalFormatter instance = new DecimalFormatter(2);
	String expResult = "-6,67*10^2";
	String result = instance.formatValue(_value);
	assertEquals(expResult, result);
    }

}