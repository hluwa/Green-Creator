package hluwa.dex.base;

import java.util.ArrayList;

import hluwa.dex.base.Item;

@SuppressWarnings("serial")
public class Pool<T extends Item> extends ArrayList<T> {
	private int startOff;
	private int endOff;
	private int allLength;
	public Class<? extends Item> itemType;

	public Pool() {
		super();
	}

	public Pool(Class<? extends Item> cls) {
		this();
		itemType = cls;
	}

	public int getStartOff() {
		return startOff;
	}

	public void setStartOff(int startOff) {
		this.startOff = startOff;
	}

	public int getEndOff() {
		return endOff;
	}

	public void setEndOff(int endOff) {
		this.endOff = endOff;
	}

	public int getAllLength() {
		return allLength;
	}

	public void setAllLength(int allLength) {
		this.allLength = allLength;
	}
}
