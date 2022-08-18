package util;

public class SpaceUtil {
	
	public static String format(Object o, int length, boolean right) {
		int spaceCount = length;
		String sO = o.toString();
		String result = "";
		for(int i = 0; i < sO.length(); i++) {
			if(sO.charAt(i) >= 'ㄱ' && sO.charAt(i) <= '힣') {
				spaceCount -= 2;
			}else {
				spaceCount--;
			}
		}
		
		for(int i = 0; i < spaceCount; i++) {
			result += " ";
		}if(right) {
			result += sO;
		}else {
			result = sO + result;
		}
		return result;
	} 
}

// System.out.print(SpacrUtil.format("안녕하세요",30));