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

/**
 *
 * @author Oleksandr Natalenko aka post-factum
 */
public class ErrorDescriptor {
    private int block, bit;
    
    public ErrorDescriptor(int _block, int _bit)
    {
        block = _block;
        bit = _bit;
    }
    
    public int getBlock()
    {
        return block;
    }
    
    public int getBit()
    {
        return bit;
    }
}
