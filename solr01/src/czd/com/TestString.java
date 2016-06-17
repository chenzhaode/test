package czd.com;

import org.junit.Test;

public class TestString {
	
	
	
	@Test
	public void test01(){		
		String str="I am asp.net programmer";
		String[] ss=str.split(" ");
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<ss.length;i++){
			sb.append(ss[i]).append((int)Math.pow(2, i)).append(" ");
		}
		System.out.println(sb);		
	}
	
	
	
	

}
