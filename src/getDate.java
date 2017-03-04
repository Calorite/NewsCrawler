
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class getDate {

	private static String url_reg_whole="([-|/|_]{1}20\\d{6})";
	private static String url_reg_sep_ymd="([-|/|_]{1}20\\d{2}[-|/|_]{1}\\d{1,2}[-|/|_]{1}\\d{1,2})";
	private static String url_reg_sep_ym="([-|/|_]{1}20\\d{2}[-|/|_]{1}\\d{1,2})";
	private static Calendar current=Calendar.getInstance();
    public static String getPubTimeVarious(String url,String urlContent) throws ParseException{
    	String pubTime=getPubTimeFromUrl(url);

    	if(QueryTime(pubTime)){
    		pubTime=pubTime+" "+GetTime(urlContent);

    	}
    	return pubTime;
    }

    
    public static String getPubTimeFromUrl(String url) throws ParseException{
    	Pattern p=Pattern.compile(url_reg_whole);
    	Matcher m=p.matcher(url);
    	if(m.find(0)&&m.groupCount()>0){
    		String time =m.group(0);
    		time=time.substring(1, time.length());
    		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
    		Date date =sdf.parse(time);
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(date);
    	    if(current.compareTo(calendar)>=0){
    	    	return time.substring(0, 4)+"-"+time.substring(4, 6)+"-"+time.substring(6, 8);
    	    }
    	}
    	
    	p=null;
    	m=null;
    	Pattern p_sep=Pattern.compile(url_reg_sep_ymd);
    	Matcher m_sep=p_sep.matcher(url);
    	if(m_sep.find(0)&&m_sep.groupCount()>0){
    		String time =m_sep.group(0);
    		time=time.substring(1, time.length());
    		 String[] seg = time.split("[-|/|_]{1}");
             Calendar theTime = Calendar.getInstance();
             theTime.set(Calendar.YEAR,Integer.parseInt(seg[0]));
             theTime.set(Calendar.MONTH, Integer.parseInt(seg[1]));
             theTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(seg[2]));
             if(current.compareTo(theTime)>=0)
                {
             
            return String.format("%02d", seg[0])+"-"+String.format("%02d", seg[1])+"-"+String.format("%02d", seg[2]);
                }	
    	}
    	p_sep = null;
        m_sep = null;
        Pattern p_sep_ym = Pattern.compile(url_reg_sep_ym);
        Matcher m_sep_ym = p_sep_ym.matcher(url);
        if(m_sep_ym.find(0)&&m_sep_ym.groupCount()>0)
        {
             String time =  m_sep_ym.group(0);
             time = time.substring(1,time.length());
             Calendar theTime = Calendar.getInstance();
             String[] seg = time.split("[-|/|_]{1}");
             theTime.set(Calendar.YEAR,Integer.parseInt(seg[0]));
             theTime.set(Calendar.MONTH, Integer.parseInt(seg[1]));
             theTime.set(Calendar.DAY_OF_MONTH, 1);
             if(current.compareTo(theTime)>=0)
            {
              
            return seg[0]+"-"+seg[1]+"-"+"01";
            }
        }
		return null;
    }

    
    public static String GetTime(String HTML){
    	Pattern p=Pattern.compile("[0-9]{1,2}[][0-9]{1,2}[•ª”zM]");
		 Matcher m=p.matcher(HTML);
		 String t=null;
		 if(m.find()){
			 String time=m.group();
			 time=time.replaceAll("•ª","");
			 String[] Ti=time.split("");
			 int a=Integer.valueOf(Ti[0]);
			 int b=Integer.valueOf(Ti[1]);
			 t=String.format("%02d", a)+":"+String.format("%02d", b);
			 
			 
		 }			
		return t;  	
    }
  
    public static Boolean QueryTime(String time){
		Pattern p=Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?");
		Matcher m=p.matcher(time);
		Boolean b;
		if(m.find()){
			b=false;
		}else b=true;
    	return b;  	
    }
    
}

