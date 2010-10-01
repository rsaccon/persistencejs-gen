package org.persistencejs.gen.task;

import org.persistencejs.gen.generator.SyncControllerGenerator;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.task.GenControllerTask;

public class GenSyncControllerTask extends GenControllerTask {
	
    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator createControllerGenerator(ControllerDesc controllerDesc) {
        return new SyncControllerGenerator(controllerDesc);
    }
}
