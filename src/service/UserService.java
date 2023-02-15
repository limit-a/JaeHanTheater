package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.ControllerV2;
import dao.UserDAO;
import util.ScanUtil;
import util.View2;

public class UserService {

	private static UserService instance = null;

	private UserService() {
	}

	public static UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	UserDAO userDAO = UserDAO.getInstance();
	Map<String, Object> userTicketing = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	public int showPage() {
		Object nick = ControllerV2.userInfo.get("USER_NICK");
		System.out.println("------");
		System.out.println("'" + nick + "'님 환영합니다.");
		System.out.println("┌─────────────────────────────────────────────────────────┐");
		System.out.println("│                                                         │");
		System.out.println("│   1.내 정보 보기   2.내 예매내역 보기   3.내 리뷰보기   │");
		System.out.println("│                                                         │");
		System.out.println("│\t4.내 자유게시판 보기   0.홈으로 돌아가기\t  │");
		System.out.println("│                                                         │");
		System.out.println("└─────────────────────────────────────────────────────────┘");
		while (true) {
			System.out.print("입력 >>> ");
			switch (ScanUtil.nextInt()) {
			case 0:
				ControllerV2.pageStatus = false;
				return View2.HOME;
			case 1:
				return View2.USER_STATUS;
			case 2:
				return View2.USER_DRAMA;
			case 3:
				return View2.USER_REVIEW;
			case 4:
				return View2.USER_BOARD;

			default:
				break;
			}
		}
	}

	public int showStatus() {
		Map<String, Object> userInfo = ControllerV2.userInfo;
		System.out.println("════════════ 내 정보 ════════════");
		System.out.println("아이디 : " + userInfo.get("USER_ID"));
		System.out.println("닉네임 : " + userInfo.get("USER_NICK"));
		System.out.println("이름 : " + userInfo.get("USER_NAME"));
		System.out.print("생년월일 : ");
		System.out.printf("%tF\n", userInfo.get("USER_BIRTH"));
		System.out.println("전화번호 : " + userInfo.get("USER_PHONE"));
		System.out.println("이메일 : " + userInfo.get("USER_EMAIL"));
		System.out.println("═════════════════════════════════");
		System.out.println("┌───────────────────────────────────────────────┐");
		System.out.println("│  1. 회원정보 수정 2. 비밀번호 수정 0. 나가기  │");
		System.out.println("└───────────────────────────────────────────────┘");
		while (true) {
			System.out.print("입력 >>> ");
			switch (ScanUtil.nextInt()) {
			case 1:
				return View2.USER_MODIFY;
			case 2:
				return View2.USER_PASS;
			case 0:
				return View2.USER;

			default:
				break;
			}
		}
	}

	public int modifyStatus() {
		System.out.println("┌───────────────────────────────────────────────────────┐");
		System.out.println("│  1. 닉네임 2. 이름 3. 생년월일 4. 전화번호 5. 이메일  │");
		System.out.println("└───────────────────────────────────────────────────────┘");
		System.out.print("수정할 유저정보 번호 입력 >>> ");
		int userNum = ScanUtil.nextInt();
		System.out.print("수정할 유저정보 값 입력 >>> ");
		String userValue = ScanUtil.nextLine();
		int result = userDAO.userModify(userNum, userValue);
		System.out.println(result + "개 유저정보 수정됨");
		ControllerV2.userInfo = userDAO.getUserInfo();
		return View2.USER_STATUS;
	}

	public int modifyPass() {
		Object currentUserPass = ControllerV2.userInfo.get("USER_PW");
		System.out.print("현재 비밀번호 >>> ");
		String inputPass = ScanUtil.nextLine();
		System.out.print("새로운 비밀번호 >>> ");
		String anotherPass = ScanUtil.nextLine();
		System.out.print("새로운 비밀번호 확인 >>> ");
		String confirmPass = ScanUtil.nextLine();
		if (!(currentUserPass.equals(inputPass))) {
			System.out.println("현재 비밀번호가 일치하지 않습니다.");
		} else if (!(anotherPass.equals(confirmPass))) {
			System.out.println("비밀번호 확인이 일치하지 않습니다.");
		} else {
			int result = userDAO.modifyPass(anotherPass);
			System.out.println(result + "개 비밀번호 변경");
		}
		return View2.USER_STATUS;
	}

