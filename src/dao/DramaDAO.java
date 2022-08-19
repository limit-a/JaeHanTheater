package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class DramaDAO {
   
   private static DramaDAO DD = null;
   
   
   private DramaDAO() {};
   
   
   public static DramaDAO getInstance() {
      if(DD == null) DD = new DramaDAO(); return DD;
      
   }
   
   JDBCUtil jdbc = JDBCUtil.getInstance();
   
   String sql = null;


   public List<Map<String, Object>> showEDrama() {
      sql = " select distinct theater_title, " + 
            "                theater_start, " + 
            "                theater_end, " + 
            "                THEATER_CONTENT, " +
            "                THEATER_PRICE " +
            "  from theater " + 
            "  where theater_end < sysdate ";
      List<Map<String, Object>> rows = jdbc.selectList(sql);
            
      return rows;
   }
   
   public List<Map<String, Object>> showNDrama() {
      sql = " select distinct theater_title, " + 
            "                theater_start, " + 
            "                theater_end, "   +
            "                THEATER_CONTENT, "  +
            "                THEATER_PRICE " +
            "  from theater " + 
            "  where theater_end >= sysdate "+
               "and theater_start <=sysdate";
      List<Map<String, Object>> rows = jdbc.selectList(sql);
            
      return rows;
   }
   public List<Map<String, Object>> showUDrama() {
      sql = " select distinct theater_title, " + 
            "                theater_start, " + 
            "                theater_end, " +
            "                THEATER_CONTENT, "+
            "                THEATER_PRICE " +
            "  from theater " + 
            "  where theater_start > sysdate ";
      List<Map<String, Object>> rows = jdbc.selectList(sql);
            
      return rows;
   }


   public List<Map<String, Object>> getDrama(List<Object> choosen) {
      
//      sql =    "  select  theater_title, " + 
//            "          theater_DATE, " + 
//            "          theater_time1, " + 
//            "          theater_time2, " + 
//            "          theater_lseat, " + 
//            "          theater_price, " + 
//            "          theater_name " + 
//            "  from theater " + 
//            "  where theater_title = ? ";  
            
      sql = "SELECT * FROM THEATER WHERE THEATER_TITLE = ? ";
      List<Map<String,Object>> getDramaList = jdbc.selectList(sql, choosen);
      return getDramaList;
   }


   public int ticketing(List<Object> param) { // ticketing 추가 
      
      sql =    "INSERT INTO TICKETING(TICKETING_ID, TICKETING_DATE, TICKETING_QTY, USER_ID, TICKETING_STATUS, TICKETING_REFUNDDATE) " + 
            " VALUES('00'||TO_CHAR(SYSDATE, 'YY')||'-'||TO_CHAR(SYSDATE, 'MMdd')||'-'||LPAD(SEQ_TICKETING.NEXTVAL,4,'0'), " + 
            " SYSDATE, ?, ?, 0, NULL)";
      
      int result = jdbc.update(sql, param);
       
      
      return result;
   }


   public void ticket(List<Object> param, List<Object> param2) {
      

      sql = "SELECT TICKETING_ID FROM TICKETING WHERE user_id= '"+ param.get(1) + "' and TICKETING_STATUS = 0" ;
      List<Map<String, Object>> row = jdbc.selectList(sql);

      
      sql = "INSERT INTO TICKET VALUES('22'||LPAD(SEQ_TICKET.NEXTVAL,4,'0'),'"+row.get(0).get("TICKETING_ID")+"','" +param2.get(0)+"')";
      
      for(int i = 1; i <= Integer.parseInt(String.valueOf(param.get(0))); i++){
         jdbc.update(sql);
      }
      
      sql = "UPDATE TICKETING SET TICKETING_STATUS = 1";
      jdbc.update(sql);
      
      
   }


   public List<Map<String, Object>> getDrama(Object selectedDrama) {
      
         sql = " select distinct theater_title, " + 
            "                theater_start, " + 
            "                theater_end, " + 
            "                THEATER_CONTENT, " +
            "                THEATER_PRICE " +
            "  from theater " + 
            "  where theater_title = '"+ selectedDrama+ "'";
      List<Map<String,Object>> row = jdbc.selectList(sql);
      return row;
   }


   public List<Map<String, Object>> showNDrama(Object selectedDrama) {
      List<Map<String,Object>> row = new ArrayList<Map<String,Object>>();
      sql = "SELECT * FROM THEATER WHERE THEATER_TITLE ='"+selectedDrama+"' AND THEATER_DATE >= SYSDATE ";
      row = jdbc.selectList(sql);
      
      return row;
   }

   }