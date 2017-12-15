import java.io.File;
import java.io.IOException;

import hluwa.arsc.ARSCParser;
import hluwa.arsc.format.ArscFile;
import hluwa.dex.format.DexFile;
import hluwa.dex.format.insns_item;
import hluwa.dex.DexChanger;
import hluwa.dex.type.TypeCast;
import hluwa.dex.type.uLeb128;

public class GreenCreator {
	public static void main(String args[]) throws IOException 
	{
        System.out.println(new TypeCast(new byte[]{0x1c}).toLeb128().getData());
        System.exit(0);
		String path = "/Users/hluwa/Downloads/com.fujicubesoft.ManyBricksBreaker/classes.dex";
		String arscPath = "/Users/hluwa/Downloads/com.fujicubesoft.ManyBricksBreaker/resources.arsc";
		ArscFile arscFile = ARSCParser.ParseArsc(new File(arscPath));
		System.out.println(arscFile);
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
				if(mtd.indexOf("loadAd") != -1) {
					System.out.println(mtd);
				}
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
