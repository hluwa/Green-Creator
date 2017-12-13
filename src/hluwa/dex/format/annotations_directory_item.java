package hluwa.dex.format;

import hluwa.dex.base.Item;

public class annotations_directory_item extends Item {

	public int class_annotations_off;
	public int fields_size;
	public int annotated_methods_size;
	public int annotated_parameters_size;
	public id_annotation[] field_annotations;
	public id_annotation[] method_annotations;
	public id_annotation[] parameter_annotations;
	
	
	public annotations_directory_item(byte[] data, int off) {
		super(data, off);
	}

	@Override
	public void parseData() {
		this.class_annotations_off = this.cursor.nextInt();
		this.fields_size = this.cursor.nextInt();
		this.annotated_methods_size = this.cursor.nextInt();
		this.annotated_parameters_size = this.cursor.nextInt();
		if(fields_size != 0) 
		{
			field_annotations = new id_annotation[fields_size];
			for(int i = 0;i < fields_size;i++) 
			{
				field_annotations[i] = new id_annotation(this.cursor.getData(),this.cursor.getPos());
				this.cursor.belowMove(field_annotations[i].getLength());
			}
		}
		if(annotated_methods_size != 0) 
		{
			method_annotations = new id_annotation[annotated_methods_size];
			for(int i = 0;i < annotated_methods_size;i++) 
			{
				method_annotations[i] = new id_annotation(this.cursor.getData(),this.cursor.getPos());
				this.cursor.belowMove(method_annotations[i].getLength());
			}
		}
		if(annotated_parameters_size != 0) 
		{
			parameter_annotations = new id_annotation[annotated_parameters_size];
			for(int i = 0;i < annotated_parameters_size;i++) 
			{
				parameter_annotations[i] = new id_annotation(this.cursor.getData(),this.cursor.getPos());
				this.cursor.belowMove(parameter_annotations[i].getLength());
			}
		}
	}

	@Override
	public int getLength() {
		int len = 16;
		if(field_annotations != null) 
		{
			for(int i = 0;i < field_annotations.length;i++) 
			{
				len += field_annotations[i].getLength();
			}
		}
		if(method_annotations != null) 
		{
			for(int i = 0;i < method_annotations.length;i++) 
			{
				len += method_annotations[i].getLength();
			}
		}
		if(parameter_annotations != null) 
		{
			for(int i = 0;i < parameter_annotations.length;i++) 
			{
				len += parameter_annotations[i].getLength();
			}
		}
		return len;
	}

}
