/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.persistencejs.gen.generator;

import static org.slim3.gen.ClassConstants.*;

import java.util.Date;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.printer.Printer;

import org.persistencejs.gen.SyncConstants;
import org.persistencejs.gen.SyncData;


/**
 * @author higayasuo
 * 
 */
public class SyncModelMetaGenerator extends ModelMetaGenerator {
	
    private boolean syncEnabled = false;

	/**
	 * @param modelMetaDesc
	 */
	public SyncModelMetaGenerator(ModelMetaDesc modelMetaDesc) {
		super(modelMetaDesc);
		
        for (AttributeMetaDesc attr : modelMetaDesc.getAttributeMetaDescList()) {
        	SyncData sync = attr.getData(SyncConstants.Sync);
			if (sync.isEnabled()) {
				syncEnabled = true;
			}
        }
	}
	
    /**
     * Prints the class.
     * 
     * @param printer
     */
	@Override
    protected void printClass(Printer printer) {
        printer
            .println(
                "//@javax.annotation.Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(),
                ProductInfo.getVersion(),
                new Date());
        printer.println("/** */");
        printer.println("public final class %s extends %s<%s> {", modelMetaDesc
            .getSimpleName(), ClassConstants.ModelMeta, modelMetaDesc
            .getModelClassName());
        printer.println();
        printer.indent();
        printAttributeMetaFields(printer);
        printAttributeListenerFields(printer);
        printSingletonField(printer);
        printGetMethod(printer);
        printConstructor(printer);
        printer.unindent();
        printer.println();
        printer.indent();
        printEntityToModelMethod(printer);
        printModelToEntityMethod(printer);
        printGetKeyMethod(printer);
        printSetKeyMethod(printer);
        printGetVersionMethod(printer);
        printIncrementVersionMethod(printer);
        printPrePutMethod(printer);
        printGetSchemaVersionName(printer);
        printGetClassHierarchyListName(printer);
        
        if (syncEnabled) {
            printJSONtoModelMethod(printer);
            printModelToJSONMethod(printer);
            printGetNameOrIdMethod(printer);	
        } 
        
        printer.unindent();
        printer.print("}");
    }
	
	/**
     * Generates the {@code prePut} method.
     * 
     * @param printer
     *            the printer
     */
	@Override
    protected void printPrePutMethod(final Printer printer) {
        printer.println("@Override");
        printer.println("protected void prePut(Object model) {");
        printer.indent();
        printer.println("assignKeyIfNecessary(model);");
        printer.println("incrementVersion(model);");
        boolean first = true;
        for (AttributeMetaDesc attr : modelMetaDesc.getAttributeMetaDescList()) {
            if (attr.getAttributeListenerClassName() != null
                && !attr.getAttributeListenerClassName().equals(
                    AttributeListener)) {
                if (first) {
                    first = false;
                }
                printer.println("%1$s m = (%1$s) model;", modelMetaDesc
                        .getModelClassName());
                printer
                    .println(
                        "m.%1$s(slim3_%2$sAttributeListener.prePut(m.%3$s()));",
                        attr.getWriteMethodName(),
                        attr.getAttributeName(),
                        attr.getReadMethodName());
            }
        }
        if (syncEnabled) {
        	printer.println("%1$s m = (%1$s) model;", modelMetaDesc
                    .getModelClassName());
        	printer.println("if (m.isDirty() && !m.isSyncDirty()) {");
        	printer.indent();
        	printer.println("m.setLastChange(new java.util.Date().getTime());");
        	printer.println("m.setDirty(false);");
        	printer.unindent();
        	printer.println("}"); 
        }
        printer.unindent();
        printer.println("}");
        printer.println();
    }
	
