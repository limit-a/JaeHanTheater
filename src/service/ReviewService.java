package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.ControllerV2;
import dao.ReviewDAO;
import util.ScanUtil;
import util.View2;

public class ReviewService {

	private static ReviewService instance = null;

	private ReviewService() {
	}

	public static ReviewService getInstance() {
		if (instance == null) {
			instance = new ReviewService();
		}
		return instance;
	}

	ReviewDAO dao = ReviewDAO.getInstance();
	DramaService dramaService = DramaService.getInstance();

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 출력▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public void printReviewLine(Map<String, Object> item) {
		System.out.printf("[리뷰번호%2s]%8s", item.get("REVIEW_ID"), "");
		int result = Integer.parseInt((String.valueOf(item.get("REVIEW_SCORE"))));
		switch (result) {
		case 1:
			System.out.print("☆☆☆☆★");
			break;
		case 2:
			System.out.print("☆☆☆★★");
			break;
		case 3:
			System.out.print("☆☆★★★");
			break;
		case 4:
			System.out.print("☆★★★★");
			break;
		case 5:
			System.out.print("★★★★★");
			break;
		default:
			break;
		}
		System.out.printf("%20s\t", item.get("TICKETING_ID"));
		if (String.valueOf(item.get("REVIEW_CONTENT")).length() < 20) {
			System.out.print(String.valueOf(item.get("REVIEW_CONTENT")) + "…더보기\n");
		} else {
			System.out.println(String.valueOf(item.get("REVIEW_CONTENT")).substring(0, 20) + "…더보기\n");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰창 전체
	// 보여주기▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int showReview() {
		// 1. 연극에 딸린 리뷰 상황 보여주기
		String drama = (String) dramaService.selectedDrama;
		Map<String, Object> dramaInfo = dao.dramaInfo(drama);
		System.out.println();
		System.out.println();
		System.out.println(); // ControllerV2.clearScreen();
		System.out.println(
				"────────────────────────────────────────────────────────────────────────────────────────────────");
		System.out.println("⸙" + drama + "⸙  리뷰 게시판");
		if (dramaInfo == null || dramaInfo.size() == 0) {
			System.out.println("등록된 리뷰가 없습니다.");
			System.out.println(
					"────────────────────────────────────────────────────────────────────────────────────────────────");

			String userID = (String) ControllerV2.userInfo.get("USER_ID");
			List<Map<String, Object>> myticket = dao.myTicketing(userID, drama);
			if (myticket == null || myticket.size() == 0) {
				System.out.println("아무 키나 입력하시면 다시 연극 초기 메뉴로 돌아갑니다.");
				ScanUtil.nextLine();
				return View2.DRAMA;
			} else {
				System.out.println("예매내역이 있으므로 리뷰 작성이 가능합니다.");
				System.out.println("┌──────────────────────────────────────────┐");
				System.out.println("│ 1. 리뷰 작성하기 0. 연극 홈으로 돌아가기 │");
				System.out.println("└──────────────────────────────────────────┘");
				System.out.print("입력 >>> ");
				switch (ScanUtil.nextInt()) {
				case 1:
					return View2.REVIEW_WRITE;
				case 0:
					return View2.DRAMA;
				}
			}
		} else {
			BigDecimal dreviewScore = (BigDecimal) dramaInfo.get("AVG(REVIEW_SCORE)");
			System.out.printf("평균 평점 %.1f점", dreviewScore.doubleValue());
			System.out.println("(" + dramaInfo.get("COUNT(REVIEW_ID)") + "명 참여)");
		}

		// 2. 비회원인 경우: 리뷰 보여주기
		if (ControllerV2.loggedInUser == false) {
			List<Map<String, Object>> rows = dao.showReview(drama);
			System.out.printf("\n%s%14s%14s%29s\n", "리뷰번호", "평점", "예매번호", "리뷰");
			System.out.println(
					"┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
			if (rows == null || rows.size() == 0) {
			} else {
				for (Map<String, Object> item : rows) {
					printReviewLine(item);
				}
				System.out.println(
						"────────────────────────────────────────────────────────────────────────────────────────────────");
			}

			// 3-1. 회원인 경우: 나 외의 다른 사람이 쓴 리뷰 보여주기
		} else if (ControllerV2.loggedInUser == true) {
			// 회원인 경우 1. 타회원이 작성한 리뷰 보여주기
			String userID = (String) ControllerV2.userInfo.get("USER_ID");
			List<Map<String, Object>> rows = dao.membershowReview(userID, drama);
			System.out.printf("\n%s%14s%14s%29s\n", "리뷰번호", "평점", "예매번호", "리뷰");
			System.out.println(
					"┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
			if (rows == null || rows.size() == 0) {
			} else {
				for (Map<String, Object> item : rows) {
					printReviewLine(item);
				}
				System.out.println(
						"────────────────────────────────────────────────────────────────────────────────────────────────");
			}

			// 3-2. 회원인 경우: 내가 쓴 리뷰 보여주기
			List<Map<String, Object>> myRow = dao.myReview(userID, drama);
			if (myRow == null || myRow.size() == 0) {
			} else {
				System.out.println(ControllerV2.userInfo.get("USER_NICK") + "님이 쓴 리뷰가 존재합니다.");
				System.out.println("내 리뷰의 내용을 상세 조회할 경우 수정/삭제 버튼이 활성화됩니다.");
				for (Map<String, Object> item : myRow) {
					printReviewLine(item);
				}
				System.out.println(
						"────────────────────────────────────────────────────────────────────────────────────────────────");
			}
		}

		while (true) {
			if (ControllerV2.loggedInUser == false) {
				System.out.println("비회원은 리뷰 조회/작성이 제한됩니다.");
				System.out.println(
						"────────────────────────────────────────────────────────────────────────────────────────────────");
				System.out.println();
				System.out.println("┌─────────────────────────────────────────────┐");
				System.out.println("│ 1. 리뷰 자세히 보기 0. 연극 홈으로 돌아가기 │");
				System.out.println("└─────────────────────────────────────────────┘");
			} else {
				System.out.println("┌────────────────────────────────────────────────────────────────┐");
				System.out.println("│ 1. 리뷰 상세 조회하기 2. 리뷰 작성하기 0. 연극 홈으로 돌아가기 │");
				System.out.println("└────────────────────────────────────────────────────────────────┘");
			}
			while (true) {

				System.out.print("입력 >>> ");
				switch (ScanUtil.nextInt()) {
				case 1:
					return View2.REVIEW_SELECT;
				case 2:
					if (ControllerV2.loggedInUser == true) {
						return View2.REVIEW_WRITE;
					}
					break;
				case 0:
					return View2.DRAMA;
				
				default:
					break;

				}
			}
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 선택▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int selectReview() {
		Map<String, Object> row = null;
		System.out.print("조회할 리뷰 번호 입력>>");
		int input = ScanUtil.nextInt();
		row = dao.selectReview(input);

		if (row == null || row.size() == 0) {
			System.out.println("글이 존재하지 않습니다");
		} else {
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("▶ 글 번호: " + row.get("REVIEW_ID"));
			System.out.println("▶ 평점: " + row.get("REVIEW_SCORE"));
			System.out.println("▶ 리뷰 내용 ");
			System.out.println(row.get("REVIEW_CONTENT"));
		}

		while (true) {
			if (ControllerV2.loggedInUser == false) {
				System.out.println();
				System.out.print("아무 키나 입력시 리뷰게시판으로 돌아갑니다.");
				ScanUtil.nextLine();
				return View2.REVIEW;
			} else if (ControllerV2.loggedInUser == true) {
				String userID = (String) ControllerV2.userInfo.get("USER_ID");
				List<Map<String, Object>> myRow = dao.myReview(userID, (String) dramaService.selectedDrama);
				if ((myRow == null || myRow.size() == 0)
						|| !row.get("REVIEW_ID").equals(myRow.get(0).get("REVIEW_ID"))) {
					System.out.print("아무 키나 입력시 리뷰게시판으로 돌아갑니다.");
					ScanUtil.nextLine();
					return View2.REVIEW;
				} else if (row.get("REVIEW_ID").equals(myRow.get(0).get("REVIEW_ID"))) {
					System.out.println();
					System.out.println("┌──────────────────────────────────────────────────────────────────┐");
					System.out.println("│ 1. 내 리뷰 수정하기  2. 내 리뷰 삭제하기 3. 리뷰 홈으로 돌아가기 │");
					System.out.println("└──────────────────────────────────────────────────────────────────┘");
					System.out.print("입력 >>> ");
					switch (ScanUtil.nextInt()) {
					case 1:
						return View2.REVIEW_MODIFY;
					case 2:
						return View2.REVIEW_DELETE;
					case 3:
						return View2.REVIEW;
					case 0:
						return View2.REVIEW;
					default:
						break;
					}
				}

			}
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 작성▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// 예매내역, 리뷰 작성 여부 확인 후 리뷰 작성
	public int writeReview() {
		String userID = (String) ControllerV2.userInfo.get("USER_ID");
		String userNick = (String) ControllerV2.userInfo.get("USER_NICK");
		List<Map<String, Object>> myticket = dao.myTicketing(userID, (String) dramaService.selectedDrama);
		if (myticket == null || myticket.size() == 0) {
			System.out.println("(추가)예매내역이 없으므로 리뷰 작성 대상이 아닙니다.");
			System.out.println("아무 버튼이나 입력시 리뷰 창으로 이동합니다.");
			ScanUtil.nextLine();
			return View2.REVIEW;
		} else {
			System.out.print("예매내역이 존재하는 " + userNick + "님!\n");
			System.out.println(userNick + "님의 해당 극 예매 횟수는 " + myticket.size() + "번이며, 예매내역은 아래와 같습니다.");
			for (int i = 0; i < myticket.size(); i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String date = sdf.format(myticket.get(i).get("THEATER_DATE"));
				System.out.println();
				System.out.println(
						"▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				System.out.println();
				System.out.printf("%d. [예매번호] %s [공연일] %s\n", i + 1, myticket.get(i).get("TICKETING_ID"), date);
				System.out.println();
				System.out.println(
						"▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			}
			System.out.print("리뷰를 작성할 예매번호를 선택하세요>> ");
		}
		String ticketingID = (String) myticket.get(ScanUtil.nextInt() - 1).get("TICKETING_ID");

		System.out.println("──────────────────────────────────────────────────────────────────");
		List<Map<String, Object>> row2 = dao.checkReview(ticketingID);
		if (row2 == null || row2.size() == 0) {
			System.out.println("리뷰 작성 대상입니다.");
			System.out.print("리뷰 평점 입력(최하 1점 / 최대 5점) >>>");
			int reviewScore;
			do {
				reviewScore = ScanUtil.nextInt();
				if (reviewScore < 1 || reviewScore > 5)
					System.out.print("1~5점 사이의 평점이 아닙니다. 다시 입력하십시오>>");
			} while (reviewScore < 1 || reviewScore > 5);
			System.out.print("리뷰 내용 입력>>");
			String reviewContent = ScanUtil.nextLine();
			List<Object> param = new ArrayList<>();
			param.add(reviewScore);
			param.add(reviewContent);
			param.add(ticketingID);
			param.add((String) dramaService.selectedDrama);
			int result = dao.writeReview(param);
			System.out.println(result + "개 작성되었습니다.");
		} else {
			System.out.println("이미 작성된 리뷰가 있습니다.");
			System.out.println("리뷰번호" + row2.get(0).get("REVIEW_ID") + "번을 확인하십시오.");
			System.out.println("아무 키나 입력시 해당 리뷰 홈으로 돌아갑니다.");
			ScanUtil.nextLine();
		}
		System.out.println("──────────────────────────────────────────────────────────────────");
		System.out.println("");
		return View2.REVIEW;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 수정▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int modifyReview() {
		System.out.println();
		System.out.println("════════════════════════════════════════════════════════════════════════════");
		System.out.println();
		System.out.print("수정 리뷰 평점 입력(최하 1점 / 최대 5점) >>>");
		int reviewScore;
		do {
			reviewScore = ScanUtil.nextInt();
			if (reviewScore < 1 || reviewScore > 5)
				System.out.print("1~5점 사이의 평점이 아닙니다. 다시 입력하십시오>>");
		} while (reviewScore < 1 || reviewScore > 5);
		System.out.print("수정 리뷰 내용 입력 >>>");
		String reviewContent = ScanUtil.nextLine();
		System.out.println();
		System.out.println("════════════════════════════════════════════════════════════════════════════");
		String userID = (String) ControllerV2.userInfo.get("USER_ID");
		String drama = (String) dramaService.selectedDrama;
		int result = 0;
		List<Map<String, Object>> myRow = dao.myTicketing(userID, drama);
		for (int i = 0; i < myRow.size(); i++) {
			result = dao.modifyReview(reviewScore, reviewContent, (String) myRow.get(0).get("TICKETING_ID"));
			System.out.println(result);
		}

		if (result > 0) {
			System.out.println(result + "개가 수정되었습니다.");
		} else {
			System.out.println("수정에 실패했습니다.");
		}
		System.out.println("리뷰 게시판으로 돌아갑니다.");
		return View2.REVIEW;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬리뷰 삭제▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	public int deleteReview() {
		System.out.println("정말 삭제하시겠습니까?");
		System.out.println();
		System.out.println("┌───────────────────┐");
		System.out.println("│ 1. 예   2. 아니오 │");
		System.out.println("└───────────────────┘");
		System.out.println(" 선택 >>> ");
		switch (ScanUtil.nextInt()) {
		case 1:
			break;
		case 2:
			System.out.println("리뷰 게시판으로 돌아갑니다.");
			return View2.REVIEW;
		}
		String userID = (String) ControllerV2.userInfo.get("USER_ID");
		List<Map<String, Object>> myRow = dao.myReview(userID, (String) dramaService.selectedDrama);
		BigDecimal bdReviewId = (BigDecimal) myRow.get(0).get("REVIEW_ID");
		int reviewID = bdReviewId.intValue();
		int result = dao.deleteReview(reviewID);
		if (result > 0) {
			System.out.println("성공적으로 삭제되었습니다.");
		} else {
			System.out.println("삭제에 실패했습니다.");
		}
		System.out.println("리뷰 게시판으로 돌아갑니다.");
		return View2.REVIEW;
	}

}
