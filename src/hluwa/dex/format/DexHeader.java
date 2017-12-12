package hluwa.dex.format;

import hluwa.dex.type.TypeCast;

public class DexHeader {

	public byte magic[] = new byte[8];
	public int checksum;
	public byte signature[] = new byte[20];
	public int file_size;
	public int header_size = 0x70;
	public int endian_tag = TypeCast.ENDIAN_CONSTANT;
	public int link_size;
	public int link_off;
	public int map_off;
	public int string_ids_size;
	public int string_ids_off;
	public int type_ids_size;
	public int type_ids_off;
	public int proto_ids_size;
	public int proto_ids_off;
	public int field_ids_size;
	public int field_ids_off;
	public int method_ids_size;
	public int method_ids_off;
	public int class_defs_size;
	public int class_defs_off;
	public int data_size;
	public int data_off;
	public byte otherData[];
}
