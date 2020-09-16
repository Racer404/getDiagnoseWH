package fluenZa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;




public class getMsg {

	static String path;
	
	public static void main(String[] args)
	{
		System.out.println("请输入输出路径以.json为结尾");
		Scanner scanner=new Scanner(System.in);
		path=scanner.nextLine();
		ChromeOptions options=new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-gpu");
		WebDriver driver=new ChromeDriver(options);
		
		driver.get("https://m.medsci.cn/wh.asp");
		
//		System.setProperty("webdriver.chrome.driver","E:\\chromedriver.exe");
		
		int cacheInfected=0;
		int cacheSeemingly=0;
		int cacheDead=0;
		int cacheCured=0;
		
		
		while(true)
		{
		
		System.out.println("执行自动化控制");

		String output = "null";
		
		for(int i1=1;i1>0;i1++)
		{
					try {
						output=driver.findElement(By.className("count_list")).getText();
						break;
					}
					catch(Exception e)
					{
					e.printStackTrace();
							try {
								System.out.println("retrying");
								Thread.sleep(10000);
								driver.navigate().refresh();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					}
		}



		
		if(returnNum(output,1)!=cacheInfected)
		{		
		cacheInfected=returnNum(output,1);
		cacheSeemingly=returnNum(output,2);
		cacheDead=returnNum(output,3);
		cacheCured=returnNum(output,4);
		
		inputtoFile(cacheInfected,cacheSeemingly,cacheDead,cacheCured);
		System.out.println("infected:"+cacheInfected+"seemingly:"+cacheSeemingly+
				"dead:"+cacheDead+"cured:"+cacheCured);
		}

		driver.navigate().refresh();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static void inputtoFile(int infected,int seemingly,int dead,int cured)
	{

		String dateStr = "";  
        Date date = new Date();  
        //format的格式可以任意  
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss");  
        try {  
            dateStr = sdf2.format(date);  
            System.out.println(dateStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
		try {
	        File file = new File(path);
	        if(!file.exists()) {
	            file.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
	        }
	        FileWriter fw =new FileWriter(file);
	        FileOutputStream fos = new FileOutputStream(file,true);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);
	        BufferedWriter bw = new BufferedWriter(osw);
	        fw.write("");
	        fw.flush();
	        bw.write("{\"time\":\""+dateStr+"\","
	        		+ "\"infected\":"+infected+","
	        		+"\"seemingly\":"+seemingly+","
	        		+"\"dead\":"+dead+","
	        		+"\"cured\":"+cured+"}");
//	        bw.newLine();
	        bw.flush();
	        
	        fw.close();
	        bw.close();
	        osw.close();
	        fos.close();
	}catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	} catch (IOException e2) {
	    e2.printStackTrace();
	}
	}
	
	
	
	
	private static int returnNum(String input,int Order)
	{			
		
		String outputString="";
		
		switch(Order)
		{
		case 1:
		
				for(int i=0;i<input.length();i++)
				{
					
					char loopString=input.charAt(i);
					
					if(String.valueOf(loopString).equals("确")) {
						return Integer.valueOf(outputString);
					}
		
					if(String.valueOf(loopString).matches("[0-9]+"))
					{ 
		//				return loopString;
						outputString=outputString+String.valueOf(loopString);
					}
				}
				
				
		case 2:
			  int startIndex=input.indexOf("似")+1;
				char loopString;
				
				while(!String.valueOf(input.charAt(startIndex+2)).equals("接"))
				{
					startIndex++;
					loopString=input.charAt(startIndex);
					outputString=outputString+String.valueOf(loopString);
				}
				
				return Integer.valueOf(outputString);

		case 3:
			int startIndex3=input.indexOf("触")+1;
			char loopString3;
			
			while(!String.valueOf(input.charAt(startIndex3+2)).equals("死"))
			{
				startIndex3++;
				loopString=input.charAt(startIndex3);
				outputString=outputString+String.valueOf(loopString);
			}
			
			return Integer.valueOf(outputString);

		case 4:
			int startIndex4=input.indexOf("亡")+1;
			char loopString4;
			
			while(!String.valueOf(input.charAt(startIndex4+2)).equals("治"))
			{
				startIndex4++;
				loopString=input.charAt(startIndex4);
				outputString=outputString+String.valueOf(loopString);
			}
			
			return Integer.valueOf(outputString);
			
		}
		return Integer.valueOf(outputString);


	}
	

}
