package hluwa.dex;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import hluwa.common.Tools;
import hluwa.common.Exception.CursorMoveException;
import hluwa.dex.Exception.NonStandardLeb128Exception;
import hluwa.common.Exception.QueryNextDataException;
import hluwa.dex.type.TypeCast;
import hluwa.dex.type.uLeb128;
import hluwa.dex.format.Class_Def_Item;
import hluwa.dex.format.Code_Item;
import hluwa.dex.format.DexFile;
import hluwa.dex.format.DexHeader;
import hluwa.dex.format.Field_Id_Item;
import hluwa.dex.base.Pool;
import hluwa.dex.format.Method_Id_Item;
import hluwa.dex.format.String_Data_Item;
import hluwa.dex.format.String_Id_Item;
import hluwa.dex.format.encoded_method;
import hluwa.dex.format.insns_item;
import hluwa.common.FileChange;

public class DexChanger extends FileChange {
	private DexFile dexFile;

	public DexChanger(File file) throws IOException {
		super(file);
		dexFile = DexParser.ParseDex(file);
	}

	public DexChanger(DexFile dex) throws IOException {
		super(dex.getFile());
		this.dexFile = dex;
	}

	public DexFile getDexFile() {
		return dexFile;
	}

	public void setDexFile(DexFile dexFile) {
		this.dexFile = dexFile;
	}

	
	public void setNop(insns_item insns) {
		setNop(insns.getFileOff(), insns.getLength()); 
	}

	public void setNop(int off, int num) {
			this.move(off);
			for (int i = 0; i < num; i++) {
				this.changeByte((byte) 0);
			}

	}

