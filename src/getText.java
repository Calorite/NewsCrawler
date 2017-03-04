import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.OutputStreamWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cn.edu.hfut.dmic.htmlbot.contentextractor.ContentExtractor;

public class getText {
	
	 private static String noiseDelete(String text){
		  Pattern pattern = Pattern.compile("Yahoo!ファイナンスのすべての機能を利用するためには");
		  Matcher matcher = pattern.matcher(text);
		  if (matcher.find())		  
		  return null;
		return text;
	 }
	 
	 private static String newscut(String text,String str){
		  Pattern pattern = Pattern.compile(str);
		  Matcher matcher = pattern.matcher(text);
		  if (matcher.find()){
			return matcher.group(0);  
		  }		  
		  
		return text;
	 }
	 
	 
	    private static int getCodeSum(String news) {
	    	int sum=0;
	    	Pattern pattern = Pattern.compile("<[0-9]{1,4}>");
		    Matcher matcher = pattern.matcher(news);
		    while (matcher.find()) {
				sum++;
			}
		    return sum;
		}
	 
	 
	 
	 private static List<Company> getCompanylist() throws SQLException {
		List<Company> list=new ArrayList<>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		String sql="SELECT * FROM stockdb.nikei225;";
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				Company com=new Company();
				com.id=rs.getInt("id");
				com.name=rs.getString("name").replace("（株）", "");
				list.add(com);
			}
		}
		helper.deconnSQL();
		return list;
	}
	 
	 
	 private List<Integer> targetNewsCode(List<Company> list,String text) throws SQLException {		
		DBhelper helper=new DBhelper();
		helper.connSQL();
		List<Integer>  codes=new ArrayList<>();
		if (text.contains("【一緒によく見られる銘柄")) {
			text=newscut(text, "(.)*【一");
		}		
		for(Company c:list){			
			if (text.contains(String.valueOf(c.id))) {
				codes.add(c.id);
			}			
		}		
		return codes;
		
	}
	 
	 private static void createFile1(String buffer,String s){
			try{
				String path="C:\\Users\\YI\\Desktop\\NewsData\\期間\\本文\\1社\\";
				File newFile=new File(path+s+".txt");
				if(newFile.exists()){
					//追加情報
				}
				if(newFile.createNewFile()){
				Writer p=new OutputStreamWriter(new FileOutputStream(newFile,true),"UTF-8");
				p.write(buffer.toString());
				p.close();
				}else{
					System.err.println("fail");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 
	 private static void createFile2(String buffer,String s){
			try{
				String path="C:\\Users\\YI\\Desktop\\NewsData\\期間\\本文\\2~5社\\";
				File newFile=new File(path+s+".txt");
				if(newFile.exists()){
					//追加情報
				}
				if(newFile.createNewFile()){
				Writer p=new OutputStreamWriter(new FileOutputStream(newFile,true),"UTF-8");
				p.write(buffer.toString());
				p.close();
				}else{
					System.err.println("fail");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	 
	
	public static double CreatefileGetdate(String html,String s) throws Exception{
        Document doc = null;
        double label=0;
        try {
            doc = Jsoup.connect(html).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        String text="";
        if(ContentExtractor.getContentByURL(html)!=null){        	
           text=ContentExtractor.getContentByURL(html).replaceAll("[\\t\\n\\r]", "");
        }else{
        	System.out.println("can't find the text");
        }
        getText gt=new getText();
        
        if(text!=null){
        if(noiseDelete(text)!=null){
        	    String time =getDate.getPubTimeVarious(html, doc.toString());
        	    String b=time.substring(11, 13);
        	    String date=time.substring(0,10);
        	    List<Company> list=new ArrayList<Company>();
        		list=getCompanylist();
        	    String str=noiseDelete(text);
        	    getStockDataforDB sdd=new getStockDataforDB();
        	    List<Integer> codes=new ArrayList<>();
        	    codes=gt.targetNewsCode(list,str);
					if (getCodeSum(str)==1&&(codes.size()==1)) {
				int code = 0;
        	    	  for (int i:codes) {
						code=i;
					}
        	    if(Integer.parseInt(b)>=15){
        	    	  
                      createFile1(str,s+"P");
                      label=sdd.getStockData(code, date, "P");
                      //System.out.println(label);
                      
        	        }else{
        		      createFile1(str,s+"T");
        		      label=sdd.getStockData(code, date, "T");
        	      }
               }else if (codes.size()>1){
            	   if(Integer.parseInt(b)>=15){
                       createFile2(str,s+"P");
         	        }else{
         		      createFile2(str,s+"T");
         	      }
               }else {
				System.out.println("noise!");
			       }
				}
        	
			}
        	return label;
        }
	}

