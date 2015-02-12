package src.java.gui;

import java.io.File;
import java.util.Formatter;


public class Filewrite {
	public
	Formatter filer; 
    String written_file_name;

	public void filcreate() 
    {
	File	path= new File("List_Datas.txt");
	written_file_name="List_Datas.txt";
	System.out.println(path.exists());
	if(!path.exists())
	try{	
   	filer= new Formatter (path);
    System.out.println("yes");
    }
    catch(Exception e)  
    {
    	System.out.println("no");}
    } 
    
	public void adddata(String s)
    {
    	filer.format("%s\n", s );
    }
	public String close()
	{
	if(this.filer!=null)
	filer.close();
	return written_file_name;
	}

}
