
public class insertNews_label {
      public void insertdata(String newsid,double label){
    	  DBhelper helper=new DBhelper();
    	  String sql="insert into news_stock.news_labeldata values('"+newsid+"',"+label+");";
    	  helper.connSQL();
    	  if (helper.insertSQL(sql)) {
			System.out.println(newsid+"     "+label);
		}else {
			System.out.println("insert failed");
		}
    	  helper.deconnSQL();
      }
      
      public boolean insertid_title(String newsid,String link,String title){
    	  boolean f=false;
    	  DBhelper helper=new DBhelper();
    	  String sql="insert into news_stock.link_title values('"+newsid+"','"+title+"','"+link+"');";
    	  helper.connSQL();
    	  if (helper.insertSQL(sql)) {
                  f=true;
		}
    	  helper.deconnSQL();
    	  return f;
      }
}
