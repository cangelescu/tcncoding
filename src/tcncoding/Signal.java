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
public class Signal {
    /**
     * List of signal types
     */
    public enum SignalType
    {

	/**
	 * Signal from modulator
	 */
	MODULATOR,
        /**
         * Signal from noise generator
         */
        NOISE,
	/**
	 * Signal from channel
	 */
	CHANNEL,
	/**
	 * Signal from multiplier
	 */
	MULTIPLIER,
	/**
	 * Tabulated signal
	 */
	TABULATED;
    };
}
