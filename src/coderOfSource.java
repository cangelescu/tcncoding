
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author post-factum
 */
public class coderOfSource {

    public enum sourceCoderCode {mtk2, mtk5, morze};

    private sourceCoderCode using_code = null;
    private Map code_map = new HashMap();
    private String source_message = null;
    public int alignment = 0;
    private Vector source_sequence = new Vector();

    public coderOfSource(sourceCoderCode code_type, String message)
    {
	this.source_message = message;
	FileReader fr = null;
	switch (code_type)
	{
	    case mtk2:
		this.using_code = sourceCoderCode.mtk2;
		this.alignment = 5;
		try
		{
		    fr = new FileReader("/home/post-factum/work/devel/java/tcncoding/tables/mtk2");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case mtk5:
		this.using_code = sourceCoderCode.mtk5;
		this.alignment = 7;
		try
		{
		    fr = new FileReader("/home/post-factum/work/devel/java/tcncoding/tables/mtk5");
		} catch (Exception ex)
		{
		    System.err.println(ex.getMessage());
		}
		break;
	    case morze:
		break;
	    default:
		break;
	}
	this.code_map.clear();
	String line = "";
	try
	{
	    BufferedReader bfr = new BufferedReader(fr);
	    while((line = bfr.readLine()) != null)
	    {
		String[] parts = line.split("#");
		binaryNumber bnum = new binaryNumber(parts[0]);
		this.code_map.put(parts[1], bnum);
	    }
	} catch (Exception ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    public void doEncode()
    {
	this.source_sequence.clear();
	String working_message = "";
	switch (this.using_code)
	{
	    case mtk2:
		working_message = this.source_message.toUpperCase();
		break;
	    case mtk5:
		working_message = this.source_message;
		break;
	    case morze:
		break;
	    default:
		break;
	}
	int len = working_message.length();
	for (int i = 0; i < len; i++)
	{
	    char current_char = working_message.charAt(i);
	    binaryNumber num = (binaryNumber)this.code_map.get(String.valueOf(current_char));
	    if (num != null)
		this.source_sequence.add(num);
	}
    }

    public Vector getSequence()
    {
	return this.source_sequence;
    }

    public String getStringSequence()
    {
	String out = "";
	for (Object bn: this.source_sequence)
	{
	    binaryNumber number = (binaryNumber)bn;
	    out += number.getString(this.alignment) + " ";
	}
	return out;
    }
}
