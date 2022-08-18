package service;

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
   
   
   private DramaService(){};
   
   
   public static DramaService getInstance() {
      if(ds == null) ds = new DramaService(); return ds;
   }
   
   
   DramaDAO dd = DramaDAO.getInstance();
   Object selectedDrama = null;
   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
   
   
   public  int showDramaList(boolean loggedInUser) {   
      
      /*
       *  case  1 :   상영 끝난 것 , 상영중 , 상영 예정 
       */
      
      List<Map<String,Object>> eDrama = dd.showEDrama();  //end drama
      List<Map<String,Object>> nDrama = dd.showNDrama();  // now playing drama
      List<Map<String,Object>> uDrama = dd.showUDrama();  // upcoming drama
      
      List<Map<String,Object>> Dlist = new ArrayList<Map<String,Object>>(); 
      
      int count = 0;
      System.out.println(); //ControllerV2.clearScreen();
      System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
      System.out.println(" ღ  지난 공연 ღ ");
      System.out.println();
      if(eDrama == null) {
         System.out.println("없음");
      }
      else {
         for(Map<String, Object> item : eDrama) {
            System.out.println(++count+" "+item.get("THEATER_TITLE"));
            Dlist.add(item);
         }
         
      }
      System.out.println("┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
      System.out.println(" ღ  공연 중 ღ ");
      System.out.println();
      if(nDrama == null) {
         System.out.println("없음");
      }else {
         
         for(Map<String, Object> item : nDrama) {
            System.out.println(++count+" " +item.get("THEATER_TITLE") );
            Dlist.add(item);
         }
      }
      System.out.println("┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
      System.out.println(" ღ  공연 예정 ღ ");
      System.out.println();
      if(uDrama == null) {
         System.out.println("없음");
      }else {
         
         for(Map<String, Object> item : uDrama) {
            System.out.println(++count+" " + item.get("THEATER_TITLE"));
            Dlist.add(item);
         }
      }
      
      
      System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
      System.out.print("조회할 연극 번호를 입력하세요.(0.뒤로 가기) >>"); // 상세보기 안에 리뷰보기 포함 
      try {
          int choose = ScanUtil.nextInt();
          if (choose == 0) {
			return View2.HOME;
		}
          selectedDrama = Dlist.get(choose-1).get("THEATER_TITLE");
          
       }catch(Exception e) {
          System.out.println("잘못된 입력입니다.");
          return View2.DRAMA;
       }
      return View2.DRAMA_INFO;

   
      
   }

   
   public int dramaTicketing() {
      
      
	   System.out.println(); //ControllerV2.clearScreen();
	   System.out.println("▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");
	   System.out.println("[예매 가능 연극]");
	   System.out.println();
      int count = 0;
      
      List<Map<String, Object>> nDrama = dd.showNDrama(selectedDrama); // 상영중인 극
      if(nDrama == null) {
         System.out.println("해당 극이 없습니다. ");
         return View2.HOME;
      }
      else {
         
         for(Map<String, Object> item : nDrama ) {
        	 System.out.print(SpaceUtil.format((++count), 3, false));
          	System.out.print(SpaceUtil.format(item.get("THEATER_TITLE"), 20, false));
          	System.out.print(SpaceUtil.format("공연 날짜 : " + sdf.format(item.get("THEATER_DATE")), 25, false));
          	System.out.print(SpaceUtil.format("공연 시간 : " + item.get("THEATER_TIME1") + "~" + item.get("THEATER_TIME2"), 30, false));
          	System.out.print(SpaceUtil.format("가격 : " + item.get("THEATER_PRICE") , 20, false));
          	System.out.println(SpaceUtil.format("잔여좌석 : " + item.get("THEATER_LSEAT") + " / 25", 10, false));
//             System.out.println( (++count) + " " +item.get("THEATER_TITLE")+ "| 상영날: " + sdf.format(item.get("THEATER_DATE")) + "|상영 시간: " + item.get("THEATER_TIME1") + "~" 
//                   + item.get("THEATER_TIME2") + "  가격: " + item.get("THEATER_PRICE") + " 좌석수 :" + item.get("THEATER_LSEAT") + " / 25");
             
         }
      }

      System.out.println("▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰▰");
      System.out.print("선택 >>> ");
      int choose = ScanUtil.nextInt();
      System.out.print("구매 수량 >>> ");
      int qty = ScanUtil.nextInt();
      if(qty > Integer.parseInt(String.valueOf(nDrama.get(choose-1).get("THEATER_LSEAT")))){
         System.out.println("최대 구매 수량을 초과했습니다!");
         return View2.DRAMA;
      }
      System.out.print("구매를 위해 비밀번호를 입력하세요 >>> ");
      String pass = ScanUtil.nextLine();
      
      if(pass.equals(ControllerV2.userInfo.get("USER_PW"))) {
         List<Object> param2 = new ArrayList<Object>();
         param2.add(nDrama.get(choose-1).get("THEATER_ID"));
         //ControllerV2.clearScreen();
         System.out.println("═══════════════ 명세서 ═══════════════");
         System.out.println("극이름 : " + nDrama.get(choose-1).get("THEATER_TITLE"));
         System.out.println("극 상영날 :" + sdf.format(nDrama.get(choose-1).get("THEATER_DATE")));
         System.out.println("예매 수 : " + qty );
         System.out.println("총 금액 : " + qty * Integer.parseInt(String.valueOf(nDrama.get(choose-1).get("THEATER_PRICE"))));
         System.out.println("══════════════════════════════════════");
         System.out.println("┌───────────────────┐");
         System.out.println("│ 1. 구매   2. 취소 │");
         System.out.println("└───────────────────┘");
         System.out.println("선택 >>> ");
         
         List<Object> param = new ArrayList<>();
         param.add(qty);
         param.add(ControllerV2.userInfo.get("USER_ID"));
         
         switch(ScanUtil.nextInt()) {
         case 1: int result = dd.ticketing(param); 
               if(result >0) System.out.println("구매가 완료되었습니다.");
               dd.ticket(param,param2);
               return View2.HOME;
         case 2:  return View2.HOME; 
         default : return View2.DRAMA;
         }
      }
      else {
         System.out.println("비밀번호 틀렸습니다.");
         return View2.DRAMA;
      }
      
      
   }
   
   

   public int showDramaInfo() {
	  System.out.println("•─────────────────── 연극 정보 ───────────────────•");
      List<Map<String,Object>> row = new ArrayList<Map<String,Object>>();
      row = dd.getDrama(selectedDrama);
      
      if(Integer.parseInt(String.valueOf(row.get(0).get("THEATER_START"))) - Integer.parseInt(sdf.format(new Date()) ) > 0) { // 상영예정 
         System.out.println("[상영 예정]");
      }
      else if(Integer.parseInt(String.valueOf(row.get(0).get("THEATER_END"))) - Integer.parseInt(sdf.format(new Date()) ) < 0) {
         // 상영종료 
         System.out.println("[상영 종료]");
      }
      else {
         System.out.println("[상영중]");
      }
      System.out.println("⸙"+selectedDrama+"⸙");
      System.out.println(row.get(0).get("THEATER_CONTENT"));
      //[{THEATER_CONTENT=이 노래를 만들고 부르면 다시 봄이 올 거예요., THEATER_END=20220722, THEATER_TITLE=하데스, THEATER_START=20220720}]
      System.out.println("공연기간 : " +row.get(0).get("THEATER_START")+ " ~ " +row.get(0).get("THEATER_END"));
      System.out.println("티켓 가격 : " + row.get(0).get("THEATER_PRICE"));
      System.out.println("•─────────────────────────────────────────────────•");
      System.out.println();
      
      if(Integer.parseInt(String.valueOf(row.get(0).get("THEATER_START"))) - Integer.parseInt(sdf.format(new Date()) ) > 0) { // 상영예정 
         System.out.println("1.리뷰 자세히 보기  0.홈으로 돌아가기");
         switch(ScanUtil.nextInt()) {
         case 1: return View2.REVIEW;
         case 0: selectedDrama = null; return View2.HOME;
         default : return View2.HOME;
         }
      }
      else if(Integer.parseInt(String.valueOf(row.get(0).get("THEATER_END"))) - Integer.parseInt(sdf.format(new Date()) ) < 0) {
         // 상영종료 
    	  System.out.println("┌────────────────────────────────┐");
      	 System.out.println("│ 1.리뷰 보기  0.홈으로 돌아가기 │");
      	 System.out.println("└────────────────────────────────┘");
      	 System.out.print("선택 >>> ");
         switch(ScanUtil.nextInt()) {
         case 1: return View2.REVIEW;
         case 0: selectedDrama = null; return View2.HOME;
         default : return View2.HOME;
         }
      }
      else {
    	  System.out.println("┌──────────────────────────────────────────┐");
          System.out.println("│ 1.리뷰 보기 2.예매하기 0.홈으로 돌아가기 │");
          System.out.println("└──────────────────────────────────────────┘");
          System.out.print("선택 >>> ");
         switch(ScanUtil.nextInt()) {
         case 1: return View2.REVIEW;
         case 2: if(ControllerV2.userInfo== null) {
            System.out.println("로그인후 이용 가능합니다."); return View2.HOME;
         }
         else {
            return View2.DRAMA_TICKETTING;
         }
         case 3: selectedDrama = null; return View2.HOME;
         default : return View2.HOME;
         }
      }
      
   }


   public int dramaReview() {
      // TODO Auto-generated method stub
      return 0;
   }


   public int ticketRefund() {
      System.out.println("환불할 예매정보를 입력하세요");
      return 0;
   }

   
}
