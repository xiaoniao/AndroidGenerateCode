package com.javapoet.android;

import java.util.Map;

/**
 * 标签
 * 
 * TextView tv1<br>
 * android:layout_width:wrap_content<br>
 * android:layout_height:wrap_content<br>
 * android:text:Hello World!<br>
 */
public class Tag {
	public static final String ID_KEY = "android:id";
	public String name;// 标签名字
	public Map<String, String> attributeMap;// 属性值

	public Tag(String name, Map<String, String> attributeMap) {
		this.name = name;
		this.attributeMap = attributeMap;
	}

	@Override
	public String toString() {
		return name;
	}

	// 判断控件id是否一样
	public boolean isSameId(Tag tag) {
		if (tag.attributeMap == null || attributeMap == null) {
			return false;
		}
		if (!tag.attributeMap.containsKey(ID_KEY) || !attributeMap.containsKey(ID_KEY)) {
			return false;
		}
		if (tag.attributeMap.get(ID_KEY).equals(attributeMap.get(ID_KEY))) {
			return true;
		}
		return false;
	}
}
