package service;

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
		System.out.print("생년월일 : " );
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

}
