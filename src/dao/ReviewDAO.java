package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ReviewDAO {
	
	private static ReviewDAO instance = null;

	private ReviewDAO() {
	}

	public static ReviewDAO getInstance() {
		if (instance == null) {
			instance = new ReviewDAO();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();
	String sql = null;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬연극 리뷰 정보▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public Map<String, Object> dramaInfo (Object selectedDrama) {		
		sql = "SELECT THEATER_TITLE, " 
				+ "   AVG(REVIEW_SCORE), " 
				+ "   COUNT(REVIEW_ID) "
				+ "FROM REVIEW "
				+ "WHERE THEATER_TITLE = '" + selectedDrama + "' "
				+ "GROUP BY THEATER_TITLE ";
		Map<String, Object> row = jdbc.selectOne(sql);
		return row;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬내가 예매를 했는가?▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public List<Map<String, Object>> myTicketing(String userId, Object selectedDrama) {
		sql = "     SELECT DISTINCT A.USER_ID, "
				+ "        B.TICKETING_ID, "
				+ "        D.* "
				+ "   FROM CLIENT A, TICKETING B, TICKET C, THEATER D "
				+ "  WHERE A.USER_ID=B.USER_ID "
				+ "    AND B.TICKETING_ID=C.TICKETING_ID "
				+ "    AND C.THEATER_ID=D.THEATER_ID "
				+ "    AND A.USER_ID= '" + userId + "' "
				+ "    AND D.THEATER_TITLE= '" + selectedDrama + "'";
		List<Map<String, Object>> row = jdbc.selectList(sql);
		return row;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬내가 리뷰를 작성했는가?▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬	
	public List<Map<String, Object>> checkReview(String ticketingID) {
		sql = "Select * "
				+ "FROM REVIEW "
				+ "WHERE TICKETING_ID = '" + ticketingID + "'";
		List<Map<String, Object>> rows = jdbc.selectList(sql);
		return rows;
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬비회원용: 모든 리뷰 보기▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public List<Map<String, Object>> showReview(String theaterTitle) {
		sql = 	  "SELECT * " 
				+ "  FROM REVIEW " 
				+ "  WHERE THEATER_TITLE='" + theaterTitle + "'";
		List<Map<String, Object>> rows = jdbc.selectList(sql);
		return rows;
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬회원용: 내가 쓴 리뷰 보기▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬	
	public List<Map<String, Object>> myReview(String userId, String theaterTitle) {
		sql = "     SELECT A.USER_NICK, B.TICKETING_ID, C.THEATER_TITLE, C.REVIEW_ID, C.REVIEW_SCORE, C.REVIEW_CONTENT "
				+ "   FROM CLIENT A, TICKETING B, REVIEW C "
				+ "  WHERE A.USER_ID=B.USER_ID "
				+ "    AND B.TICKETING_ID=C.TICKETING_ID "
				+ "    AND A.USER_ID= '" + userId + "' "
				+ "    AND C.THEATER_TITLE= '" + theaterTitle + "'";
		List<Map<String, Object>> row = jdbc.selectList(sql);
		return row;
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬회원용: 나를 제외한 회원들의 리뷰 보기▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public List<Map<String, Object>> membershowReview(String userID, Object selectedDrama) {
		sql = "    SELECT A.USER_NICK, "
				+ "       B.TICKETING_ID, "
				+ "       C.THEATER_TITLE, " 
				+ "       C.REVIEW_ID, " 
				+ "       C.REVIEW_SCORE, " 
				+ "       C.REVIEW_CONTENT "
				+ "  FROM CLIENT A, TICKETING B, REVIEW C "
				+ " WHERE A.USER_ID=B.USER_ID "
				+ "   AND B.TICKETING_ID=C.TICKETING_ID "
				+ "   AND A.USER_ID!= '" + userID + "'"
				+ "   AND C.THEATER_TITLE= '" + selectedDrama + "'";
		List<Map<String, Object>> rows = jdbc.selectList(sql);
		return rows;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 선택▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public Map<String, Object> selectReview(int input){
		sql = " SELECT * FROM REVIEW WHERE REVIEW_ID=' " + input + " ' ";
		Map<String, Object> row = jdbc.selectOne(sql);
		return row;
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 쓰기▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int writeReview(List<Object> param){
		sql = "INSERT INTO REVIEW( 					"
				+ "REVIEW_ID, 						"
				+ "REVIEW_SCORE, 					"
				+ "REVIEW_CONTENT, 					"
				+ "TICKETING_ID, 					"
				+ "THEATER_TITLE) 					"
				+ "									"
				+ "VALUES(							"
				+ "   SEQ_NUMBER_REVIEW.nextval,	"
				+ "	  ?,							"
				+ "	  ?,							"
				+ "	  ?,         					" 
				+ "	  ? )						    ";				
		return jdbc.update(sql, param);	
	}
	

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 수정▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int modifyReview(int reviewScore, String reviewContent, String ticketingId) {
		sql = "UPDATE REVIEW " 
				+ "SET REVIEW_SCORE = '" + reviewScore + "', "
				+ "    REVIEW_CONTENT='" + reviewContent + "' "
			    + "WHERE TICKETING_ID='" + ticketingId + "'";
		return jdbc.update(sql);
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 삭제▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int deleteReview(int reviewID) {
		sql = " DELETE FROM REVIEW " + 
				  " WHERE REVIEW_ID = '" + reviewID + "' ";
			return jdbc.update(sql);
	}

	
}