	// 这个模块的作用是从Class_Def的source_file_id字段恢复原本的类名称,但还有坑(多Dex,内部类) 仅可作分析用!
	public void restoreClassesNameBySource()
			throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		DexFile dexfile = this.getDexFile();
		Pool<Class_Def_Item> classes = dexfile.getClass_def_list();
		for (int i = 0; i < classes.size(); i++) {
			Class_Def_Item cls = classes.get(i);

			// 判断source_file_id是否有效
			if (cls.source_file_id == 0xffffff || cls.source_file_id == 0 || cls.source_file_id == -1) {
				continue;
			}
			boolean has = false;
			String source = dexfile.getString(cls.source_file_id);
			String name = dexfile.getName(cls);
			String pageName = name.substring(0, name.lastIndexOf("/") + 1);
			String clsName = pageName + source;

			// 这个while是判断后面是否还有类的source_file同名,这种情况下不能直接恢复
			int i1 = i + 1;
			while (i1 < classes.size()) {
				Class_Def_Item cls1 = classes.get(i1);
				if (cls1.source_file_id == 0xffffff || cls1.source_file_id == 0 || cls1.source_file_id == -1) {
					i1++;
					continue;
				}
				String source1 = new String(dexfile.getString(cls1.source_file_id));
				;
				String name1 = new String(dexfile.getName(cls1));
				String pageName1 = name1.substring(0, name1.lastIndexOf("/") + 1);
				String clsName1 = pageName1 + source1;
				if (clsName.equals(clsName1)) {
					classes.remove(i1);// 同名则顺便将其从后面的列表删除
					i1--;
					has = true;
				}

				i1++;
			}
			if (!has) {
				String destName = pageName + source.substring(0, source.length() - 6) + ";";
				System.out.println("Change: " + name + "	to	" + destName);
				this.changeClassName(cls, destName);
			}

		}
		return;

	}

	public void ChangeEncodedMethodCode(encoded_method method, Code_Item code) throws CursorMoveException {
		int result = addShort(code.registers_size);
		this.move(method.getFileOff() + method.method_id.getLength() + method.access_flags.getLength());
		this.changeLeb128(uLeb128.Create(result));
		addShort(code.ins_size);
		addShort(code.outs_size);
		addShort(code.tries_size);
		addInt(code.debug_info_off);
		addInt(code.insns_size);
		addData(code.insns);
		if(code.tries_size != 0) {
			addData(code.try_items);
			addData(code.handle);
		}
	}
	
	public void changeClassesName() throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		DexFile dexfile = this.getDexFile();
		Pool<Class_Def_Item> classes = dexfile.getClass_def_list();

		int i = 0;
		for (Class_Def_Item cls : classes) {
			String name = dexfile.getName(cls);
			if (Tools.isASCII(name.getBytes())) {
				System.out.println("Change: " + name + " to " + "mtd_" + i);
				this.changeClassName(cls, "cls" + i++);
			}
		}
	}

	public void changeMethodsName() throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		DexFile dexfile = this.getDexFile();
		Pool<Method_Id_Item> methods = dexfile.getMethod_id_list();

		int i = 0;
		for (Method_Id_Item method : methods) {
			String name = dexfile.getName(method);
			if (Tools.isASCII(name.getBytes())) {
				System.out.println("Change: " + name + " to " + "mtd_" + i);
				this.changeMethodName(method, "mtd_" + i++);
			}
		}

	}

	public void changeFieldsName() throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		DexFile dexfile = this.getDexFile();
		Pool<Field_Id_Item> fields = dexfile.getField_id_list();

		int i = 0;
		for (Field_Id_Item field : fields) {
			String name = dexfile.getName(field);
			if (Tools.isASCII(name.getBytes())) {
				System.out.println("Change: " + name + " to " + "fid_" + i);
				this.changeFieldName(field, "fid_" + i++);
			}
		}
	}

	public void changeFieldName(Field_Id_Item field, String destName)
			throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		this.changeString(field.name_id, destName);
	}

	public void changeMethodName(Method_Id_Item method, String destName)
			throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		this.changeString(method.name_id, destName);
	}

	public void changeClassName(Class_Def_Item cls, String destName)
			throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		changeString(this.getDexFile().getType_id_list().get(cls.class_id).id, destName);
	}

	public void changeInt(int off,int value)
	{
			this.move(off);
		this.changeInt(value);
	}
	
	public void changeString(int id, String data)
			throws QueryNextDataException, NonStandardLeb128Exception, CursorMoveException {
		byte length[] = new TypeCast(data.length()).toLeb128().getData();
		byte bytes[] = data.getBytes();
		int size = length.length + bytes.length;
		if (bytes[bytes.length - 1] != 0) {
			size++;
		}
		byte itemdata[] = new byte[size];
		// String_Data_Item = length + bytes
		System.arraycopy(length, 0, itemdata, 0, length.length);
		System.arraycopy(bytes, 0, itemdata, length.length, bytes.length);
		String_Data_Item item = new String_Data_Item(itemdata, 0);
		int off = addString_Data(item);
		changeStrIdOff(id, off);
	}

	public void changeStrIdOff(int id, int off) throws CursorMoveException {
		this.move(dexFile.getHeader().string_ids_off + id * String_Id_Item.length);
		this.changeInt(off);
	}

	public int addString_Data(String_Data_Item string_data_item) throws CursorMoveException {
		int off = this.addLeb128(string_data_item.length);
		addData(string_data_item.body);
		return off;
	}

	public int addLeb128(uLeb128 val) throws CursorMoveException {
		return addData(new TypeCast(val).toBytes());
	}

	public int addInt(int val) throws CursorMoveException {
		return addData(new TypeCast(val).toBytes());
	}

	public int addShort(Short val) throws CursorMoveException {
		return addData(new TypeCast(val).toBytes());
	}

	public int addData(byte data[]) throws CursorMoveException {
		if (dexFile.getHeader().link_off == 0) {
			dexFile.getHeader().link_off = getLength();
			dexFile.getHeader().link_size = 0;
		}
		this.move(dexFile.getHeader().link_off + dexFile.getHeader().link_size);
		int off = this.position;
		this.instData(data);
		dexFile.getHeader().file_size += data.length;
		dexFile.getHeader().link_size += data.length;
		return off;
	}

	public void flush() {
		this.move(0);
		DexHeader header = dexFile.getHeader();
		this.changeData(header.magic);
		this.changeInt(header.checksum);
		this.changeData(header.signature);
		this.changeInt(header.file_size);
		this.changeInt(header.header_size);
		this.changeInt(header.endian_tag);
		this.changeInt(header.link_size);
		this.changeInt(header.link_off);
		this.changeInt(header.map_off);
		this.changeInt(header.string_ids_size);
		this.changeInt(header.string_ids_off);
		this.changeInt(header.type_ids_size);
		this.changeInt(header.type_ids_off);
		this.changeInt(header.proto_ids_size);
		this.changeInt(header.proto_ids_off);
		this.changeInt(header.field_ids_size);
		this.changeInt(header.field_ids_off);
		this.changeInt(header.method_ids_size);
		this.changeInt(header.method_ids_off);
		this.changeInt(header.class_defs_size);
		this.changeInt(header.class_defs_off);
		this.changeInt(header.data_size);
		this.changeInt(header.data_off);
		super.flush(); // 先将修改的数据flush,否则this.data还是旧数据
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(this.data, 32, this.data.length - 32);
			header.signature = mdTemp.digest(); // 计算Signature
			System.arraycopy(header.signature, 0, this.data, 12, 20); // 覆盖原Signature
			Adler32 checksum = new Adler32();
			checksum.update(this.data, 12, this.data.length - 12);
			header.checksum = (int) checksum.getValue(); // 计算checksum
			this.move(8);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("[*E]" + "rebuild" + ":" + e.getMessage());
		}
		this.changeInt(header.checksum);
		this.changeData(header.signature);
		super.flush();
	}
}
