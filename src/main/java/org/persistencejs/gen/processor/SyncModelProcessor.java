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
package org.persistencejs.gen.processor;

import java.util.Set;

import org.persistencejs.gen.desc.SyncAttributeMetaDescFactory;
import org.persistencejs.gen.desc.SyncModelMetaDescFactory;
import org.persistencejs.gen.generator.SyncModelMetaGenerator;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.processor.ModelProcessor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;


/**
 * @author rsaccon
 * 
 */
public class SyncModelProcessor extends ModelProcessor {

	/**
	 * @param annotationTypeDeclarations
	 * @param env
	 */
	public SyncModelProcessor(
			Set<AnnotationTypeDeclaration> annotationTypeDeclarations,
			AnnotationProcessorEnvironment env) {
		super(annotationTypeDeclarations, env);
	}

	@Override
	protected ModelMetaDescFactory createModelMetaDescFactory(
			AttributeMetaDescFactory attributeMetaDescFactory) {
		return new SyncModelMetaDescFactory(env, attributeMetaDescFactory);
	}

	@Override
	protected ModelMetaGenerator createModelMetaGenerator(
			ModelMetaDesc modelMetaDesc) {
//		SyncData syncData = new SyncData();
//		syncData.setEnabled(true);
//		modelMetaDesc.setData("sync", syncData);
		return new SyncModelMetaGenerator(modelMetaDesc);
	}
	

	@Override
	protected AttributeMetaDescFactory createAttributeMetaDescFactory() {
		return new SyncAttributeMetaDescFactory(env);
	}
}