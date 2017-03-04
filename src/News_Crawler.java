import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.parser.DTD;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class News_Crawler {
	
    private  Title_Link getTitleandLink(String str){
    	Title_Link tl=new Title_Link();
    	Pattern pattern1 = Pattern.compile("\"(.)*\"");
		Matcher matcher1 = pattern1.matcher(str.toString());
		String url="";
		if(matcher1.find()){
			String s=matcher1.group(0).toString();
		    url=s.substring(1, s.length()-1);
		    tl.Link="http://news.finance.yahoo.co.jp"+url;
		}
		Pattern pattern2 = Pattern.compile(">(.)*<");
		Matcher matcher2 = pattern2.matcher(str.toString());
		String title="";
		if(matcher2.find()){
			title=matcher2.group(0);
			tl.Title=title.substring(1, title.length()-1);
			//System.out.println(tl.Title);
		}
    	return tl;
    }
	

    
    
    
	public List<Title_Link> getURLs(String url){
		List<Title_Link> URLs=new ArrayList();
		News_Crawler nc=new News_Crawler();
		
		Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Pattern pattern = Pattern.compile("<a href=\"/detail((?!>).)*>((?!>).){10,}</a>");
		  Matcher matcher = pattern.matcher(doc.toString());
		  while (matcher.find()){
			  //URLs.add(matcher.group(0));
			  Title_Link tl=new Title_Link();
			  tl=nc.getTitleandLink(matcher.group(0));
			  if(tl.Link.contains("vip")){
				  
			  }else{
				  URLs.add(tl);
			  }	
		  }           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   				
		return URLs;
	}
	
	public List<String> getDateList(String startDate,String endDate){
		List<String> list=new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
		Date bdate = null;
		Date edate = null;
		try {
			bdate = formatter.parse(startDate);
			edate = formatter.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.add(startDate);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(bdate);
		boolean b=true;
		while (b) {
			calendar.add(Calendar.DAY_OF_MONTH,1);
			if (edate.after(calendar.getTime())) {
				list.add(formatter.format(calendar.getTime()));
			}else{
				break;
			}
		}
		list.add(endDate);
		return list;
	}
	
	private List<String> getPages(String dateLink) {
		List<String> pages=new ArrayList<>();
		Document doc = null;
        try {
            doc = Jsoup.connect(dateLink).get();
            int sum = 0;
		    Pattern pattern = Pattern.compile(">[0-9]{1,4}</strong>件中");
		    Matcher matcher = pattern.matcher(doc.toString());
		    
		    if (matcher.find()) {
		    	Pattern pattern1 = Pattern.compile("[0-9]{1,4}");
			    Matcher matcher1 = pattern1.matcher(matcher.group(0));
		    	if (matcher1.find()) {
					String str=matcher1.group(0);
				    //System.out.println(str);
				    sum=Integer.valueOf(str);
				}		    
			}
		    if (sum>14) {
				for(int i=1;i<=sum/15+1;i++){
					//System.out.println(i);
					pages.add(String.valueOf(i));
				}
			}else {
				pages.add("1");
			}
		   	    
            }catch(IOException e){
            	 // TODO Auto-generated catch block
                e.printStackTrace();
               }
		return pages;
	}
	
	
	public static void main(String[] args){
		News_Crawler nc=new News_Crawler();
		List<Title_Link> URLs=new ArrayList();
		List<String> list=new ArrayList<String>();
		list=nc.getDateList("2016-12-01", "2016-12-20");//ニュースの収集期間を指定する
		List<String> pages=new ArrayList<>();
		getText gt=new getText();
		
		for(String d:list){//date list
			String date=d.replace("-", "");
			System.out.println("date:  "+d);
			String url="http://news.finance.yahoo.co.jp/category/stocks/?date="+date;
			pages=nc.getPages(url);
			for (String p:pages) {//page list
				System.out.println("page:  "+p);
				URLs=nc.getURLs("http://news.finance.yahoo.co.jp/category/stocks/?p="+p+"&date="+date);
			    int n=0;
				  for(Title_Link link : URLs){
					 n++;
					 double label;
					 insertNews_label news_label=new insertNews_label();
					 String id=String.format("%2d", n).replace(" ", "0");
					 String page=String.format("%2d", Integer.valueOf(p)).replace(" ", "0");
					 String textid=date+page+id;
					 try {
						label=gt.CreatefileGetdate(link.Link, textid);
						if (label!=0) {
							news_label.insertdata(textid, label);
							if (news_label.insertid_title(textid, link.Link, link.Title)) {
								System.out.println(link.Title);
							}else {
								System.out.println("insert link and title failed");
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
			    	 //System.out.println(date+"     "+p); 
			         
			         
		        }
			} 

			
			
		}
		
		
		
	}
}
