package com.javapoet.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ParseXml {

	public static List<Tag> parseXML(InputStream inputStream, String encode) throws Exception {
		List<Tag> result = new LinkedList<Tag>();
		Tag tag = null;

		// 创建一个xml解析的工厂
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			// 获得xml解析类的引用
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(inputStream, encode);
			// 获得事件的类型
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					int attributeCount = parser.getAttributeCount();
					Map<String, String> attributeMap = new HashMap<String, String>();
					for (int i = 0; i < attributeCount; i++) {
						attributeMap.put(parser.getAttributeName(i), parser.getAttributeValue(i));
					}
					tag = new Tag(parser.getName(), attributeMap);
					for (Tag t : result) {
						if (t.isSameId(tag)) {
							throw new Exception("Not allowed to have the same id");
						}
					}
					result.add(tag);
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return result;
	}
}
