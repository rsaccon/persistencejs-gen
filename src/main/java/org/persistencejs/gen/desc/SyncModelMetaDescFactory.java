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
package org.persistencejs.gen.desc;

import java.util.ArrayList;
import java.util.List;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.desc.ModelMetaClassName;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.desc.ModelMetaDescFactory.PolyModelDesc;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Modifier;

/**
 * @author higayasuo
 * 
 */
public class SyncModelMetaDescFactory extends ModelMetaDescFactory {

	/**
	 * 
	 */
	public SyncModelMetaDescFactory(AnnotationProcessorEnvironment env,
			AttributeMetaDescFactory attributeMetaDescFactory) {
		super(env, attributeMetaDescFactory);
	}
}
