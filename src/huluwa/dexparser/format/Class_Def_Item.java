package huluwa.dexparser.format;

import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;

public class Class_Def_Item extends Item {
	private static String itemName = "Class_Def";
	private static int length = 32;

	public int class_id;
	public int access_flags;
	public int superclass_id;
	public int interfaces_off;
	public int source_file_id;
	public int annotations_off;
	public int class_data_off;
	public int static_value_off;

	public Class_Data class_data;
	public Type_List_Item interfaces;
	public encoded_array static_value;
	public annotations_directory_item annotations;
	
	public Class_Def_Item(byte[] data, int off) {
		super(data, off);

	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData(){
		class_id = this.cursor.nextInt();
		access_flags = this.cursor.nextInt();
		superclass_id = this.cursor.nextInt();
		interfaces_off = this.cursor.nextInt();
		source_file_id = this.cursor.nextInt();
		annotations_off = this.cursor.nextInt();
		class_data_off = this.cursor.nextInt();
		static_value_off = this.cursor.nextInt();
		if (class_data_off != 0) {
			this.class_data = new Class_Data(this.cursor.getBytes(), class_data_off);
		}
		if(interfaces_off != 0) 
		{
			this.interfaces = new Type_List_Item(this.cursor.getBytes(), interfaces_off);
		}
		if(static_value_off != 0) 
		{
			this.static_value = new encoded_array(this.cursor.getBytes(), static_value_off);
		}
		if(annotations_off != 0) 
		{
			this.annotations = new annotations_directory_item(this.cursor.getBytes(),annotations_off);
		}
	}
}