	/**
	 * Generates printJSONtoModelMethod method
	 * 
	 * @param printer
	 *            the printer
	 */
	protected void printJSONtoModelMethod(Printer printer) {
		printer.println(
				"public %1$s JSONtoModel(com.google.appengine.repackaged.org.json.JSONObject json, long timestamp) {",
				modelMetaDesc.getModelClassName());
		printer.indent();
		printer.println("%1$s model;", modelMetaDesc.getModelClassName());
		printer.println("try {");
		printer.indent();
		printer.println("com.google.appengine.api.datastore.Entity entity;");
		printer.println("com.google.appengine.api.datastore.Key key;");
		printer.println("try {");
		printer.indent();
		printer.println("try {");
		printer.indent();
		printer.println("key = org.slim3.datastore.Datastore.createKey(this, json.getLong(\"id\"));");
		printer.unindent();
		printer.println("} catch (com.google.appengine.repackaged.org.json.JSONException e1) {");
		printer.indent();
		printer.println("key = org.slim3.datastore.Datastore.createKey(this, json.getString(\"id\"));");
		printer.unindent();
		printer.println("}");
		printer.println("entity = org.slim3.datastore.Datastore.get(key);");
		printer.unindent();
		printer.println("} catch (org.slim3.datastore.EntityNotFoundRuntimeException e) {");
		printer.indent();
		printer.println("try {");
		printer.indent();
		printer.println("key = com.google.appengine.api.datastore.KeyFactory.createKey(kind, json.getLong(\"id\"));");
		printer.unindent();
		printer.println("} catch (com.google.appengine.repackaged.org.json.JSONException e2) {");
		printer.indent();
		printer.println("key = com.google.appengine.api.datastore.KeyFactory.createKey(kind, json.getString(\"id\"));");
		printer.unindent();
		printer.println("}");
		printer.println("entity = new com.google.appengine.api.datastore.Entity(key);"); 
		printer.unindent();
		printer.println("}");
		printer.println("model = entityToModel(entity);");

		for (AttributeMetaDesc attr : modelMetaDesc
				.getAttributeMetaDescList()) {
			SyncData sync = attr.getData(SyncConstants.Sync);
			if (sync.isEnabled()) {
				if (sync.isTimestamp()) {
					printer.println("model.%1$s(timestamp);",
							attr.getWriteMethodName());
				} else if (attr.getDataType().getClassName().equals("org.slim3.datastore.ModelRef")) {
					String attrJsonName = attr.getName().substring(0, attr.getName().length() - 3);
					String attrJsonNameCap = java.lang.String.format( "%s%s",
                            java.lang.Character.toUpperCase(attrJsonName.charAt(0)),
                            attrJsonName.substring(1));
					printer.println("String %1$s = \"%1$s\";", attrJsonName);
					printer.println("if (json.has(%1$s)) {", attrJsonName);
					printer.indent();
					printer.println("com.google.appengine.api.datastore.Key ref;");
					printer.println("try {");
					printer.indent();
					printer.println("ref = org.slim3.datastore.Datastore.createKey(%1$sMeta.get(), json.getLong(%2$s));", attrJsonNameCap, attrJsonName);
					printer.unindent();
					printer.println("} catch (com.google.appengine.repackaged.org.json.JSONException e) {");
					printer.indent();
					printer.println("ref = org.slim3.datastore.Datastore.createKey(%1$sMeta.get(), json.getString(%2$s));", attrJsonNameCap, attrJsonName);
					printer.unindent();
					printer.println("}");
					printer.println("model.%1$s().setKey(ref);", attr.getReadMethodName());
					printer.unindent();
					printer.println("}");
				} else if (!attr.isPrimaryKey()) {
					printer.println("if (json.has(\"%1$s\")) {",
							attr.getName());
					printer.indent();
					printer.println(
							"model.%1$s(json.get%2$s(\"%3$s\"));",
							attr.getWriteMethodName(), typeMapper(attr.getDataType().getClassName()), attr.getName());
					printer.unindent();
					printer.println("}");
				}
			}
		}
		
		printer.unindent();
		printer.println("} catch (com.google.appengine.repackaged.org.json.JSONException e) {");
		printer.indent();
		printer.println("return null;");
		printer.unindent();
		printer.println("}");
		printer.println("return model;");
		printer.unindent();
		printer.println("}");
		printer.println();
	}

