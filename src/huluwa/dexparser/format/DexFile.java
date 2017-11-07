package huluwa.dexparser.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DexFile {
	private File file;

	private DexHeader header;

	private ItemList<String_Id_Item> string_id_list;
	private ItemList<Type_Id_Item> type_id_list;
	private ItemList<Proto_Id_Item> proto_id_list;
	private ItemList<Field_Id_Item> field_id_list;
	private ItemList<Method_Id_Item> method_id_list;
	private ItemList<Class_Def_Item> class_def_list;

	private ItemList<String_Data_Item> string_data_list;
	private ItemList<Type_List_Item> type_list_list;

	private MapList map_list;

	public byte link_data[];

	public DexFile(File file) {
		this.file = file;
	}

	public DexFile() {

	}

	public Map_Item getMap(int type) {
		MapList list = getMap_list();
		for (int i = 0; i < list.getSize(); i++) {
			if (list.get(i).type == type) {
				return list.get(i);
			}
		}
		return null;
	}

	public static boolean isDexFile(File file){
		if (file == null) {
			return false;
		}
		InputStream in;
		try {
			in = new FileInputStream(file);
			if (in.available() < 4) {
				in.close();
				return false;
			}
			if (in.read() != 0x64 || in.read() != 0x65 || in.read() != 0x78 || in.read() != 0x0A) {
				in.close();
				in = null;
				return false;
			}
			in.close();
			in = null;
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public short findMethod(String mtdStr) {
		for(short i =0 ;i < getMethod_id_list().size();i++) {
			Method_Id_Item method = getMethod_id_list().get(i);
			String s = getName(method);
			if(mtdStr.equals(s)) {
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<encoded_method> getAllEncodedMethod(){
		ArrayList<encoded_method> all = new ArrayList<encoded_method>();
		for (Class_Def_Item cls : class_def_list) {
			if (cls.class_data == null) {
				continue;
			}
			String clsName = getName(cls);
			all.addAll(cls.class_data.direct_methods);
			all.addAll(cls.class_data.virtual_methods);
		}
		return all;
	}
	
	public ArrayList<insns_item> getAllInsnsItem() {
		ArrayList<insns_item> all = new ArrayList<insns_item>();
		for (encoded_method method : getAllEncodedMethod()) {
			if (method.code != null) {
				all.addAll(method.code.insns_items);
			}
		}
		return all;
	}

	public String getNameByMethodId(int id) {
		return getName(method_id_list.get(id));
	}

	public String getNameByClassId(int id) {
		return getName(class_def_list.get(id));
	}

	public String getNameByTypeId(int id) {
		return getName(type_id_list.get(id));
	}

	public String getNameByFieldId(int id) {
		return getName(field_id_list.get(id));
	}

	public String getNameByProtoId(int id) {
		return getName(proto_id_list.get(id));
	}
	
	public String getName(encoded_method method) {
		return getNameByMethodId(method.real_id);
	}

	public String getName(Type_Id_Item type) {
		return getString(type.id);
	}

	public String getName(Proto_Id_Item proto) {
		return getString(proto.shorty_id);
	}

	public String getName(Field_Id_Item field) {
		return getString(field.name_id);
	}

	public String getName(Class_Def_Item cls) {
		return getNameByTypeId(cls.class_id);
	}

	public String getName(Method_Id_Item method) {
		String className = getNameByTypeId(method.class_id).replaceAll("/", "\\.");
		className = className.substring(1, className.length() - 2);
		return className + "." + getString(method.name_id).replaceAll("\0","") + "("+ getNameByProtoId(method.proto_id).replaceAll("\0","") + ")";
	}

	public String getString(int id) {
		return new String(string_data_list.get(id).body);
	}

	public File getFile() {
		return this.file;
	}

	public DexHeader getHeader() {
		return header;
	}

	public void setHeader(DexHeader header) {
		this.header = header;
	}

	public ItemList<String_Id_Item> getString_id_list() {
		return string_id_list;
	}

	public void setString_id_list(ItemList<String_Id_Item> string_id_list) {
		this.string_id_list = string_id_list;
	}

	public ItemList<Type_Id_Item> getType_id_list() {
		return type_id_list;
	}

	public void setType_id_list(ItemList<Type_Id_Item> type_id_list) {
		this.type_id_list = type_id_list;
	}

	public ItemList<Proto_Id_Item> getProto_id_list() {
		return proto_id_list;
	}

	public void setProto_id_list(ItemList<Proto_Id_Item> proto_id_list) {
		this.proto_id_list = proto_id_list;
	}

	public ItemList<Method_Id_Item> getMethod_id_list() {
		return method_id_list;
	}

	public void setMethod_id_list(ItemList<Method_Id_Item> method_id_list) {
		this.method_id_list = method_id_list;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public MapList getMap_list() {
		return map_list;
	}

	public void setMap_list(MapList map_list) {
		this.map_list = map_list;
	}

	public ItemList<Field_Id_Item> getField_id_list() {
		return field_id_list;
	}

	public void setField_id_list(ItemList<Field_Id_Item> field_id_list) {
		this.field_id_list = field_id_list;
	}

	public ItemList<Class_Def_Item> getClass_def_list() {
		return class_def_list;
	}

	public void setClass_def_list(ItemList<Class_Def_Item> class_def_list) {
		this.class_def_list = class_def_list;
	}

	public ItemList<String_Data_Item> getString_data_list() {
		return string_data_list;
	}

	public void setString_data_list(ItemList<String_Data_Item> string_data_list) {
		this.string_data_list = string_data_list;
	}

	public ItemList<Type_List_Item> getType_list_list() {
		return type_list_list;
	}

	public void setType_list_list(ItemList<Type_List_Item> type_list_list) {
		this.type_list_list = type_list_list;
	}

}
