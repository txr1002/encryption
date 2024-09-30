package encryption;

import java.util.Scanner;

public class Algorithm {
	public static String subkey1;
	public static String subkey2;
	public static String ans;
	public static String[][] S1_box = new String[][] {
		{ "01", "00", "11", "10" }, { "11", "10", "01", "00" },
		{ "00", "10", "01", "11" }, { "11", "01", "00", "10" } };
	public static String[][] S2_box = new String[][] {
		{ "00", "01", "10", "11" }, { "10", "11", "01", "00" },
		{ "11", "00", "01", "10" }, { "10", "01", "00", "11" } };

	public static void main(String args[]) {
		getKey();
		//扩展功能
		System.out.println("输入明文（ASII编码字符串）：");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		for (char character : str.toCharArray()) {
            // 将字符转换为对应的ASCII码整数
            int asciiValue = (int) character;
            // 将ASCII码整数转换为二进制字符串
            String binaryString = String.format("%8s", Integer.toBinaryString(asciiValue)).replace(' ', '0');
            // 打印字符及其对应的二进制字符串
            //System.out.println("Character: " + character + " Binary: " + binaryString);
            fk(binaryString);
    		String miwen=IP(ans,2);
    		int asciiValue2 = Integer.parseInt(miwen, 2);
    		char asciiChar2 = (char) asciiValue2;
    		System.out.print(asciiChar2); 
        }
		
	}

	public static void getKey() {
		System.out.println("请输入10位密钥：");
		Scanner sc = new Scanner(System.in);
		String key = sc.nextLine();
		// System.out.println(key);
		// 第一步 置换
		char[] keyarray = key.toCharArray();
		// System.out.println(keyarray[3]);
		char[] replacement = new char[10];
		replacement[0] = keyarray[2];
		replacement[1] = keyarray[4];
		replacement[2] = keyarray[1];
		replacement[3] = keyarray[6];
		replacement[4] = keyarray[3];
		replacement[5] = keyarray[9];
		replacement[6] = keyarray[0];
		replacement[7] = keyarray[8];
		replacement[8] = keyarray[7];
		replacement[9] = keyarray[5];

		StringBuilder tmp1 = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			tmp1.append(replacement[i]);
		}
		String key1 = tmp1.toString();
		StringBuilder tmp2 = new StringBuilder();
		for (int i = 5; i < 10; i++) {
			tmp2.append(replacement[i]);
		}
		String key2 = tmp2.toString();
		String lkey1 = leftrotate(key1, 1);
		String lkey2 = leftrotate(key2, 1);
		// System.out.println(lkey1);
		// 压缩置换
		char[] lkey1array = lkey1.toCharArray();
		char[] lkey2array = lkey2.toCharArray();
		StringBuilder ltmp1 = new StringBuilder();
		ltmp1.append(lkey2array[0]);
		ltmp1.append(lkey1array[2]);
		ltmp1.append(lkey2array[1]);
		ltmp1.append(lkey1array[3]);
		ltmp1.append(lkey2array[2]);
		ltmp1.append(lkey1array[4]);
		ltmp1.append(lkey2array[4]);
		ltmp1.append(lkey2array[3]);
		subkey1 = ltmp1.toString();
		String llkey1 = leftrotate(key1, 2);
		String llkey2 = leftrotate(key2, 2);
		// System.out.println(llkey1);
		// 压缩置换
		char[] llkey1array = llkey1.toCharArray();
		char[] llkey2array = llkey2.toCharArray();
		StringBuilder lltmp1 = new StringBuilder();
		lltmp1.append(llkey2array[0]);
		lltmp1.append(llkey1array[2]);
		lltmp1.append(llkey2array[1]);
		lltmp1.append(llkey1array[3]);
		lltmp1.append(llkey2array[2]);
		lltmp1.append(llkey1array[4]);
		lltmp1.append(llkey2array[4]);
		lltmp1.append(llkey2array[3]);
		subkey2 = lltmp1.toString();
		// System.out.println(subkey1);
		//System.out.println("密钥2"+subkey2); //分别表示两个密钥值
	}

	public static String leftrotate(String str, int n) {
		return str.substring(n, str.length()) + str.substring(0, n);
	}

	// n=1代表初始置换，n=2表示最终置换
	public static String IP(String msg,int n) {
		
		char[] msgarray = msg.toCharArray();
		char[] replacement = new char[8];
		StringBuilder tmp = new StringBuilder();
		if (n == 1) {
			replacement[0] = msgarray[1];
			replacement[1] = msgarray[5];
			replacement[2] = msgarray[2];
			replacement[3] = msgarray[0];
			replacement[4] = msgarray[3];
			replacement[5] = msgarray[7];
			replacement[6] = msgarray[4];
			replacement[7] = msgarray[6];
		} else if (n == 2) {
			replacement[0] = msgarray[3];
			replacement[1] = msgarray[0];
			replacement[2] = msgarray[2];
			replacement[3] = msgarray[4];
			replacement[4] = msgarray[6];
			replacement[5] = msgarray[1];
			replacement[6] = msgarray[7];
			replacement[7] = msgarray[5];
		} else
			System.out.println("输入参数有误");
		for (int i = 0; i < 8; i++) {
			tmp.append(replacement[i]);
		}
		return tmp.toString();
		// 完成ip置换，在犹豫返回类型是void还是string
	}

	public static String xor(char c[],char key[]) {
	  int len=c.length;
	  char result[]=new char[len];
	  StringBuilder tmp = new StringBuilder();
	  for(int i=0;i<len;i++) {
		  if(c[i]==key[i]) result[i]='0';
		  else result[i]='1';
		  tmp.append(result[i]);
	  }
	  //System.out.print("reusult:");
	  //System.out.println(result);
	  return tmp.toString();
	 }
