package hluwa.dex.format;

import hluwa.dex.base.Pool;

public class MapList {
	private int size;
	public Pool<Map_Item> map_list = new Pool<Map_Item>();

	public Map_Item get(int index) {
		return map_list.get(index);
	}

	public void set(int index, Map_Item item) {
		map_list.set(index, item);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public MapList(int size) {
		this.size = size;
	}

	public MapList() {

	}

}
