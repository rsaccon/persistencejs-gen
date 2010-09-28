package org.persistencejs.gen.desc;

import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;

import org.persistencejs.gen.SyncConstants;
import org.persistencejs.gen.SyncData;

/**
 * @author higayasuo
 * 
 */
public class SyncAttributeMetaDescFactory extends AttributeMetaDescFactory {

	/**
	 * @param env
	 */
	public SyncAttributeMetaDescFactory(AnnotationProcessorEnvironment env) {
		super(env);
	}

	@Override
	protected void handleField(AttributeMetaDesc attributeMetaDesc,
			ClassDeclaration classDeclaration,
			FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
		super.handleField(attributeMetaDesc, classDeclaration,
				fieldDeclaration, attribute);
		SyncData syncData = new SyncData();
		AnnotationMirror sync = DeclarationUtil.getAnnotationMirror(env,
				fieldDeclaration, SyncConstants.Sync);
		if (sync != null) {
			syncData.setEnabled(true);
			Boolean timestamp = AnnotationMirrorUtil.getElementValue(sync,
					SyncConstants.timestamp);
			if (timestamp != null) {
				syncData.setTimestamp(timestamp.booleanValue());
			}
		}
		attributeMetaDesc.setData(SyncConstants.Sync, syncData);
	}
}
