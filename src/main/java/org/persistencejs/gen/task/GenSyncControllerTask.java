package org.persistencejs.gen.task;

import java.util.StringTokenizer;

import org.persistencejs.gen.generator.SyncControllerGenerator;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.task.GenControllerTask;
import org.slim3.gen.task.WebConfig;

public class GenSyncControllerTask extends GenControllerTask {
	
    /** the modelDefinition */
    protected String modelDefinition;
    
    private String modelClassName;
    private String rootPackageName;
    
    /**
     * Sets the modelDefinition.
     * 
     * @param modelDefinition
     *            the modelDefinition to set
     */
    public void setModelDefinition(String modelDefinition) {
        this.modelDefinition = modelDefinition;
    }
    
    public void doExecute() throws Exception {
        modelClassName = parseInput(modelDefinition);
        WebConfig config = createWebConfig();
        StringBuilder buf = new StringBuilder();
        buf.append(config.getRootPackageName());
        rootPackageName = buf.toString();
        super.doExecute();
    }
	
    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator createControllerGenerator(ControllerDesc controllerDesc) {
        return new SyncControllerGenerator(controllerDesc, modelClassName, rootPackageName);
    }
    
    /**
     * 
     * @param input
     *            the input
     * @return the parsed text.
     */
    private String parseInput(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        int count = tokenizer.countTokens();
        if (count == 1) {
        	return tokenizer.nextToken();
        }
        throw new RuntimeException(MessageFormatter.getSimpleMessage(
            MessageCode.SLIM3GEN0013,
            input,
            count));
    }
}
