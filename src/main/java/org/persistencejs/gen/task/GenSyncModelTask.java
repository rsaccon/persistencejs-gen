package org.persistencejs.gen.task;

import org.persistencejs.gen.generator.SyncModelGenerator;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.task.GenModelTask;

public class GenSyncModelTask extends GenModelTask {
	
    /**
     * Creates a {@link Generator}.
     * 
     * @param modelDesc
     *            the model description
     * @return a generator
     */
    protected Generator createModelGenerator(ModelDesc modelDesc) {
        return new SyncModelGenerator(modelDesc);
    }

}
