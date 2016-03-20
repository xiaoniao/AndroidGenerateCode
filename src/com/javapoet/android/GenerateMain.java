package com.javapoet.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

/**
 * 目前只生成有id的findViewByid
 */
public class GenerateMain {

	public static void main(String[] args) {

		ComponentUtils.init();

		File directory = new File("xml");
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				if (!file.getName().endsWith(".xml")) {
					continue;
				}
				// 只解析xml文件
				List<Tag> list = null;
				try {
					list = ParseXml.parseXML(new FileInputStream(file), "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				List<FieldSpec> fieldSpecs = ComponentUtils.createField(list);
				CodeBlock activityOncreateCode = ComponentUtils.createCodeBlocActivity(list);
				CodeBlock fragmnetOncreateCode = ComponentUtils.createCodeBlocFragment(list);
				try {
					ComponentUtils.createActivity("com.javapoet.codegenerate", "MainActivity", "src", fieldSpecs, activityOncreateCode);
					ComponentUtils.createFragment("com.javapoet.codegenerate", "MainFragment", "src", fieldSpecs, fragmnetOncreateCode);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
