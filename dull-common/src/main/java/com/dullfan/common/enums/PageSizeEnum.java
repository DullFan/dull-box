package com.dullfan.common.enums;

public enum PageSizeEnum {
	SIZE15(15), SIZE20(20), SIZE30(30), SIZE40(40), SIZE50(50);
	final int size;
	private PageSizeEnum(int size) {
		this.size = size;
	}
	public int getSize() {
		return this.size;
	}
}
