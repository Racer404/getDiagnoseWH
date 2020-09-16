package get4numbersWithHttpRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import com.google.gson.Gson;

import get4numbersWithHttpRequest.getDatas.localDatas.dataRecorder;
import get4numbersWithHttpRequest.getDatas.localDatas.rows;
import get4numbersWithHttpRequest.getDatas.nCovDatas.dataCollection;


public class getDatas {

	static String path;
	static int ConfrimedNum;
	static int CuredNum;
	static int DeadNum;
	static int Seemingly;
	static String dateStr = "";

	static String httpUrl = "http://api.tianapi.com/txapi/ncovcity/index";
	
	static String finalOutJson;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("���������·����.jsonΪ��β");
		Scanner scanner=new Scanner(System.in);
		path=scanner.nextLine();
		
		//���߳�		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				while(true)
				{
					String thisdateHH="";
					
					Date date = new Date();  
			        //format�ĸ�ʽ��������  
			        DateFormat sdfHH=new SimpleDateFormat("HH");
			        try {
			            thisdateHH = sdfHH.format(date);  
			            System.out.println(thisdateHH);  
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			        
			        if(thisdateHH.equals("23"))
			        {
			        	inputtoFile(getFinalJsonPerDay());
			        }
			        
			        try {
						Thread.sleep(3600000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        
				}
		        
		       
				
			}
		}).start();
		
		
		
		
		//���߳��¼�:
		while(true)
		{
			ConfrimedNum=0;
			CuredNum=0;
			DeadNum=0;
			Seemingly=399487;
		
			
			//��ȡ��ǰʱ��
			Date date = new Date();  
	        //format�ĸ�ʽ��������  
	        DateFormat sdf2 = new SimpleDateFormat("MM.dd");  
	        try {
	            dateStr = sdf2.format(date);  
	            System.out.println(dateStr);  
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
			//д���ļ�
	        
	        for(int i=5;i>1;i++)
	        {
				try {
					finalOutJson=getFinalJsonPerMinute();
					break;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
//					e1.printStackTrace();
		        	System.out.println("NETWORK ERROR");
		        	
		        	try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
	        
			inputtoFile(finalOutJson);

			try {
				//���߳�˯��120s
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}
	
	
	private static String getFinalJsonPerMinute() throws Exception

	{
		//HTTP����GET���Json
		String jsonResult = request(httpUrl,"f1b1ff0ecc9eb27d2a17bff48f32a168");
//		System.out.println(jsonResult);
		
		//�ȸ�Gson�ཫJson��װ��nCovDatas����
		nCovDatas ncDatas=new Gson().fromJson(jsonResult, nCovDatas.class);
		nCovDatas.dataCollection[] dCollections=ncDatas.getDataCollection();
		
		//ѭ����ӳ����
		for(int i=0;i<dCollections.length;i++)
		{
			ConfrimedNum=ConfrimedNum+dCollections[i].getConfrimedCount();
			CuredNum=CuredNum+dCollections[i].getCuredCount();
			DeadNum=DeadNum+dCollections[i].getDeadCount();
		}
		System.out.println(ConfrimedNum);
		System.out.println(Seemingly);
		System.out.println(CuredNum);
		System.out.println(DeadNum);
		
		//�����ص�JSON��װ����󲢸�д�����������
		localDatas lDatas=new Gson().fromJson(getDatafromFile(path), localDatas.class);
		lDatas.dataRecorder[lDatas.dataRecorder.length-1].setInfected(ConfrimedNum);
		lDatas.dataRecorder[lDatas.dataRecorder.length-1].setSeemingly(Seemingly);
		lDatas.dataRecorder[lDatas.dataRecorder.length-1].setCured(CuredNum);
		lDatas.dataRecorder[lDatas.dataRecorder.length-1].setDead(DeadNum);
		lDatas.dataRecorder[lDatas.dataRecorder.length-1].setDate(dateStr);
		
		lDatas.rows[lDatas.rows.length-1].c[0].setV(dateStr);
		lDatas.rows[lDatas.rows.length-1].c[1].setV(CuredNum+"");
		lDatas.rows[lDatas.rows.length-1].c[2].setV(DeadNum+"");
		
		lDatas.cols[1].setLabel("����");
		lDatas.cols[2].setLabel("����");
		
		return new Gson().toJson(lDatas);
	}

	private static String getFinalJsonPerDay()

	{
		localDatas lDatas=new Gson().fromJson(getDatafromFile(path), localDatas.class);
		rows[] cacheRows=new rows[lDatas.getRows().length+1];
		dataRecorder[] cacheRecorder=new dataRecorder[lDatas.getDataRecorder().length+1];
		for(int i=0;i<lDatas.getRows().length;i++)
		{
			cacheRows[i]=lDatas.getRows()[i];
			cacheRecorder[i]=lDatas.getDataRecorder()[i];
		}
		cacheRows[cacheRows.length-1]=lDatas.getRows()[lDatas.getRows().length-1];
		cacheRecorder[cacheRecorder.length-1]=lDatas.dataRecorder[lDatas.dataRecorder.length-1];
		
		lDatas.rows=cacheRows;
		lDatas.dataRecorder=cacheRecorder;
		return new Gson().toJson(lDatas);
	}
	
	private static void inputtoFile(String finalJson)
	
	{	
		
		try {
	        File file = new File(path);
	        if(!file.exists()) {
	            file.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
	        }
	        FileWriter fw =new FileWriter(file);
	        FileOutputStream fos = new FileOutputStream(file,true);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);
	        BufferedWriter bw = new BufferedWriter(osw);
	        fw.write("");
	        fw.flush();
	        bw.write(finalJson);
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
	
	private static String getDatafromFile(String path) {

		String Path=path;
	        BufferedReader reader = null;
	      String laststr = "";
	        try {
	          FileInputStream fileInputStream = new FileInputStream(Path);
	           InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
	            reader = new BufferedReader(inputStreamReader);
	           String tempString = null;
	         while ((tempString = reader.readLine()) != null) {
	              laststr += tempString;
	          }
	           reader.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	        } finally {
	           if (reader != null) {
	               try {
	                 reader.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	                }
	           }
	       }
	       return laststr;
	   }

	
	
	/**
	 * @param urlAll
	 *            :����ӿ�
	 * @param httpArg
	 *            :����
	 * @return ���ؽ��
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?key=" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	
	
	
	
	
	
	
	
	class nCovDatas
	{
		String code;
		String msg;
		dataCollection[] newslist;
		
		public String getCode()
		{
			return code;
		}
		
		public String getMsg()
		{
			return msg;
		}
		
		public dataCollection[] getDataCollection()
		{
			return newslist;
		}
		
				class dataCollection
				{
					int confirmedCount;
					int curedCount;
					int deadCount;
					
					public int getConfrimedCount()
					{
						return confirmedCount;
					}
					
					public int getCuredCount()
					{
						return curedCount;
					}
					
					public int getDeadCount()
					{
						return deadCount;
					}
		}

}
	
	
	
	
	
	class localDatas{
		
		dataRecorder[] dataRecorder;
		rows[] rows;
		cols[] cols;
		public class dataRecorder{
			int infected;
			int seemingly;
			int dead;
			int cured;
			String date;
			
			public int getInfected()
			{
				return infected;
			}
			
			public void setInfected(int infected)
			{
				this.infected=infected;
			}
			
			public int getSeemingly()
			{
				return seemingly;
			}
			
			public void setCured(int cured)
			{
				this.cured=cured;
			}
			
			public int getCured()
			{
				return cured;
			}
			
			public void setDead(int dead)
			{
				this.dead=dead;
			}
			
			public int getDead()
			{
				return dead;
			}
			
			public void setSeemingly(int seemingly)
			{
				this.seemingly=seemingly;
			}
			
			public String getDate()
			{
				return date;
			}
			
			public void setDate(String date)
			{
				this.date=date;
			}
			
		}
		public class rows{

			c[] c;
			public class c{
				String v;
				String f;
				
				public String getV()
				{
					return v;
				}
				
				public void setV(String v)
				{
					this.v=v;
				}
				public String getF()
				{
					return f;
				}
				public void setF(String f)
				{
					this.f=f;
				}
			}
		}
		public class cols{
			String id;
			String label;
			String pattern;
			String type;
			
			public String getId()
			{
				return id;
			}
			public void setId(String id)
			{
				this.id=id;
			}
			public String getLabel()
			{
				return label;
			}
			public void setLabel(String label)
			{
				this.label=label;
			}
			public String getPattern()
			{
				return pattern;
			}
			public void setPattern(String pattern)
			{
				this.pattern=pattern;
			}
			public String getType()
			{
				return type;
			}
			public void setType(String type)
			{
				this.type=type;
			}
		}
	
		
	
	public dataRecorder[] getDataRecorder()
	{
		return dataRecorder;
	}
	
	public rows[] getRows()
	{
		return rows;
	}
	
	public cols[] getCols()
	{
		return cols;
	}
	
	}




}
