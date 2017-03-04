import java.sql.ResultSet;
import java.sql.SQLException;

public class getStockDataforDB {

	private static double getData(String sql) throws SQLException {
		double label=0;
		DBhelper helper=new DBhelper();
		helper.connSQL();
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if (rs.next()) {
				double close=rs.getDouble("close");
				double open=rs.getDouble("open");
				label=(close-open)/open;
			}	
		}
		helper.deconnSQL();
		return label;
	}
	
	
	public double getStockData(int code,String date,String tag) throws SQLException {
		double label=0;
		if (tag.equals("P")) {
			label=getData("SELECT * FROM stockdb.compantstockdata where id=(SELECT id+1 FROM stockdb.compantstockdata where compantID="+code+" and date='"+date+"') and compantID="+code+";");
		}else{
			label=getData("SELECT * FROM stockdb.compantstockdata where compantID="+code+" and date='"+date+"';");
		}		
		return label;		
	}
}
