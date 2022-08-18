package dao;

import java.util.List;
import java.util.Map;

import controller.ControllerV2;
import util.JDBCUtil;

public class UserDAO {

	private static UserDAO instance = null;

	private UserDAO() {
	}

	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();

	String sql = null;

	public Map<String, Object> getUserInfo() {
		Object userID = ControllerV2.userInfo.get("USER_ID");
		sql = " SELECT * FROM CLIENT WHERE USER_ID='" + userID + "'";
		Map<String, Object> row = jdbc.selectOne(sql);
		return row;
	}

	public int userModify(int userNum, String userValue) {
		sql = " UPDATE CLIENT SET ";
		switch (userNum) {
		case 1:
			sql += " USER_NICK= ";
			break;
		case 2:
			sql += " USER_NAME= ";
			break;
		case 3:
			sql += " USER_BIRTH= ";
			break;
		case 4:
			sql += " USER_PHONE= ";
			break;
		case 5:
			sql += " USER_EMAIL= ";
			break;

		default:
			break;
		}
		sql += "  '" + userValue + "' ";
		sql += " WHERE USER_ID='" + ControllerV2.userInfo.get("USER_ID") + "' ";
		int result = jdbc.update(sql);
		return result;
	}

	public int modifyPass(String anotherPass) {
		Object userID = ControllerV2.userInfo.get("USER_ID");
		sql = "    UPDATE CLIENT                                              "
				+ "   SET USER_PW='" + anotherPass + "'                       "
				+ " WHERE USER_ID='" + userID + "'                            ";
		int result = jdbc.update(sql);
		return result;
	}

	public List<Map<String, Object>> showUserReview() {
		Object userID = ControllerV2.userInfo.get("USER_ID");
		sql = "      SELECT *                                           "
				+ "    FROM CLIENT C, TICKETING T, REVIEW R             "
				+ "   WHERE C.USER_ID='" + userID + "'                  "
				+ "         AND C.USER_ID=T.USER_ID                     "
				+ "         AND T.TICKETING_ID=R.TICKETING_ID           ";
		List<Map<String, Object>> rows = jdbc.selectList(sql);
		return rows;
	}

	public Map<String, Object> selectUserReview(int input) {
		Object userID = ControllerV2.userInfo.get("USER_ID");
		sql = "      SELECT *                                           "
				+ "    FROM CLIENT C, TICKETING T, REVIEW R             "
				+ "   WHERE C.USER_ID='" + userID + "'                  "
				+ "         AND R.REVIEW_ID='" + input + "'             "
				+ "         AND C.USER_ID=T.USER_ID                     "
				+ "         AND T.TICKETING_ID=R.TICKETING_ID           ";
		Map<String, Object> row = jdbc.selectOne(sql);
		return row;
	}

	public Map<String, Object> showUserBoard() {
		Object userNick = ControllerV2.userInfo.get("USER_NICK");
		sql = " SELECT * FROM BOARD WHERE BOARD_NICK='" + userNick + "' ";
		Map<String, Object> row = jdbc.selectOne(sql);
		return row;
	}

	public List<Map<String, Object>> myTicketList() {

		sql = "SELECT distinct A.TICKETING_QTY*C.THEATER_PRICE, "
				+ "       C.THEATER_TITLE, " + "       A.TICKETING_DATE, "
				+ "       a.ticketing_id, " + "       A.TICKETING_QTY, "
				+ "       c.theater_name, " + "       c.theater_time1, "
				+ "       c.theater_time2 "
				+ " FROM TICKETING A, TICKET B, THEATER C "
				+ " WHERE A.TICKETING_ID = B.ticketing_id "
				+ " AND   B.THEATER_ID = C.THEATER_ID "
				+ " AND   A.TICKETING_REFUNDDATE IS NULL "
				+ " AND  a.user_id ='" + ControllerV2.userInfo.get("USER_ID")
				+ "'";
		List<Map<String, Object>> row = jdbc.selectList(sql);

		return row;
	}

	public int refundTicketing(Object selectedTicketing) {
		sql = " DELETE FROM TICKET WHERE TICKETING_ID= '" + selectedTicketing
				+ "' ";
		jdbc.update(sql);
		sql = " UPDATE TICKETING SET TICKETING_REFUNDDATE=SYSDATE WHERE TICKETING_ID='"
				+ selectedTicketing + "' ";
		int result = jdbc.update(sql);
		return result;
	}

}
