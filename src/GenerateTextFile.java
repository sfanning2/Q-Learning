import java.io.FileWriter;
import java.io.IOException;
 
public class GenerateTextFile
{
   public static void generateTextFile(String sFileName, String contents)
   {
	try
	{
	    FileWriter writer = new FileWriter(sFileName);
 
	    writer.append(contents);
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
    }
}