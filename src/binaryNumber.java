public class binaryNumber {

    private final int digits = 16;
    private long number;
    private long[] binary = new long[digits];

    public binaryNumber(String sequence)
    {
	String bin = "";
	for (int i = 0; i < digits - sequence.length(); i++)
	    bin += "0";
	bin += sequence;

	long value = 0;
	for (int i = 0; i < digits; i++)
	    if (bin.charAt(i) == '1')
		value += Math.pow(2, digits - 1 - i);
	this.number = value;

	for (int i = 0; i < digits; i++)
	{
	    switch (bin.charAt(i))
	    {
		case '0':
		    this.binary[i] = 0;
		    break;

		case '1':
		    this.binary[i] = 1;
		    break;

		default: break;
	    }
	}
    }

    public binaryNumber(long number)
    {
	this.number = number;
	String bin = Integer.toBinaryString((int) number);
	for (int i = 0; i < digits - bin.length(); i++)
	{
	    this.binary[i] = 0;
	}

	for (int i = digits - bin.length(); i < digits; i++)
	{
	    switch (bin.charAt(i - digits + bin.length()))
	    {
		case '0':
		    this.binary[i] = 0;
		    break;

		case '1':
		    this.binary[i] = 1;
		    break;

		default: break;
	    }
	}
    }

    public String getString(int align)
    {
	String out = "";
	int k = 0;
	if (align == 0)
	{
	    while (this.binary[k] == 0)
		k++;
	} else
	    k = digits - align;
	for (int i = k; i < digits; i++)
	    out += String.valueOf(this.binary[i]);
	return out;
    }

    public long toInt()
    {
	return this.number;
    }

    public long[] toIntArray()
    {
	return this.binary;
    }

    public long[] toAlignedIntArray(int align)
    {
	int k = 0;
	if (align == 0)
	{
	    while (this.binary[k] == 0)
		k++;
	} else
	    k = digits - align;
	long[] out = new long[digits - k];
	for (int i = k; i < this.digits; i++)
	    out[i - k] = this.binary[i];
	return out;
    }

    private long binaryToInt(long[] binary)
    {
	long out = 0;
	for (int i = 0; i < digits; i++)
	    if (binary[i] == 1)
		out += Math.pow(2, digits - 1 - i);
	return out;
    }

    public binaryNumber sum2(binaryNumber number)
    {
	long in2[] = number.toIntArray();
	long out[] = new long[digits];
	for (int i = 0; i < digits; i++)
	{
	    if (in2[i] == 0 && this.binary[i] == 0)
	    {
		out[i] = 0;
	    } else
	    if (in2[i] == 1 && this.binary[i] == 0)
	    {
		out[i] = 1;
	    } else
	    if (in2[i] == 0 && this.binary[i] == 1)
	    {
		out[i] = 1;
	    } else
	    if (in2[i] == 1 && this.binary[i] == 1)
	    {
		out[i] = 0;
	    }
	}
	
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    public binaryNumber not2()
    {
	long out[] = new long[digits];
	int k = 0;
	while (this.binary[k] == 0)
	    k++;
	for (int i = k; i < digits; i++)
	{
	    if (this.binary[i] == 0)
		out[i] = 1;
	    else
		out[i] = 0;
	}

	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    public binaryNumber shl2()
    {
	long out[] = new long[digits];
	for (int i = 0; i < digits - 1; i++)
	{
	    out[i] = this.binary[i + 1];
	}
	out[digits - 1] = 0;
	long initValue = binaryToInt(out);
	binaryNumber res = new binaryNumber(initValue);
	return res;
    }

    public int getWeight()
    {
	int out = 0;
	for (int i = 0; i < digits; i++)
	    if (this.binary[i] == 1)
		out++;
	return out;
    }

}
