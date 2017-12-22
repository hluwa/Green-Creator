package hluwa.dex;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import hluwa.common.ByteCursor;
import hluwa.dex.base.Item;
import hluwa.dex.base.Pool;
import hluwa.dex.format.*;

public class DexParser {
//	private ByteCursor cursor;
//	private DexFile dexFile;
//
//	public DexParser(File dexfile){
//		this(ParseDex(dexfile));
//	}
//
//	public DexParser(DexFile dexfile) {
//		this.dexFile = dexfile;
//		InputStream in;
//		try {
//			in = new FileInputStream(dexfile.getFile());
//			byte[] data = new byte[in.available()];
//			in.read(data);
//			in.close();
//			this.cursor = new ByteCursor(data);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static DexFile ParseDex(byte data[]) {
		return ParseDex(new ByteCursor(data));
	}

	public static DexFile ParseDex(ByteCursor cursor) {
		if(!DexFile.isDexFile(cursor.getData()))
		{
			System.out.println("[Error] This file is not DexFile ");
			return null;
		}
		DexFile dexFile = new DexFile();
		DexHeader header = ParseHeader(cursor);
		dexFile.setHeader(header);
		MapList maplist = ParseMap(header,cursor);
		dexFile.setMap_list(maplist);
		ParseItems(dexFile,cursor);
		if (header.link_off != 0 && header.link_size != 0) {
			cursor.move(header.link_off);
			dexFile.link_data = cursor.nextData(header.link_size);
		}
		return dexFile;
	}

	
	public static DexFile ParseDex(File file){
		if(!DexFile.isDexFile(file)) 
		{
			System.out.println("[Error] This file is not DexFile ");
			return null;
		}
		ByteCursor cursor = new ByteCursor(file);
		return ParseDex(cursor);
	}

	private static DexHeader ParseHeader(ByteCursor cursor){
			DexHeader header = new DexHeader();
			header.magic = cursor.nextString();
			header.checksum = cursor.nextInt();
			header.signature = cursor.nextData(20);
			header.file_size = cursor.nextInt();
			header.header_size = cursor.nextInt();
			header.endian_tag = cursor.nextInt();
			header.link_size = cursor.nextInt();
			header.link_off = cursor.nextInt();
			header.map_off = cursor.nextInt();
			header.string_ids_size = cursor.nextInt();
			header.string_ids_off = cursor.nextInt();
			header.type_ids_size = cursor.nextInt();
			header.type_ids_off = cursor.nextInt();
			header.proto_ids_size = cursor.nextInt();
			header.proto_ids_off = cursor.nextInt();
			header.field_ids_size = cursor.nextInt();
			header.field_ids_off = cursor.nextInt();
			header.method_ids_size = cursor.nextInt();
			header.method_ids_off = cursor.nextInt();
			header.class_defs_size = cursor.nextInt();
			header.class_defs_off = cursor.nextInt();
			header.data_size = cursor.nextInt();
			header.data_off = cursor.nextInt();
			header.otherData = cursor.nextData(header.header_size - 0x70);
		return header;
	}

	public static MapList ParseMap(DexHeader header,ByteCursor cursor){
		if (header== null) {
			return null;
		}
		cursor.move(header.map_off);
		int size = cursor.nextInt();
		MapList map = new MapList(size);
		for (int i = 0; i < size; i++) {
			int file_off = cursor.getPos();
			Map_Item item = new Map_Item(cursor.getData(), file_off);
			map.map_list.add(item);
			cursor.belowMove(item.getLength());
		}
		return map;
	}

	public static void ParseItems(DexFile dexfile,ByteCursor cursor){
		MapList map = dexfile.getMap_list();
		int i = 0;
		while (i < map.getSize()) {
			Map_Item mapitem = map.map_list.get(i);
			Pool itemlist = null;
			Pool datalist = null;
			int fristoff;
			switch (mapitem.type) {
			case Map_Item.STRING_ID_MAP:
				itemlist = new Pool<String_Id_Item>(String_Id_Item.class);
				dexfile.setString_id_list(itemlist);
				break;
			case Map_Item.TYPE_ID_MAP:
				itemlist = new Pool<Type_Id_Item>(Type_Id_Item.class);
				dexfile.setType_id_list(itemlist);
				break;
			case Map_Item.PROTO_ID_MAP:
				itemlist = new Pool<Proto_Id_Item>(Proto_Id_Item.class);
				dexfile.setProto_id_list(itemlist);
				break;
			case Map_Item.FIELD_ID_MAP:
				itemlist = new Pool<Field_Id_Item>(Field_Id_Item.class);
				dexfile.setField_id_list(itemlist);
				break;
			case Map_Item.METHOD_ID_MAP:
				itemlist = new Pool<Method_Id_Item>(Method_Id_Item.class);
				dexfile.setMethod_id_list(itemlist);
				break;
			case Map_Item.CLASS_DEF_MAP:
				itemlist = new Pool<Class_Def_Item>(Class_Def_Item.class);
				dexfile.setClass_def_list(itemlist);
				break;
			case Map_Item.STRING_DATA_MAP:
				datalist = new Pool<String_Data_Item>(String_Data_Item.class);
				cursor.move(mapitem.offset);
				fristoff = cursor.getPos();
				for (int pos = 0; pos < mapitem.size; pos++) {
					String_Data_Item item = new String_Data_Item(cursor.getData(), cursor.getPos());
					datalist.add(item);
					cursor.belowMove(item.getLength());
				}
				datalist.setStartOff(fristoff);
				datalist.setEndOff(cursor.getPos());
				datalist.setAllLength(cursor.getPos() - fristoff);
				dexfile.setString_data_list(datalist);
				break;
			case Map_Item.TYPE_LIST_MAP:
				datalist = new Pool<Type_List_Item>(Type_List_Item.class);
				cursor.move(mapitem.offset);
				fristoff = cursor.getPos();
				for (int pos = 0; pos < mapitem.size; pos++) {
					int off = cursor.getPos();
					Type_List_Item item = new Type_List_Item(cursor.getData(), off);
					datalist.add(item);
					cursor.belowMove(item.getLength());
					if(item.size % 2 != 0) 
					{
						cursor.belowMove(2);
					}
				}
				datalist.setStartOff(fristoff);
				datalist.setEndOff(cursor.getPos());
				datalist.setAllLength(cursor.getPos() - fristoff);
				dexfile.setType_list_list(datalist);
			}
			if (itemlist != null) {
				cursor.move(mapitem.offset);
				fristoff = cursor.getPos();
				int pos = 0;
				while (pos++ < mapitem.size) {
					Constructor constructor;
					try {
						constructor = itemlist.itemType.getConstructor(byte[].class, int.class);
						Item item = (Item) (constructor.newInstance(cursor.getData(), cursor.getPos()));
						itemlist.add(item);
						cursor.belowMove(item.getLength());
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				itemlist.setStartOff(fristoff);
				itemlist.setEndOff(cursor.getPos());
				itemlist.setAllLength(cursor.getPos() - fristoff);
			}
			i++;
		}
	}
}
