package com.javapoet.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * 生成 Activity 、Fragment 组件的工具类
 * 
 * @author author
 */
public class ComponentUtils {
	
	private static ClassName appCompatActivity = ClassName.get("android.support.v7.app", "AppCompatActivity");
	private static ClassName fragment = ClassName.get("android.support.v4.app", "Fragment");
	private static ClassName bundle = ClassName.get("android.os", "Bundle");
	private static ClassName Nullable = ClassName.get("android.support.annotation", "Nullable");
	private static ClassName View = ClassName.get("android.view", "View");
	private static ClassName LayoutInflater = ClassName.get("android.view", "LayoutInflater");
	private static ClassName ViewGroup = ClassName.get("android.view", "ViewGroup");
	
	private static ClassName RelativeLayout = ClassName.get("android.widget", "RelativeLayout");
	private static ClassName LinearLayout = ClassName.get("android.widget", "LinearLayout");
	private static ClassName TextView = ClassName.get("android.widget", "TextView");
	private static ClassName Button = ClassName.get("android.widget", "Button");
	
	private static Map<String, ClassName> classNames = new HashMap<String, ClassName>();
	
	private static String author = "author";
	
	public static void init() {
		classNames.put("RelativeLayout", RelativeLayout);
		classNames.put("LinearLayout", LinearLayout);
		classNames.put("TextView", TextView);
		classNames.put("Button", Button);
	}
	
	/**
	 * 生成字段属性 只生成有id的字段
	 */
	public static List<FieldSpec> createField(List<Tag> list) {
		List<FieldSpec> result = new ArrayList<FieldSpec>();
		for (Tag tag : list) {
			if (tag.attributeMap != null && tag.attributeMap.containsKey(Tag.ID_KEY)) {
				String id = tag.attributeMap.get(Tag.ID_KEY).substring(5);
				FieldSpec fieldSpec = FieldSpec.builder(classNames.get(tag.name), id, Modifier.PRIVATE).build();
				result.add(fieldSpec);
			}
		}
		return result;
	}
	
	/**
	 * 生成findViewById
	 * @param list
	 * @return
	 */
	public static CodeBlock createCodeBlocActivity(List<Tag> list) {
		Builder builder = CodeBlock.builder();
		for (Tag tag : list) {
			if (tag.attributeMap != null && tag.attributeMap.containsKey(Tag.ID_KEY)) {
				String id = tag.attributeMap.get(Tag.ID_KEY).substring(5);
				builder.addStatement("$L = ($L)findViewByid($L)", id, tag.name, ("R.id." + id));
			}
		}
		return builder.build();
	}
	
	/**
	 * 生成view.findViewById
	 * @param list
	 * @return
	 */
	public static CodeBlock createCodeBlocFragment(List<Tag> list) {
		Builder builder = CodeBlock.builder();
		for (Tag tag : list) {
			if (tag.attributeMap != null && tag.attributeMap.containsKey(Tag.ID_KEY)) {
				String id = tag.attributeMap.get(Tag.ID_KEY).substring(5);
				builder.addStatement("$L = ($L)view.findViewByid($L)", id, tag.name, ("R.id." + id));
			}
		}
		return builder.build();
	}
	
	/**
	 * 生成 Activity 模板
	 */
	public static void createActivity(String packageName, String activityName, String destFileName, List<FieldSpec> fieldSpec, CodeBlock codeBlock) throws IOException {
		
		MethodSpec onCreate = MethodSpec.methodBuilder("Oncreate")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PROTECTED)
				.addParameter(bundle, "savedInstanceState")
				.addStatement("super.onCreate($L)", "savedInstanceState")
				.addStatement("setContentView($L)", "R.layout.activity_main")
				.addCode(codeBlock)
				.build();
		
		TypeSpec activityClass = TypeSpec.classBuilder(activityName)
				.superclass(appCompatActivity)
				.addFields(fieldSpec)
				.addMethod(onCreate)
				.addJavadoc("$L $L\n", author, new Date().toString())
				.build();
		JavaFile javaFile = JavaFile.builder(packageName, activityClass)
				.build();
		
		javaFile.writeTo(System.out);
		javaFile.writeTo(new File(destFileName));
	}
	
	/**
	 * 生成 Fragment 模板
	 */
	public static void createFragment(String packageName, String fragmentName, String destFileName, List<FieldSpec> fieldSpec,CodeBlock codeBlock) throws IOException {
		
		MethodSpec onCreate = MethodSpec.methodBuilder("oncreate")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(bundle, "savedInstanceState")
				.addStatement("super.onCreate($L)", "savedInstanceState")
				.build();
		
		MethodSpec onCreateView = MethodSpec.methodBuilder("onCreateView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(View)
				.addParameter(LayoutInflater, "inflater")
				.addParameter(ViewGroup, "container")
				.addParameter(bundle, "savedInstanceState")
				.addStatement("View view = null")
				.addCode(codeBlock)
				.addStatement("return view")
				.build();
		
		MethodSpec onDestroyView = MethodSpec.methodBuilder("onDestroyView")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super.onDestroyView()")
				.build();
		
		MethodSpec onDestroy = MethodSpec.methodBuilder("onDestroy")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super.onDestroy()")
				.build();
		
		TypeSpec fragmentClass = TypeSpec.classBuilder(fragmentName)
				.superclass(fragment)
				.addFields(fieldSpec)
				.addMethod(onCreate)
				.addMethod(onCreateView)
				.addMethod(onDestroyView)
				.addMethod(onDestroy)
				.addJavadoc("$L $L\n", author, new Date().toString())
				.build();
		JavaFile javaFile = JavaFile.builder(packageName, fragmentClass)
				.build();
		
		javaFile.writeTo(System.out);
		javaFile.writeTo(new File(destFileName));
	}
}
