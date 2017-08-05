package huluwa.dexparser.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import huluwa.dexparser.Exception.CursorMoveException;
import huluwa.dexparser.Exception.NonDexFileException;
import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.format.Class_Def_Item;
import huluwa.dexparser.format.DexFile;
import huluwa.dexparser.format.DexHeader;
import huluwa.dexparser.format.Field_Id_Item;
import huluwa.dexparser.format.ItemList;
import huluwa.dexparser.format.MapList;
import huluwa.dexparser.format.Map_Item;
import huluwa.dexparser.format.Method_Id_Item;
import huluwa.dexparser.format.Proto_Id_Item;
import huluwa.dexparser.format.String_Data_Item;
import huluwa.dexparser.format.String_Id_Item;
import huluwa.dexparser.format.Type_Id_Item;
import huluwa.dexparser.format.Type_List_Item;

public class DexParser {
	private ByteCursor cursor;
	private DexFile dexFile;

	public DexParser(File dexfile) throws NonDexFileException, IOException {
		this(new DexFile(dexfile));
	}

	public DexParser(DexFile dexfile) throws NonDexFileException, IOException {
		if (!dexfile.isDexFile()) {
			throw new NonDexFileException(dexfile.getFile().getAbsolutePath());
		}
		this.dexFile = dexfile;
		InputStream in = new FileInputStream(dexfile.getFile());
		byte[] data = new byte[in.available()];
		in.read(data);
		in.close();
		this.cursor = new ByteCursor(data);
	}

	public DexParser(byte data[]) {
		this(new ByteCursor(data));
	}

	public DexParser(ByteCursor cursor) {
		this.cursor = cursor;
		this.dexFile = new DexFile();
	}

	public DexFile parseDex() throws IOException, NonDexFileException, QueryNextDataException, CursorMoveException,
			NonSameItemLengthException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NonStandardLeb128Exception {
		parseHeader();
		parseMap();
		parseItems();
		if (dexFile.getHeader().link_off != 0 && dexFile.getHeader().link_size != 0) {
			this.cursor.move(dexFile.getHeader().link_off);
			this.dexFile.link_data = this.cursor.nextData(dexFile.getHeader().link_size);
		}
		return this.dexFile;
	}

	public DexHeader parseHeader() throws IOException, NonDexFileException, QueryNextDataException {
		if (dexFile.getHeader() == null) {
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
			dexFile.setHeader(header);
		}
		return dexFile.getHeader();
	}

	public MapList parseMap() throws CursorMoveException, QueryNextDataException, NonSameItemLengthException,
			IOException, NonDexFileException {
		if (dexFile.getHeader() == null) {
			parseHeader();
		}
		if (dexFile.getMap_list() == null) {
			cursor.move(dexFile.getHeader().map_off);
			int size = cursor.nextInt();
			MapList map = new MapList(size);
			for (int i = 0; i < size; i++) {
				int file_off = cursor.position;
				Map_Item item = new Map_Item(cursor.getBytes(), file_off);
				map.map_list.add(item);
				this.cursor.belowMove(item.getLength());
			}
			dexFile.setMap_list(map);
		}
		return dexFile.getMap_list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void parseItems()
			throws CursorMoveException, QueryNextDataException, NonSameItemLengthException, IOException,
			NonDexFileException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NonStandardLeb128Exception {
		if (dexFile.getMap_list() == null) {
			parseMap();
		}
		MapList map = dexFile.getMap_list();
		int i = 0;
		while (i < map.getSize()) {
			Map_Item mapitem = map.map_list.get(i);
			ItemList itemlist = null;
			ItemList datalist = null;
			int fristoff;
			switch (mapitem.type) {
			case Map_Item.STRING_ID_MAP:
				itemlist = new ItemList<String_Id_Item>(String_Id_Item.class);
				dexFile.setString_id_list(itemlist);
				break;
			case Map_Item.TYPE_ID_MAP:
				itemlist = new ItemList<Type_Id_Item>(Type_Id_Item.class);
				dexFile.setType_id_list(itemlist);
				break;
			case Map_Item.PROTO_ID_MAP:
				itemlist = new ItemList<Proto_Id_Item>(Proto_Id_Item.class);
				dexFile.setProto_id_list(itemlist);
				break;
			case Map_Item.FIELD_ID_MAP:
				itemlist = new ItemList<Field_Id_Item>(Field_Id_Item.class);
				dexFile.setField_id_list(itemlist);
				break;
			case Map_Item.METHOD_ID_MAP:
				itemlist = new ItemList<Method_Id_Item>(Method_Id_Item.class);
				dexFile.setMethod_id_list(itemlist);
				break;
			case Map_Item.CLASS_DEF_MAP:
				itemlist = new ItemList<Class_Def_Item>(Class_Def_Item.class);
				dexFile.setClass_def_list(itemlist);
				break;
			case Map_Item.STRING_DATA_MAP:
				datalist = new ItemList<String_Data_Item>(String_Data_Item.class);
				this.cursor.move(mapitem.offset);
				fristoff = this.cursor.position;
				for (int pos = 0; pos < mapitem.size; pos++) {
					String_Data_Item item = new String_Data_Item(this.cursor.getBytes(), this.cursor.position);
					datalist.add(item);
					this.cursor.belowMove(item.getLength());
				}
				datalist.setStartOff(fristoff);
				datalist.setEndOff(this.cursor.position);
				datalist.setAllLength(this.cursor.position - fristoff);
				dexFile.setString_data_list(datalist);
				break;
			case Map_Item.TYPE_LIST_MAP:
				datalist = new ItemList<Type_List_Item>(Type_List_Item.class);
				this.cursor.move(mapitem.offset);
				fristoff = this.cursor.position;
				for (int pos = 0; pos < mapitem.size; pos++) {
					int off = this.cursor.position;
					Type_List_Item item = new Type_List_Item(this.cursor.getBytes(), off);
					datalist.add(item);
					this.cursor.belowMove(item.getLength());
				}
				datalist.setStartOff(fristoff);
				datalist.setEndOff(this.cursor.position);
				datalist.setAllLength(this.cursor.position - fristoff);
				dexFile.setType_list_list(datalist);
			}
			if (itemlist != null) {
				this.cursor.move(mapitem.offset);
				fristoff = this.cursor.position;
				int pos = 0;
				while (pos++ < mapitem.size) {
					Constructor constructor = itemlist.itemType.getConstructor(byte[].class, int.class);
					Item item = (Item) (constructor.newInstance(this.cursor.getBytes(), this.cursor.position));
					itemlist.add(item);
					this.cursor.belowMove(item.getLength());
				}
				itemlist.setStartOff(fristoff);
				itemlist.setEndOff(this.cursor.position);
				itemlist.setAllLength(this.cursor.position - fristoff);
			}
			i++;
		}
	}
}
