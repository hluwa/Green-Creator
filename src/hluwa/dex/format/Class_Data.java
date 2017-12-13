package hluwa.dex.format;

import java.util.ArrayList;
import java.util.List;

import hluwa.dex.base.Item;
import hluwa.dex.type.uLeb128;

public class Class_Data extends Item {
	public static final String itemName = "Class_Data";

	public uLeb128 static_fields_size;
	public uLeb128 instance_fields_size;
	public uLeb128 direct_methods_size;
	public uLeb128 virtual_methods_size;
	public List<encoded_field> static_fields;
	public List<encoded_field> instance_fields;
	public List<encoded_method> direct_methods;
	public List<encoded_method> virtual_methods;

	public Class_Data(byte[] data, int off) {
		super(data, off);
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		int list_length = 0;
		for (encoded_field sf : static_fields) {
			list_length += sf.getLength();
		}
		for (encoded_field inf : instance_fields) {
			list_length += inf.getLength();
		}
		for (encoded_method dm : direct_methods) {
			list_length += dm.getLength();
		}
		for (encoded_method vm : virtual_methods) {
			list_length += vm.getLength();
		}
		return this.static_fields_size.getLength() + this.instance_fields_size.getLength()
				+ this.direct_methods_size.getLength() + this.virtual_methods_size.getLength() + list_length;
	}

	@Override
	public void parseData() {
		static_fields = new ArrayList<encoded_field>();
		instance_fields = new ArrayList<encoded_field>();
		direct_methods = new ArrayList<encoded_method>();
		virtual_methods = new ArrayList<encoded_method>();
		this.static_fields_size = cursor.nextuLeb128();
		this.instance_fields_size = cursor.nextuLeb128();
		this.direct_methods_size = cursor.nextuLeb128();
		this.virtual_methods_size = cursor.nextuLeb128();

		for (int i = 0; i < static_fields_size.toInt(); i++) {
			encoded_field field = new encoded_field(this.cursor.getData(), this.cursor.getPos());
			this.cursor.belowMove(field.getLength());
			if (i != 0) {
				field.real_id = this.static_fields.get(i - 1).real_id + field.field_id.toInt();
			} else {
				field.real_id = field.field_id.toInt();
			}
			this.static_fields.add(field);

		}
		for (int i = 0; i < instance_fields_size.toInt(); i++) {
			encoded_field field = new encoded_field(this.cursor.getData(), this.cursor.getPos());
			this.cursor.belowMove(field.getLength());
			if (i != 0) {
				field.real_id = this.instance_fields.get(i - 1).real_id + field.field_id.toInt();
			} else {
				field.real_id = field.field_id.toInt();
			}
			this.instance_fields.add(field);
		}
		for (int i = 0; i < direct_methods_size.toInt(); i++) {
			encoded_method method = new encoded_method(this.cursor.getData(), this.cursor.getPos());
			this.cursor.belowMove(method.getLength());
			if (i != 0) {
				method.real_id = this.direct_methods.get(i - 1).real_id + method.method_id.toInt();
			} else {
				method.real_id = method.method_id.toInt();
			}
			this.direct_methods.add(method);
		}
		for (int i = 0; i < virtual_methods_size.toInt(); i++) {
			encoded_method method = new encoded_method(this.cursor.getData(), this.cursor.getPos());
			this.cursor.belowMove(method.getLength());
			if (i != 0) {
				method.real_id = this.virtual_methods.get(i - 1).real_id + method.method_id.toInt();
			} else {
				method.real_id = method.method_id.toInt();
			}
			this.virtual_methods.add(method);
		}

	}
}
