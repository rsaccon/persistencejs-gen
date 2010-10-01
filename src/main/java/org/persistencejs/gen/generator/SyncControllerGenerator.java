package org.persistencejs.gen.generator;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a controller java file.
 * 
 * @author rsaccon
 * @since 1.0.0
 * 
 */
public class SyncControllerGenerator implements Generator {

    /** the controller description */
    protected final ControllerDesc controllerDesc;

    /**
     * Creates a new {@link ControllerGenerator}.
     * 
     * @param controllerDesc
     *            the controller description
     */
    public SyncControllerGenerator(ControllerDesc controllerDesc) {
        if (controllerDesc == null) {
            throw new NullPointerException(
                "The controllerDesc parameter is null.");
        }
        this.controllerDesc = controllerDesc;
    }

    public void generate(Printer p) {
        if (controllerDesc.getPackageName().length() != 0) {
            p.println("package %s;", controllerDesc.getPackageName());
            p.println();
        }
        p.println("import %s;", controllerDesc.getSuperclassName());
        p.println("import %s;", ClassConstants.Navigation);
        p.println();
        p.println("public class %s extends %s {", controllerDesc
            .getSimpleName(), ClassUtil.getSimpleName(controllerDesc
            .getSuperclassName()));
        p.println();
        p.println("    @Override");
        p.println("    public %s run() throws Exception {", ClassUtil
            .getSimpleName(ClassConstants.Navigation));
        if (controllerDesc.isUseView()) {
            p.println("        return forward(\"%s\");", controllerDesc
                .getSimpleViewName());
        } else {
            p.println("        return null;");
        }
        p.println("    }");
        p.println("}");
    }
}

