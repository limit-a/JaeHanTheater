package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	
	/*
	 *  
	 *  JDBC를 좀더 쉽고 편하게 사용하기 위한 Utility 클래스
	 *  
	 *  Map<String, Object> selectOne(String sql);
	 *  Map<String, Object> selectOne(String sql, List<Object> param)
	 *  List<Map<String,Object>> selectList(String sql)
	 *  List<Map<String, Object>> selectList(String sql, List<Object> param)
	 *  int update(String sql)
	 *  int update(String sql, List<Object> param)
	 *  
	 */
	
	// 싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴
	
	
	//인스턴스를 보관할 변수 
	private static JDBCUtil instance = null;
	
	// JDBCUtil 객체를 만들 수 없게 (인스턴스화 할 수 없게) private으로 제한함 
	private JDBCUtil(){};
	
	public static JDBCUtil getInstance() {
		if(instance == null) instance = new JDBCUtil();
		return instance;
	}
	 
	
	 private String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	 private String USER = "MJ";
	 private String PASSWORD = "java";

	 Connection conn = null;
	 PreparedStatement ps = null;
	 ResultSet rs = null;
	
	 public Map<String, Object> selectOne(String sql, List<Object> param){
		 
		 //sql => SELECT * FROM JAVA_BOARD WHERE B_NUMBER=?"
		 //param = >  [1]
		 
		 //sql => SELECT * FROM JAVA_BOARD WHERE WRITER=? AND TITLE=?"
		 //param = >  ["홍길동", "안녕"]
		 
		 Map<String,Object> row = null;
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 for(int i = 0; i < param.size(); i++) {
				 ps.setObject(i + 1, param.get(i)); // ?는 1부터시작함
			 }
			
			 rs = ps.executeQuery();
			 
			 //컬럼명을 알고싶어서 메타데이터를 사용
			 ResultSetMetaData rsmd = rs.getMetaData(); 
			 
			 int columnCount = rsmd.getColumnCount();
			 
			 while(rs.next()) {
				 row = new HashMap<>();
				 for(int i = 1; i <= columnCount; i++) {
					 String key = rsmd.getColumnLabel(i);
					 //getColumnName vs getColumnLabel
					 // getColumnName : 원본 컬럼명을 가져옴 
					 // getColumnLabel : as로 선언된 별명을 가져옴, 없으면 원본 컬럼명
					 Object value = rs.getObject(i);
					 row.put(key, value);
				 }
				 //{DATETIME=2022-08-05 17:06:35.0, WRITER=와이와이, TITLE=나나이야기, CONTENT=지금이순간, B_NUMBER=3}
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
		 }finally {
				if(rs != null) try {rs.close();} catch (Exception e) { }
				if(ps != null) try {ps.close();} catch (Exception e) { }
				if(conn != null) try {conn.close();} catch (Exception e) { }
					
				}
		 
		 return row;
	 }
	 
	 // SELECT를 하여 한줄출력 
	 public Map<String, Object> selectOne(String sql){
		 
		//sql => "SELECT * FROM JAVA_BOARD WHERE B_NUMBER=(SELECT MAX(B_NUMBER) FROM JAVA_BOARD)"
		// sql => "SELECT * FROM MEMBER MEM_ID='a001' AND MEM_PASS = '123'"
		 Map<String,Object> row = null;
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
			 //컬럼명을 알고싶어서 메타데이터를 사용
			 ResultSetMetaData rsmd = rs.getMetaData(); 
			 
			    
		
			 int columnCount = rsmd.getColumnCount();
			 while(rs.next()) {
				 row = new HashMap<>();
				 for(int i = 1; i <= columnCount; i++) {
					 String key = rsmd.getColumnLabel(i);
					 //getColumnName vs getColumnLabel
					 // getColumnName : 원본 컬럼명을 가져옴 
					 // getColumnLabel : as로 선언된 별명을 가져옴, 없으면 원본 컬럼명
					 Object value = rs.getObject(i);
					 row.put(key, value);
				 }
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
		 }finally {
				if(rs != null) try {rs.close();} catch (Exception e) { }
				if(ps != null) try {ps.close();} catch (Exception e) { }
				if(conn != null) try {conn.close();} catch (Exception e) { }
					
				}
		 
		 return row;
	 }
	 
	 
	 public int update(String sql) {
		 
		 // sql =>  "DELETE FROM JAVA_BOARD" ?가 없는 SQL
		 // sql =>
		 // SQL => "UPDATE JAVA_BOARD SET TITLE ='하하' "
		 // SQL =>  "INSERT MY_MEMBER(MEM_ID,MEM_PASS, MEM_NAME) VALUES ('admin', '1234','홍길동') "
		 int result = 0;
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 result = ps.executeUpdate();
			 
			 
		 }catch(SQLException e){
			 e.printStackTrace();
		 }finally {
			 if(rs != null) try {rs.close();} catch (Exception e) { }
			 if(ps != null) try {ps.close();} catch (Exception e) { }
			 if(conn != null) try {conn.close();} catch (Exception e) { }
			 
		 }
		 return result;
	 }
	 
	 public int update(String sql,List<Object> param) {
		 int result = 0;
		 // sql =>  "DELETE FROM JAVA_BOARD WHERE B_NUMBER=?" ?가 있는 SQL
		 // sql =>  "UPDATE JAVA_BOARD SET TITLE ='하하' WHERE B_NUMNER =?"
		 // SQL =>  "INSERT MY_MEMBER(MEM_ID,MEM_PASS, MEM_NAME) VALUES (?, ?, ?) "
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 for(int i = 0; i< param.size(); i++) {
				 ps.setObject(i + 1, param.get(i));
			 }
			 result = ps.executeUpdate();
			 
			 
		 }catch(SQLException e){
			 e.printStackTrace();
		 }finally {
			 if(rs != null) try {rs.close();} catch (Exception e) { }
			 if(ps != null) try {ps.close();} catch (Exception e) { }
			 if(conn != null) try {conn.close();} catch (Exception e) { }
			 
		 }
		 return result;
	 }
	 
	 public List<Map<String,Object>> selectList(String sql, List<Object> param){
		 
		 //sql => "SELECT * FROM MEMBER WHERE MEME_ADD1 LIKE '%'||?||'%'"
		
		 //sql => "SELECT * FROM JAVA_BOARD WHERE WRITER =?"
		 //sql => "SELECT * FROM JAVA_BOARD WHERE B_NUMBER > ?"	 
		 List<Map<String,Object>> result = null;
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 for(int i = 0 ; i < param.size(); i++) {
				 ps.setObject(i + 1, param.get(i));
			 }
			 
			 
			 rs = ps.executeQuery();
			 
			 ResultSetMetaData rsmd = rs.getMetaData();
			 int columnCount = rsmd.getColumnCount();
			 
			 while(rs.next()) {
				 if(result == null) result = new ArrayList<>();
				 Map<String, Object> row = new HashMap<>();
				 for(int i = 1; i <=columnCount; i++) {
					 String key = rsmd.getColumnLabel(i);
					 Object value = rs.getObject(i);
					 row.put(key, value);
				 }
				 result.add(row);
				 
			 }
			 
			 
		 }catch(SQLException e) {
			 e.printStackTrace();
		 }finally {
			 if(rs != null) try {rs.close();} catch(Exception e) {}
			 if(ps != null) try {ps.close();} catch(Exception e) {}
			 if(conn != null) try {conn.close();} catch(Exception e) {}
		 }
		 
		 return result;
	 }
	public List<Map<String,Object>> selectList(String sql){
		 
		//sql => "SELECT * FROM MEMBER"
		//sql => "SELECT * FROM JAVA_BOARD"
		//sql => "SELECT * FROM JAVA_BOARD WHERE B_NUMBER >10"
		
		 List<Map<String,Object>> result = null;
		 try {
			 conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 ps = conn.prepareStatement(sql);
			 
			 rs = ps.executeQuery();
			 
			 ResultSetMetaData rsmd = rs.getMetaData();
			 int columnCount = rsmd.getColumnCount();
			 
			 while(rs.next()) {
				 if(result == null) result = new ArrayList<>();
				 Map<String, Object> row = new HashMap<>();
				 for(int i = 1; i <=columnCount; i++) {
					 String key = rsmd.getColumnLabel(i);
					 Object value = rs.getObject(i);
					 row.put(key, value);
				 }
				 result.add(row);
				 
			 }
			 
			 
		 }catch(SQLException e) {
			 e.printStackTrace();
		 }finally {
			 if(rs != null) try {rs.close();} catch(Exception e) {}
			 if(ps != null) try {ps.close();} catch(Exception e) {}
			 if(conn != null) try {conn.close();} catch(Exception e) {}
		 }
	 
		 return result;
	}
	 
}
