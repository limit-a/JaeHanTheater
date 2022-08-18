package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.ControllerV2;
import dao.DramaDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View2;

public class DramaService {

	private static DramaService ds = null;

	private DramaService() {
	};

	public static DramaService getInstance() {
		if (ds == null)
			ds = new DramaService();
		return ds;
	}

	DramaDAO dd = DramaDAO.getInstance();
	Object selectedDrama = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public int showDramaList(boolean loggedInUser) {

		/*
		 * case 1 : 상영 끝난 것 , 상영중 , 상영 예정
		 */

		List<Map<String, Object>> eDrama = dd.showEDrama(); // end drama
		List<Map<String, Object>> nDrama = dd.showNDrama(); // now playing drama
		List<Map<String, Object>> uDrama = dd.showUDrama(); // upcoming drama

		List<Map<String, Object>> Dlist = new ArrayList<Map<String, Object>>();

		int count = 0;
		System.out.println(); // ControllerV2.clearScreen();
		// 시간 기준으로 지나간 연극의 목록을 보여주기
		System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		System.out.println(" ღ  지난 공연 ღ ");
		System.out.println();
		if (eDrama == null)
			System.out.println("없음");
		else {
			for (Map<String, Object> item : eDrama) {
				System.out.println(++count + " " + item.get("THEATER_TITLE"));
				Dlist.add(item);
			}

		}
		// 시간 기준으로 현재 볼 수 있는 연극 목록을 보여주기
		System.out.println("┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.println(" ღ  공연 중 ღ ");
		System.out.println();
		if (nDrama == null)
			System.out.println("없음");
		else {
			for (Map<String, Object> item : nDrama) {
				System.out.println(++count + " " + item.get("THEATER_TITLE"));
				Dlist.add(item);
			}
		}
		// 시간 기준으로 앞으로 할 공연 목록을 보여주기
		System.out.println("┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.println(" ღ  공연 예정 ღ ");
		System.out.println();
		if (uDrama == null)
			System.out.println("없음");
		else {
			for (Map<String, Object> item : uDrama) {
				System.out.println(++count + " " + item.get("THEATER_TITLE"));
				Dlist.add(item);
			}
		}

		System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		// 상세보기 안에 리뷰보기 포함
		System.out.print("조회할 연극 번호를 입력하세요.(0.뒤로 가기) >>");
		try {
			int choose = ScanUtil.nextInt();
			if (choose == 0) {
				return View2.HOME;
			}
			selectedDrama = Dlist.get(choose - 1).get("THEATER_TITLE");

		} catch (Exception e) {
			System.out.println("잘못된 입력입니다.");
			return View2.DRAMA;
		}
		return View2.DRAMA_INFO;

	}

	public int dramaTicketing() {

		System.out.println(); // ControllerV2.clearScreen();
		System.out.println(
				"▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");
		System.out.println("[예매 가능 연극]");
		System.out.println();
		int count = 0;

		List<Map<String, Object>> nDrama = dd.showNDrama(selectedDrama); // 상영중인
																			// 극
		if (nDrama == null) {
			System.out.println("해당 극이 없습니다. ");
			return View2.HOME;
		} else {

			for (Map<String, Object> item : nDrama) {
				System.out.print(SpaceUtil.format((++count), 3, false));
				System.out.print(
						SpaceUtil.format(item.get("THEATER_TITLE"), 20, false));
				System.out.print(SpaceUtil.format(
						"공연 날짜 : " + sdf.format(item.get("THEATER_DATE")), 25,
						false));
				System.out
						.print(SpaceUtil.format(
								"공연 시간 : " + item.get("THEATER_TIME1") + "~"
										+ item.get("THEATER_TIME2"),
								30, false));
				System.out.print(SpaceUtil.format(
						"가격 : " + item.get("THEATER_PRICE"), 20, false));
				System.out.println(SpaceUtil.format(
						"잔여좌석 : " + item.get("THEATER_LSEAT") + " / 24", 10,
						false));
//             System.out.println( (++count) + " " +item.get("THEATER_TITLE")+ "| 상영날: " + sdf.format(item.get("THEATER_DATE")) + "|상영 시간: " + item.get("THEATER_TIME1") + "~" 
//                   + item.get("THEATER_TIME2") + "  가격: " + item.get("THEATER_PRICE") + " 좌석수 :" + item.get("THEATER_LSEAT") + " / 25");

			}
		}

		System.out.println(
				"▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");
		System.out.print("선택 >>> ");
		int choose = ScanUtil.nextInt();
		if (choose > nDrama.size() || choose < 1) {
			System.out.println("해당 극없음");
			return View2.DRAMA_TICKETTING;
		}
		System.out.print("예매 수량 >>> ");
		int qty = ScanUtil.nextInt();
		if (qty > Integer.parseInt(
				String.valueOf(nDrama.get(choose - 1).get("THEATER_LSEAT")))) {
			System.out.println("최대 예매 수량을 초과했습니다!");
			return View2.DRAMA;
		}
		System.out.print("예매를 위해 비밀번호를 입력하세요 >>> ");
		String pass = ScanUtil.nextLine();

		if (pass.equals(ControllerV2.userInfo.get("USER_PW"))) {
			List<Object> param2 = new ArrayList<Object>();
			param2.add(nDrama.get(choose - 1).get("THEATER_ID"));
			// ControllerV2.clearScreen();
			System.out.println("═══════════════ 명세서 ═══════════════");
			System.out.println(
					"극이름 : " + nDrama.get(choose - 1).get("THEATER_TITLE"));
			System.out.println("극 상영날 :"
					+ sdf.format(nDrama.get(choose - 1).get("THEATER_DATE")));
			System.out.println("예매 수 : " + qty);
			System.out.println("총 금액 : " + qty * Integer.parseInt(String
					.valueOf(nDrama.get(choose - 1).get("THEATER_PRICE"))));
			System.out.println("══════════════════════════════════════");
			System.out.println("┌───────────────────┐");
			System.out.println("│ 1. 예매   2. 취소 │");
			System.out.println("└───────────────────┘");
			System.out.print("선택 >>> ");

			List<Object> param = new ArrayList<>();
			param.add(qty);
			param.add(ControllerV2.userInfo.get("USER_ID"));

			switch (ScanUtil.nextInt()) {
			case 1:
				int result = dd.ticketing(param);
				if (result > 0) {
					dd.ticket(param, param2);
					System.out.println("예매가 완료되었습니다.");
				} else {
					System.out.println("예매를 실패하였습니다.");
				}
				return View2.HOME;
			case 2:
				return View2.HOME;
			default:
				return View2.DRAMA;
			}
		} else {
			System.out.println("비밀번호가 틀렸습니다.");
			return View2.DRAMA;
		}

	}

	public int showDramaInfo() {

		List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();

		row = dd.getDrama(selectedDrama);

		Date theaterStart = null;
		Date theaterEnd = null;

		try {
			theaterStart = sdf.parse((String) row.get(0).get("THEATER_START"));
			theaterEnd = sdf.parse((String) row.get(0).get("THEATER_END"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 시간을 기준으로 연극 날짜와 비교하여 공연종료, 공연중, 공연예정 구분
		System.out.println("•─────────────────── 연극 정보 ───────────────────•");
		if (new Date().after(theaterEnd)) {
			System.out.println("[공연 종료]");
		} else if (new Date().before(theaterStart)) {
			System.out.println("[공연 예정]");
		} else {
			System.out.println("[공연중]");
		}

		System.out.println("⸙" + selectedDrama + "⸙");
		System.out.println(row.get(0).get("THEATER_CONTENT"));
		System.out.println("공연기간 : " + row.get(0).get("THEATER_START") + " ~ "
				+ row.get(0).get("THEATER_END"));
		System.out.println("티켓 가격 : " + row.get(0).get("THEATER_PRICE"));
		System.out
				.println("•─────────────────────────────────────────────────•");
		System.out.println();

		if (new Date().equals(theaterEnd) || new Date().after(theaterEnd)) {
			System.out.println("┌───────────────────────────────┐");
			System.out.println("│ 1.리뷰 보기 0.홈으로 돌아가기 │");
			System.out.println("└───────────────────────────────┘");
		} else if (new Date().before(theaterStart)) {
			System.out.println("┌───────────────────┐");
			System.out.println("│ 0.홈으로 돌아가기 │");
			System.out.println("└───────────────────┘");
		} else {
			System.out.println("┌──────────────────────────────────────────┐");
			System.out.println("│ 1.리뷰 보기 2.예매하기 0.홈으로 돌아가기 │");
			System.out.println("└──────────────────────────────────────────┘");
		}
		while (true) {
			System.out.print("선택 >>> ");
			switch (ScanUtil.nextInt()) {
			case 1:
				if (new Date().equals(theaterStart)
						|| new Date().after(theaterStart)) {
					return View2.REVIEW;
				}
				break;
			case 2:
				if (new Date().equals(theaterStart)
						|| (new Date().after(theaterStart)
								&& new Date().before(theaterEnd))) {
					if (ControllerV2.userInfo == null) {
						System.out.println("로그인후 이용 가능합니다.");
						return View2.HOME;
					} else {
						return View2.DRAMA_TICKETTING;
					}
				}
				break;
			case 0:
				selectedDrama = null;
				return View2.HOME;

			default:
				break;
			}
		}

//		if (Integer.parseInt(String.valueOf(row.get(0).get("THEATER_START")))
//				- Integer.parseInt(sdf.format(new Date())) > 0) { // 상영예정
//			System.out.println("1.리뷰 자세히 보기  0.홈으로 돌아가기");
//			switch (ScanUtil.nextInt()) {
//			case 1:
//				return View2.REVIEW;
//			case 0:
//				selectedDrama = null;
//				return View2.HOME;
//			default:
//				return View2.HOME;
//			}
//		} else if (Integer
//				.parseInt(String.valueOf(row.get(0).get("THEATER_END")))
//				- Integer.parseInt(sdf.format(new Date())) < 0) {
//			// 상영종료
//			System.out.println("┌────────────────────────────────┐");
//			System.out.println("│ 1.리뷰 보기  0.홈으로 돌아가기 │");
//			System.out.println("└────────────────────────────────┘");
//			System.out.print("선택 >>> ");
//			switch (ScanUtil.nextInt()) {
//			case 1:
//				return View2.REVIEW;
//			case 0:
//				selectedDrama = null;
//				return View2.HOME;
//			default:
//				return View2.HOME;
//			}
//		} else {
//			System.out.println("┌──────────────────────────────────────────┐");
//			System.out.println("│ 1.리뷰 보기 2.예매하기 0.홈으로 돌아가기 │");
//			System.out.println("└──────────────────────────────────────────┘");
//			System.out.print("선택 >>> ");
//			switch (ScanUtil.nextInt()) {
//			case 1:
//				return View2.REVIEW;
//			case 2:
//				if (ControllerV2.userInfo == null) {
//					System.out.println("로그인후 이용 가능합니다.");
//					return View2.HOME;
//				} else {
//					return View2.DRAMA_TICKETTING;
//				}
//			case 0:
//				selectedDrama = null;
//				return View2.HOME;
//			default:
//				return View2.HOME;
//			}
//		}

	}

}
