package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.ControllerV2;
import dao.BoardDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View2;

public class BoardService {

	private static BoardService instance = null;

	private BoardService() {
	}

	public static BoardService getInstance() {
		if (instance == null) {
			instance = new BoardService();
		}
		return instance;
	}

	BoardDAO dao = BoardDAO.getInstance();

	int selectedBoardId = -1;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public int showBoard() {

		List<Map<String, Object>> rows = dao.showBoard();

		System.out.println("▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰ 게시판 리스트 ▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");
		System.out.print(SpaceUtil.format("번호", 8, false));
		System.out.print(SpaceUtil.format("날짜", 16, false));
		System.out.print(SpaceUtil.format("제목", 30, false));
		System.out.println(SpaceUtil.format("작성자", 10, false));
		// System.out.printf("%s\t%s\t\t%-35s\t%s\n", "번호","날짜","제목","작성자");
		System.out.println("─────────────────────────────────────────────────────────────────────────");
		if (rows == null || rows.size() == 0) {
		} else {
			for (Map<String, Object> item : rows) {
				System.out.print(SpaceUtil.format(item.get("BOARD_ID"), 8, false));
				System.out.print(SpaceUtil.format(sdf.format(item.get("BOARD_DATE")), 16, false));
				System.out.print(SpaceUtil.format(item.get("BOARD_TITLE"), 30, false));
				System.out.println(SpaceUtil.format(item.get("BOARD_NICK"), 10, false));
				//System.out.printf("%s\t%tF\t%-30s\t%s\n", item.get("BOARD_ID"), item.get("BOARD_DATE"),
				//		item.get("BOARD_TITLE"), item.get("BOARD_NICK"));
			}
		}

		System.out.println("▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");

		while (true) {
			System.out.println("┌──────────────────────────────────────────────────┐");
			System.out.println("│  1. 게시판 선택 2. 게시판 작성 0. 게시판 나가기  │");
			System.out.println("└──────────────────────────────────────────────────┘");
			System.out.print("입력 >>> ");

			switch (ScanUtil.nextInt()) {
			case 1:
				return View2.BOARD_SELECT;
			case 2:
				return View2.BOARD_WRITE;
			case 0:
				return View2.HOME;

			default:
				break;
			}
		}

	}

	public int selectBoard() {
		Map<String, Object> row = null;
		int input = -1;
		if (selectedBoardId > 0) {
			row = dao.selectBoard(selectedBoardId);
		} else {

			System.out.print("게시판 번호 입력 >>> ");
			input = ScanUtil.nextInt();
			row = dao.selectBoard(input);
		}

		if (row == null || row.size() == 0) {
			System.out.println("글이 존재하지 않습니다");
			return View2.BOARD;
		} else {
			selectedBoardId = input;

			System.out.println();
			System.out.println("•─────────────────────────────────────────────•");
			System.out.println("  " + row.get("BOARD_TITLE") + "\t" + "작성자 : " + row.get("BOARD_NICK"));
			System.out.println();
			System.out.println("  " + row.get("BOARD_WRITE"));
			System.out.println();
			System.out.println("  " + sdf.format(row.get("BOARD_DATE")));
			System.out.println("•─────────────────────────────────────────────•");

			while (true) {
				
				System.out.println();
				System.out.println("┌──────────────────────────────────────────┐");
				System.out.println("│  1.게시판 수정  2.게시판 삭제  0.나가기  │");
				System.out.println("└──────────────────────────────────────────┘");
				System.out.print("입력 >>> ");
				switch (ScanUtil.nextInt()) {
				case 1:
					if (checkUser()) {
						return View2.BOARD_MODIFY;

					}
					break;
				case 2:
					if (checkUser()) {
						return View2.BOARD_DELETE;
					}
					break;
				case 0:
					selectedBoardId = -1;
					return ControllerV2.pageStatus ? View2.USER_BOARD
							: View2.BOARD;

				default:
					break;
				}
			}
		}
	}

	public int writeBoard() {
		Object nickName = null;
		Object password = null;

		System.out.print("게시판 제목 >>> ");
		String title = ScanUtil.nextLine();
		System.out.print("게시판 내용 >>> ");
		String content = ScanUtil.nextLine();

		if (ControllerV2.loggedInUser == false) {
			System.out.print("게시판 닉네임 >>> ");
			nickName = ScanUtil.nextLine();
			System.out.print("게시판 비밀번호 >>> ");
			password = ScanUtil.nextLine();
		} else {
			nickName = ControllerV2.userInfo.get("USER_NICK");
		}
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(content);
		param.add(nickName);
		param.add(password);
		int result = dao.writeBoard(param);
		System.out.println(result + "개 작성됨");
		return View2.BOARD;
	}

	public boolean checkUser() {
		if (ControllerV2.loggedInUser == false) {
			System.out.print("비밀번호를 입력하세요 >>> ");
			String pw = ScanUtil.nextLine();
			Map<String, Object> row = dao.selectBoardPw(selectedBoardId);
			if (!pw.equals(row.get("BOARD_PW"))) {
				System.out.println("비밀번호가 틀렸습니다.");
				return false;
			}
		} else {
			Object nick1 = ControllerV2.userInfo.get("USER_NICK");
			Map<String, Object> nick2 = dao.selectBoardNick(selectedBoardId);
			if (!nick1.equals(nick2.get("BOARD_NICK"))) {
				System.out.println("잘못된 접근입니다.");
				return false;
			}
		}
		return true;
	}

	public int modifyBoard(boolean pageStatus) {
		
		System.out.println("┌──────────────────────────┐");
		System.out.println("│ 1.제목 수정  2.내용 수정 │");
		System.out.println("└──────────────────────────┘");
		System.out.print("번호를 입력하세요 >>> ");
		int num = ScanUtil.nextInt();
		System.out.print("수정 입력 >>> ");
		String value = ScanUtil.nextLine();
		int result = dao.modifyBoard(num, value, selectedBoardId);
		if (result > 0) {
			System.out.println(result + "개가 수정되었습니다.");
			selectedBoardId = -1;
			return pageStatus ? View2.USER_BOARD : View2.BOARD;
		} else {
			System.out.println("수정 실패");
			return View2.BOARD_SELECT;
		}
	}

	public int deleteBoard(boolean pageStatus) {
		int result = dao.deleteBoard(selectedBoardId);
		if (result > 0) {
			System.out.println(result + "개가 삭제되었습니다.");
			selectedBoardId = -1;
			return pageStatus ? View2.USER_BOARD : View2.BOARD;
		} else {
			System.out.println("삭제 실패");
			return View2.BOARD_SELECT;
		}
	}
}
