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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author post-factum
 */
public class ErrorsInjectorTest {

    public ErrorsInjectorTest() {
    }

    /**
     * Test of injectErrors method, of class ErrorsInjector.
     */
    @Test
    public void test000InjectErrors()
    {
	List<BinaryNumber> symbols = new ArrayList<BinaryNumber>();
	symbols.add(new BinaryNumber(619));
	symbols.add(new BinaryNumber(357));
	symbols.add(new BinaryNumber(37));

	ErrorsInjector injector = new ErrorsInjector(symbols, 3, true);
	injector.injectErrors();
	List<BinaryNumber> brokenSymbols = new ArrayList<BinaryNumber>();
	brokenSymbols = injector.getSequence();

	List<BinaryNumber> errorsVectors = new ArrayList<BinaryNumber>();
	for (int i = 0; i < symbols.size(); i++)
	    errorsVectors.add(symbols.get(i).sum2(brokenSymbols.get(i)));
	
	int expResult = 9;
	int result = 0;
	for (BinaryNumber cbn: errorsVectors)
	    result += cbn.getWeight();

	assertEquals(expResult, result);
    }

    /**
     * Test of injectErrors method, of class ErrorsInjector.
     */
    @Test
    public void test001InjectErrors()
    {
	List<BinaryNumber> symbols = new ArrayList<BinaryNumber>();
	symbols.add(new BinaryNumber(619));
	symbols.add(new BinaryNumber(357));
	symbols.add(new BinaryNumber(37));

	ErrorsInjector injector = new ErrorsInjector(symbols, 3, false);
	injector.injectErrors();
	List<BinaryNumber> brokenSymbols = new ArrayList<BinaryNumber>();
	brokenSymbols = injector.getSequence();

	List<BinaryNumber> errorsVectors = new ArrayList<BinaryNumber>();
	for (int i = 0; i < symbols.size(); i++)
	    errorsVectors.add(symbols.get(i).sum2(brokenSymbols.get(i)));

	int expResult = 3;
	int result = 0;
	for (BinaryNumber cbn: errorsVectors)
	    result += cbn.getWeight();

	assertEquals(expResult, result);
    }

}