	/**
	 * Generates ModelToJSON method
	 * 
	 * @param printer
	 *            the printer
	 */
	protected void printModelToJSONMethod(Printer printer) {
		printer.println(
				"public com.google.appengine.repackaged.org.json.JSONObject modelToJSON(%1$s model) {",
				modelMetaDesc.getModelClassName());
		printer.indent();
		printer.println("com.google.appengine.api.datastore.Key key = model.getKey();");
		printer.println("com.google.appengine.repackaged.org.json.JSONObject json = new com.google.appengine.repackaged.org.json.JSONObject();");
		printer.println("try {");
		printer.indent();
		printer.println("json.put(\"id\", getNameOrId(key));");
		for (AttributeMetaDesc attr : modelMetaDesc
				.getAttributeMetaDescList()) {
			SyncData sync = attr.getData(SyncConstants.Sync);
			if (sync.isEnabled()) {
				if (sync.isTimestamp()) {
					printer.println("json.put(\"_lastChange\", model.%1$s());", attr.getReadMethodName());
				} else if (attr.getDataType().getClassName().equals("org.slim3.datastore.ModelRef")) {
					String attrJsonName = attr.getName().substring(0, attr.getName().length() - 3);
					printer.println("if (model.%1$s().getKey() != null) {", attr.getReadMethodName());
					printer.indent();
					printer.println("json.put(\"%1$s\", getNameOrId(model.%2$s().getKey()));", attrJsonName, attr.getReadMethodName());
					printer.unindent();
					printer.println("}");
				} else if (!attr.isPrimaryKey()) {
					printer.println("json.put(\"%1$s\", model.%2$s());", attr.getName(), attr.getReadMethodName());
				}
			}
		}
		printer.unindent();
		printer.println("} catch (com.google.appengine.repackaged.org.json.JSONException e) {");
		printer.indent();
		printer.println("return null;");
		printer.unindent();
		printer.println("}");
		printer.println("return json;");
		printer.unindent();
		printer.println("}");
		printer.println();
	}

	/**
	 * Generates getNameOrId method
	 * 
	 * @param printer
	 *            the printer
	 */
	private void printGetNameOrIdMethod(Printer printer) {
		printer.println(
				"public String getNameOrId(com.google.appengine.api.datastore.Key key) {");
		printer.indent();
		printer.println("return (key.getName() == null) ? Long.toString(key.getId()) : key.getName();");
		printer.unindent();
		printer.println("}");
		printer.println();
	}		
	
	private String typeMapper(String type) {
		if (type.equals("java.lang.String")) {
			return "String";
		} else if (type.equals("boolean")) {
			return "Boolean";
		} else {
			return "";		
	    }
	}
	

//	@Override
//	protected void printAttributeMetaFields(Printer printer) {
//		AttributeMetaFieldsGenerator generator = new SyncAttributeMetaFieldsGenerator(
//				printer);
//		generator.generate();
//	}
//	
//	protected class SyncAttributeMetaFieldsGenerator extends
//			AttributeMetaFieldsGenerator {
//
//		/**
//		 * @param printer
//		 */
//		public SyncAttributeMetaFieldsGenerator(Printer printer) {
//			super(printer);
//		}
//
//		@Override
//		public void generate() {
//			for (AttributeMetaDesc attr : modelMetaDesc
//					.getAttributeMetaDescList()) {
//				if (Boolean.TRUE.equals(attr.getData(SyncConstants.Sync))) {
//					printer.println("// Sync:true");
//				} else {
//					printer.println("// Sync:false");
//				}
//				DataType dataType = attr.getDataType();
//				dataType.accept(this, attr);
//			}
//		}
//	}
}