	public int showUserBoard() {
		Map<String, Object> row = userDAO.showUserBoard();
		if (row == null || row.size() == 0) {
			System.out.println("유저가 작성한 게시판이 없습니다.");
			return View2.USER_STATUS;
		} else {
			System.out.println(row);
			System.out.println("┌──────────────────────┐");
			System.out.println("│  1. 선택  0. 나가기  │");
			System.out.println("└──────────────────────┘");
			while (true) {
				System.out.print("입력 >>> ");
				switch (ScanUtil.nextInt()) {
				case 1:
					return View2.BOARD_SELECT;
				case 0:
					return View2.USER;

				default:
					break;
				}
			}
		}
	}

	
	public int showMyTicketing() {
		   
		   System.out.println("==============내 예매내역========================");
		   int count = 0;
		   List<Map<String,Object>> myTicketList = new ArrayList<Map<String,Object>>();
		   myTicketList = userDAO.myTicketList();
		   
		   if(myTicketList == null) {
		      System.out.println("예매 내역이 없습니다 ! ");
		      return View2.USER;
		   }
		   // 예매내역이 있으면    1. 돌아가기  2. 환불  
		   System.out.println(myTicketList);
		   for(Map<String,Object> item : myTicketList) {
		      System.out.println(++count + "극 이름:" + item.get("THEATER_TITLE") + "예매 수량 " + item.get("TICKETING_QTY") + "결제금액 :" + item.get("A.TICKETING_QTY*C.THEATER_PRICE") + "구매시간:" +sdf.format(item.get("TICKETING_DATE")));
		   }
		   try {
		      System.out.println("선택>>");
		      int choose = ScanUtil.nextInt();
		      userTicketing = myTicketList.get(choose-1);
		      //[{A.TICKETING_QTY*C.THEATER_PRICE=48000, TICKETING_QTY=3, TICKETING_ID=0022-0817-0034, THEATER_TIME2=21:00, TICKETING_DATE=2022-08-17 12:18:46.0, THEATER_TIME1=19:00, THEATER_TITLE=드립소년단, THEATER_NAME=행복관}, {A.TICKETING_QTY*C.THEATER_PRICE=32000, TICKETING_QTY=2, TICKETING_ID=0022-0817-0035, THEATER_TIME2=21:00, TICKETING_DATE=2022-08-17 12:19:10.0, THEATER_TIME1=19:00, THEATER_TITLE=시간을 파는 상점, THEATER_NAME=믿음관}]
		      System.out.println(userTicketing.get("TICKETING_ID"));
		      System.out.println("티켓번호:" +userTicketing.get("TICKETING_ID") + "극이름 :" + userTicketing.get("THEATER_TITLE") + "상영시간" + userTicketing.get("THEATER_TIME1") +"~"+ userTicketing.get("THEATER_TIME2") + "상영관" + userTicketing.get("THEATER_NAME"));
		      System.out.println("1.돌아가기 2. 환불하기 ");
		      switch(ScanUtil.nextInt()) {
		      case 1: userTicketing = null; return View2.USER;
		      case 2: return View2.USER_REFUND;
		      default : return View2.USER;
		      }
		      
		   }catch(Exception e) {
		      System.out.println("잘못입력");
		      return View2.USER;
		   }
		   
		}
	
	public int refundTicketing() { 
		/*
		 *  환불 : 
		 *     1. 회원의 티켓팅 번호를 가져옴 
		 *     2. 선택 후  예매 정보는 삭제 안하고  refunddate 기입  
		 *     3. 해당 ticketing_id를 가지고 ticket 삭제 
		 *     4. 트리거로 인해 ticket삭제시 잔여좌석 반환 
		 */
		   System.out.println("해당 예매를 취소하시겠습니까?");
		   System.out.println("1. 예 2. 아니오");
		   while (true) {
		   System.out.print("입력 >>> ");
		   switch (ScanUtil.nextInt()) {
		   case 1:
		      int result = userDAO.refundTicketing(userTicketing.get("TICKETING_ID"));
		      userTicketing = null;
		      return View2.USER;
		   case 2:
		      return View2.USER;

		   default:
		      break;
		   }
		}
		   
		}
	
	


	
//	public int showUserReview() {
//		Map<String, Object> row = userDAO.showUserReview();
//		return 0;
//	}

}
