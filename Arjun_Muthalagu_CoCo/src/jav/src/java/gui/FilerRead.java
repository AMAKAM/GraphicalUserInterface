package src.java.gui;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.java.api.CoCo_batch;


public class FilerRead extends Filewrite 
{
	Scanner scan;
	FilerRead(String s)
	{
		fileopen_for_reading(s);	
	}
	
	FilerRead()
	{
		fileopen_for_reading();	
	}
	
	public void fileopen_for_reading()
	{
			
	try
	{
	scan= new Scanner((new File("TestFile.txt")));
	System.out.println("File opened sucessfully");
	System.out.println(scan);
	}catch(Exception e)
	{
	System.out.println("no");
	}
	}
	
	public void fileopen_for_reading(String s)
	{
			
	try
	{
	scan= new Scanner((new File(s)));
	System.out.println("File opened sucessfully");
	}catch(Exception e)
	{
	System.out.println("no file to open");
	}
	}
	
	
	public ArrayList<String> readdata()
	{	
		ArrayList<String> return_arraylistnew = new ArrayList<String>();
		Filewrite fw = new Filewrite();
	    Scanner read = null;
		fw.filcreate();
		if(fw.filer!=null)
		{	
		Set<String> prevent_duplicates = new HashSet<String>();
		int line=0;
		boolean first_time = true;
		int current_delimiter,past_delimiter,duplicate_count;
		int return_array_count_new=0;
		CoCo_batch COCO_OBJECT = new CoCo_batch();
		ArrayList<String> current_line = new ArrayList<String>();
		Pattern p =Pattern.compile("\\d+");;
		Matcher m;
		String no_duplicate[];
		COCO_OBJECT = new CoCo_batch();
	while ( (scan.hasNext()) )//&& (line<10) && (return_array_count<20))
	{
	current_line.add(line, scan.next());	
	current_delimiter = current_line.get(line).indexOf(",",0);
	past_delimiter = -1;
	System.out.println("the string at current_line " + line +" is "+current_line.get(line).toString());
	while(current_delimiter!=-1)
		{
			if(first_time)
				current_delimiter = current_line.get(line).indexOf(",",0); 
			else
				current_delimiter=current_line.get(line).indexOf(",", current_delimiter +1);
			if(first_time)
					{	
						m=p.matcher(current_line.get(line).substring(0, current_delimiter));
						if(!m.find())
						prevent_duplicates.add(current_line.get(line).substring(0, current_delimiter));	
						first_time = false;
						past_delimiter = current_delimiter;
					}
			
			else if(current_delimiter!= -1)
					{   
						m=p.matcher(current_line.get(line).substring(past_delimiter+1, current_delimiter));
						if(!m.find())
						prevent_duplicates.add(current_line.get(line).substring(past_delimiter+1, current_delimiter));	
					
						past_delimiter = current_delimiter;
					}
		}
					m=p.matcher(current_line.get(line).substring(past_delimiter +1));
					if(!m.find())
						prevent_duplicates.add(current_line.get(line).substring(past_delimiter +1));
		line++;
		first_time=true;
	}
	duplicate_count = prevent_duplicates.size();
	System.out.println(duplicate_count);
	no_duplicate = new String[duplicate_count];
	prevent_duplicates.toArray(no_duplicate);
	System.out.println(no_duplicate.length);	
		return_array_count_new=0;
		
    	 for(int e=0;e<no_duplicate.length;e++)
    	 {  
		
		if((!COCO_OBJECT.line(no_duplicate[e]).equalsIgnoreCase("other")))
		{
			return_arraylistnew.add(no_duplicate[e]);
			fw.adddata(return_arraylistnew.get(return_array_count_new).toString());
			return_array_count_new++;
		}
		
	
    }
    fw.close(); 
	Collections.sort(return_arraylistnew);
	}
	else
	{	scan.close();
		String file_name=fw.close();
		
		try
		{
		read = new Scanner(new File(file_name));
		}
		catch(Exception e)
		{
			read.close();	
		}
		int index=0;
		System.out.println(read.hasNext());
		while(read.hasNext())
		{
			String buffer=read.next();
			return_arraylistnew.add(index, buffer.toLowerCase());
			index++;
		}
	}
	System.out.println(read);	
	Collections.sort(return_arraylistnew);	
	return return_arraylistnew;
	}
}

	

