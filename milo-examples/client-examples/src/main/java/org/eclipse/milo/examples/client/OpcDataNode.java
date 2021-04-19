package org.eclipse.milo.examples.client;



public final class OpcDataNode {
	
	private String path = "";
    private String namespace = "";
    private String value = "";
    private String type = "";
    private String unit = "";
    private String date = "";

	    public void setPath(String path) {
		this.path = path;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setDate(String date) {
		this.date = date;
	}
		
		public String getPath() {
			return path;
		}
		public String getNamespace() {
			return namespace;
		}
		public String getValue() {
			return value;
		}
		public String getType() {
			return type;
		}
		public String getUnit() {
			return unit;
		}
		public String getDate() {
			return date;
		}
		
}
