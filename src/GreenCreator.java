import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import huluwa.dexparser.Exception.CursorMoveException;
import huluwa.dexparser.Exception.NonDexFileException;
import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.format.Code_Item;
import huluwa.dexparser.format.DexFile;
import huluwa.dexparser.format.Method_Id_Item;
import huluwa.dexparser.format.encoded_method;
import huluwa.dexparser.format.insns_item;
import huluwa.dexparser.tool.ByteCursor;
import huluwa.dexparser.tool.DexChanger;
import huluwa.dexparser.type.TypeCast;

public class GreenCreator {
	public static void main(String args[])
			throws NonSameItemLengthException, IOException, NonDexFileException, QueryNextDataException,
			CursorMoveException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NonStandardLeb128Exception {

		String path = "C:\\Users\\huluwa\\Desktop\\classes2.dex";
		DexChanger changer = new DexChanger(new File(path));
		DexFile dexfile = changer.getDexFile();
		String magiclist[] = {
				"com.google.android.gms.ads.AdView.loadAd",
				"com.google.android.gms.ads.InterstitialAd.loadAd",
				"com.google.android.gms.ads.reward.RewardedVideoAd.loadAd",
				"com.mopub.mobileads.AdViewController.loadAd",
				"com.mopub.mobileads.MoPubInterstitial$MoPubInterstitialView.loadAd"
		};
		for (insns_item insns : dexfile.getAllInsnsItem()) {
			if (insns.opcode.toString().startsWith("INVOKE")) {

				changer.move(insns.getFileOff() + 2); // invoke系列指令格式 A|G|op BBBB F|E|D|C ,所以off + 2是methodId
				int methodId = changer.nextShort() & 0xFFFF; // 转为无符号数

				if (methodId < 0 || methodId > dexfile.getHeader().method_ids_size) { // invoke-custom
					continue;// 调用的索引有可能是FFFFFE,防止其他意外情况,过滤掉非正常methodId
				}
				String mtd = dexfile.getNameByMethodId(methodId);
//				if(mtd.indexOf("loadAd") != -1) {
//					System.out.println(mtd);
//				}
				for(String magic : magiclist) {
					if(mtd.indexOf(magic) != -1) {
						changer.setNop(insns);
						System.out.println(insns.getFileOff() + " - invoke method " + mtd);
					}
				}
			}
		}
		changer.flush();
	}
	
//	测试无效.. 
//	public static void addMyToast(String info,DexChanger changer) throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
//		changer.changeString(6116, info);
//		short makeText = changer.getDexFile().findMethod("android.widget.Toast.makeText(LLLI)");
//		short show = changer.getDexFile().findMethod("android.widget.Toast.show(V)");
//		if(makeText == -1 & show == -1) {
//			System.out.println("很抱歉无法添加Toast,因为找不到这个方法,请手动添加。");
//		}
//		byte hexData[] = { 0x1A, 0x00, 0, 0, 0x12, 0x11, 0x71, 0x30, 0, 0, 0x0C, 0x01, 0x0C, 0x00, 0x6E, 0x10,0, 0, 0x00, 0x00};
//		System.arraycopy(new TypeCast(makeText).toBytes(), 0, hexData, 8, 2);
//		System.arraycopy(new TypeCast(show).toBytes(), 0, hexData, 16, 2);
//		for(encoded_method method : changer.getDexFile().getAllEncodedMethod()) {
//			String mtdName = changer.getDexFile().getNameByMethodId(method.real_id);
//			if("com.dv.adm.Main.onCreate(VL)".equals(mtdName)) {
//				//System.out.println(method.getFileOff());
//				Code_Item code = method.code;
//				int len = hexData.length + code.insns.length;
//				byte insns[] = new byte[len];
//				code.insns_size = insns.length / 2;
//				System.arraycopy(hexData, 0, insns, 0, hexData.length);
//				System.arraycopy(code.insns, 0, insns, hexData.length, code.insns.length);
//				code.insns = insns;
//				changer.ChangeEncodedMethodCode(method, code);
//			}
//		}
//	}
}
