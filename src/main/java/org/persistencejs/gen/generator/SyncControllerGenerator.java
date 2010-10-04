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
    
    private final String modelClassName;
    private final String rootPackageName;

    /**
     * Creates a new {@link ControllerGenerator}.
     * 
     * @param controllerDesc
     *            the controller description
     */
    public SyncControllerGenerator(ControllerDesc controllerDesc, String modelClassName, String rootPackageName) {
        if (controllerDesc == null) {
            throw new NullPointerException(
                "The controllerDesc parameter is null.");
        }
        if (modelClassName == null) {
            throw new NullPointerException(
                "The sync model ClassName is null.");
        }
        if (rootPackageName == null) {
            throw new NullPointerException(
                "The root package name is null.");
        }
        this.controllerDesc = controllerDesc;
        this.modelClassName = modelClassName;
        this.rootPackageName = rootPackageName;
    }

    public void generate(Printer p) {
        if (controllerDesc.getPackageName().length() != 0) {
            p.println("package %s;", controllerDesc.getPackageName());
            p.println();
        }
        p.println("import java.io.BufferedReader;");
        p.println("import java.io.InputStreamReader;");
        p.println("import java.util.Date;");
        p.println("import java.util.Iterator;");
        p.println();
        p.println("import %s;", controllerDesc.getSuperclassName());
        p.println("import %s;", ClassConstants.Navigation);
        p.println("import org.slim3.datastore.Datastore;");
        p.println();
        p.println("import com.google.appengine.repackaged.org.json.JSONArray;");
        p.println("import com.google.appengine.repackaged.org.json.JSONStringer;");
        p.println();
        p.println("import %1$s.meta.%2$sMeta;", rootPackageName, modelClassName);
        p.println("import %1$s.model.%2$s;", rootPackageName, modelClassName);
        p.println();
        p.println("public class %s extends %s {", controllerDesc
            .getSimpleName(), ClassUtil.getSimpleName(controllerDesc
            .getSuperclassName()));
        p.println();
        p.indent();
        p.println("@Override");
        p.println("public %s run() throws Exception {", ClassUtil
            .getSimpleName(ClassConstants.Navigation));
        p.indent();
        p.println("response.setContentType(\"application/json\");");
        p.println("response.setCharacterEncoding(\"UTF-8\");");
        p.println();
        p.println("%1$sMeta meta = %1$sMeta.get();", modelClassName);
        p.println("if (isGet()) {");   
        p.println("    Iterator<%s> mIterator =", modelClassName);
        p.println("        Datastore");
        p.println("            .query(meta)");
        p.println("            .filter(meta.lastChange.greaterThan(asLong(\"since\")))");
        p.println("            .asList()");
        p.println("            .iterator();");
        p.println();
        p.println("    JSONArray arr = new JSONArray();");
        p.println();
        p.println("    while (mIterator.hasNext()) {");
        p.println("        arr.put(meta.modelToJSON(mIterator.next()));");
        p.println("    }");
        p.println();
        p.println("    response.getWriter().write(");
        p.println("        new JSONStringer()");
        p.println("            .object()");
        p.println("            .key(\"now\")");
        p.println("            .value(new Date().getTime())");
        p.println("            .key(\"updates\")");
        p.println("            .value(arr)");
        p.println("            .endObject()");
        p.println("            .toString());");
        p.println("} else if (isPost()) {");
        p.println("    BufferedReader input =");
        p.println("        new BufferedReader(new InputStreamReader(");
        p.println("            request.getInputStream()));");
        p.println("    String str = \"\";");
        p.println("    for (String line; (line = input.readLine()) != null; str += line);");
        p.println("    input.close();");
        p.println();
        p.println("    JSONArray arr = new JSONArray(str);");
        p.println("    long timestamp = new Date().getTime();");
        p.println();
        p.println("    for (int i = 0; i < arr.length(); i++) {");
        p.println("        %s m = meta.JSONtoModel(", modelClassName);
        p.println("            arr.getJSONObject(i), timestamp);");
        p.println("        m.setSyncDirty(true);");
        p.println("        Datastore.put(m);");
        p.println("        m.setSyncDirty(false);");
        p.println("    }");
        p.println();
        p.println("    response.getWriter().write(");
        p.println("        new JSONStringer()");
        p.println("            .object()");
        p.println("            .key(\"status\")");
        p.println("            .value(\"ok\")");
        p.println("            .key(\"now\")");
        p.println("            .value(new Date().getTime())");
        p.println("            .endObject()");
        p.println("            .toString());");
        p.println("}");
        p.println();
        p.println("return null;");
        p.unindent();
        p.println("}");
        p.unindent();
        p.println("}");
    }
}

