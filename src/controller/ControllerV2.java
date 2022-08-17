package controller;

import java.util.Map;

import service.RootService;
import service.UserService;
import service.BoardService;
import service.DramaService;
import service.ReviewService;
import util.ScanUtil;
import util.View2;

public class ControllerV2 {

	public static boolean loggedInUser = false;
	public static Map<String, Object> userInfo = null;
	public static boolean pageStatus = false;

	RootService rootService = RootService.getInstance();
	UserService userService = UserService.getInstance();
	BoardService boardService = BoardService.getInstance();
	DramaService dramaService = DramaService.getInstance();
	ReviewService reviewService = ReviewService.getInstance();

	public static void main(String[] args) {
		new ControllerV2().start();
	}

	private void start() {

		int view = View2.HOME;

		while (true) {
			switch (view) {
			case View2.HOME:
				view = home();
				break;
			case View2.SIGNUP:
				view = rootService.signUp();
				break;
			case View2.LOGIN:
				view = rootService.login();
				break;
			case View2.LOGOUT:
				view = rootService.logout();
				break;

			case View2.USER:
				pageStatus = true;
				view = userService.showPage();
				break;
			case View2.USER_STATUS:
				view = userService.showStatus();
				break;
			case View2.USER_MODIFY:
				view = userService.modifyStatus();
				break;
			case View2.USER_PASS:
				view = userService.modifyPass();
				break;
			case View2.USER_DRAMA:
//				view = userService.
				break;
			case View2.USER_REFUND:
				break;
			case View2.USER_REVIEW:
//				view = userService.showUserReview();
				break;
			case View2.USER_BOARD:
				view = userService.showUserBoard();
				break;

			case View2.BOARD:
				view = boardService.showBoard();
				break;
			case View2.BOARD_SELECT:
				view = boardService.selectBoard();
				break;
			case View2.BOARD_WRITE:
				view = boardService.writeBoard();
				break;
			case View2.BOARD_MODIFY:
				view = boardService.modifyBoard(pageStatus);
				break;
			case View2.BOARD_DELETE:
				view = boardService.deleteBoard(pageStatus);
				break;
				
			case View2.DRAMA:
	             view = dramaService.showDramaList(loggedInUser);
	             break;
	         case View2.DRAMA_INFO:
	             view = dramaService.showDramaInfo();
	             break;
	         case View2.DRAMA_TICKETTING:
	             view = dramaService.dramaTicketing();
	             break;
	         case View2.DRAMA_REVIEW:
	             view = dramaService.dramaReview();
	             break;
	         case View2.DRAMA_REFUND:
	             view = dramaService.ticketRefund();
	             break;

				
			case View2.REVIEW:
				view = reviewService.showReview();
				break;
			case View2.REVIEW_SELECT:
				view = reviewService.selectReview();
				break;
			case View2.REVIEW_WRITE:
				view = reviewService.writeReview();
				break;
			case View2.REVIEW_MODIFY:
				view = reviewService.modifyReview();
				break;
			case View2.REVIEW_DELETE:
				view = reviewService.deleteReview();
				break;
			}

		}

	}

	private int home() {
		System.out.println("=============+++ 재한 극장 +++===================");
		if (!loggedInUser) {
			System.out.println(" 1. 연극 2. 게시판 3. 로그인 4. 회원가입");
		} else {
			System.out.println(" 1. 연극 2. 게시판 3. 마이페이지 4. 로그아웃");
		}
		System.out.println("=================================================");
		System.out.print("입력 >>> ");
		switch (ScanUtil.nextInt()) {
		case 1:
			return View2.DRAMA;
		case 2:
			return View2.BOARD;
		case 3:
			if (!loggedInUser) {
				return View2.LOGIN;
			}
			return View2.USER;
		case 4:
			if (!loggedInUser) {
				return View2.SIGNUP;
			}
			return View2.LOGOUT;
		default:
			return View2.HOME;
		}

	}
}
