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
	public static void main(String args[]) throws IOException 
	{

		String path = "C:\\Users\\huluwa\\Desktop\\classes.dex";
		DexChanger changer = new DexChanger(new File(path));
		DexFile dexfile = changer.getDexFile();
		String magiclist[] = {
				"com.google.android.gms.ads.AdView.loadAd",
				"com.google.android.gms.ads.InterstitialAd.loadAd",
				"com.google.android.gms.ads.reward.RewardedVideoAd.loadAd",
				"com.google.android.gms.ads.NativeExpressAdView.loadAd",
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
	
}