//1010000010 10011010

	public static void fk(String str) {
		//System.out.println("输入8位明文：");
		//Scanner sc = new Scanner(System.in);
		//String str = sc.nextLine();
		String msg = IP(str,1); // 传入置换后的明文
		//System.out.println("初始置换后的明文："+msg);
		// 轮函数，明文先分为左右两部分
		String l0 = msg.substring(0, 4);
		String r0 = msg.substring(4);
		// 右侧进行扩展，调用异或运算函数
		// System.out.print(r0);
		char l[] = l0.toCharArray();
		char r[] = r0.toCharArray();
		char r_extend[] = new char[8];
		r_extend[0] = r[3];
		r_extend[1] = r[0];
		r_extend[2] = r[1];
		r_extend[3] = r[2];
		r_extend[4] = r[1];
		r_extend[5] = r[2];
		r_extend[6] = r[3];
		r_extend[7] = r[0];
		char key[] = subkey1.toCharArray();
		String addkey=xor(r_extend, key);
		//System.out.println(key);
		//System.out.println(r_extend);
		//替换盒
		String S1 = addkey.substring(0, 4);
		String S2 = addkey.substring(4, 8);
		S1 = searchSbox(S1, 1);
		S2 = searchSbox(S2, 2);
		String SS = S1 + S2;
		//System.out.println(SS);
		//直接替换
		char s[]=SS.toCharArray();
		char ss[]=new char[4];
		ss[0]=s[1];
		ss[1]=s[3];
		ss[2]=s[2];
		ss[3]=s[0];
		//再次异或运算，然后左右交换
		String right=xor(l,ss);
		//System.out.println(right);
		String c=r0+right;
		//第一次fk+sw后输出结果
		//System.out.println("密文为："+c);
		
		
		char r2[]=right.toCharArray();
		char r_extend2[] = new char[8];
		r_extend2[0] = r2[3];
		r_extend2[1] = r2[0];
		r_extend2[2] = r2[1];
		r_extend2[3] = r2[2];
		r_extend2[4] = r2[1];
		r_extend2[5] = r2[2];
		r_extend2[6] = r2[3];
		r_extend2[7] = r2[0];
		//使用子密钥2
		char key2[] = subkey2.toCharArray();
		String addkey2=xor(r_extend2, key2);
		//System.out.println(key);
		//System.out.println(r_extend);
		//替换盒
		String S21 = addkey2.substring(0, 4);
		String S22 = addkey2.substring(4, 8);
		S21 = searchSbox(S21, 1);
		S22 = searchSbox(S22, 2);
		String SS2 = S21 + S22;
		//System.out.println(SS);
		//直接替换
		char s2[]=SS2.toCharArray();
		char ss2[]=new char[4];
		ss2[0]=s2[1];
		ss2[1]=s2[3];
		ss2[2]=s2[2];
		ss2[3]=s2[0];
		//最后和fw后结果的左侧值异或运算，没有左右交换！！
		String left2=xor(r,ss2);
		//拿fw后结果的右侧值不变
		ans=left2+right;
		//System.out.println("第二次fk结果:"+ans);
	}
	public static String searchSbox(String str, int n) { //S盒的查找
		StringBuilder sb = new StringBuilder();
		sb.append(str.charAt(0));
		sb.append(str.charAt(3));
		String ret = new String(sb);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(str.charAt(1));
		sb1.append(str.charAt(2));
		String ret1 = new String(sb1);
		String retu = new String();
		if (n == 1) {
			retu = S1_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
		} else {
			retu = S2_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
		}
		return retu;
	}
}
