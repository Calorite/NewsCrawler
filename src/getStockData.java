import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class getStockData {//return StockData
	public static final String YAHOO_FINANCE_URL = "http://table.finance.yahoo.com/table.csv?";		
	public static List<StockData> getStockCsvData(String stockName, String fromDate,String toDate) {
		List<StockData> list = new ArrayList<StockData>();
		String[] datefromInfo= fromDate.split("-");
		String[] toDateInfo = toDate.split("-");
		String code = stockName.substring(0, 4);;

		String a = (Integer.valueOf(datefromInfo[1])-1)+"";
		String b = datefromInfo[2];
		String c =  datefromInfo[0];
		String d = (Integer.valueOf(toDateInfo[1])-1)+"";
		String e = toDateInfo[2];
		String f =  toDateInfo[0];

		String params = "&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e="
		+ e + "&f=" + f;
		String url = YAHOO_FINANCE_URL + "s=" + stockName + params;
		URL MyURL = null;
		URLConnection con = null;
		InputStreamReader ins = null;
		BufferedReader in = null;
		try {
		MyURL = new URL(url);
		con = MyURL.openConnection();
		ins = new InputStreamReader(con.getInputStream(), "UTF-8");
		in = new BufferedReader(ins);

		String newLine = in.readLine();// 

		while ((newLine = in.readLine()) != null) {
		String stockInfo[] = newLine.trim().split(",");
		StockData sd=new StockData();
		sd.setCode(code);
		sd.setDate(stockInfo[0]);
		sd.setOpen(Float.valueOf(stockInfo[1]));
		sd.setHigh(Float.valueOf(stockInfo[2]));
		sd.setLow(Float.valueOf(stockInfo[3]));
		sd.setClose(Float.valueOf(stockInfo[4]));
	//	sd.setVolume(Float.valueOf(stockInfo[5]));
	//	sd.setAdj(Float.valueOf(stockInfo[6]));
		list.add(sd);
		}

		} catch (Exception ex) {
		return null; //
		} finally {
		if (in != null)
		try {
		in.close();
		} catch (IOException ex) {
		ex.printStackTrace();
		}
		}
		return list;
		}	
	
	
	public static StockData getStockCsvData(String stockName, String date){
		List<StockData> list = getStockCsvData(stockName,date,date);
		return ((list.size()>0)?list.get(0):null);
	}	
	
	 private static List<Integer> getCompanylist() throws SQLException {
		List<Integer> list=new ArrayList<>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		String sql="SELECT * FROM stockdb.nikei225;";
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				Company com=new Company();
				com.id=rs.getInt("id");
				list.add(com.id);
			}
		}
		helper.deconnSQL();
		return list;
	}
	
	
	public static void main(String[] args) throws SQLException {
		News_Crawler nc=new News_Crawler();
		List<Integer> list=new ArrayList<>();
		list=getCompanylist();
		getStockData gsd=new getStockData();
		for (int code:list) {
                    List<StockData> stockDatas=new ArrayList<>();
			     	stockDatas=gsd.getStockCsvData(String.valueOf(code), "2016-12-01","2016-12-27");
			     	DBhelper bhelper=new DBhelper();
			     	bhelper.insert(stockDatas);
						
		}
	}
	
}
