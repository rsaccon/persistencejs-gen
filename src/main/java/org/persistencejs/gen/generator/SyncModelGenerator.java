package org.persistencejs.gen.generator;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a model java file.
 * 
 * @author rsaccon
 * 
 */
public class SyncModelGenerator implements Generator {

    /** the model description */
    protected final ModelDesc modelDesc;

    /**
     * Creates a new {@link SyncModelGenerator}.
     * 
     * @param modelDesc
     *            the model description
     */
    public SyncModelGenerator(ModelDesc modelDesc) {
        if (modelDesc == null) {
            throw new NullPointerException("The modelDesc parameter is null.");
        }
        this.modelDesc = modelDesc;
    }

    public void generate(Printer p) {
        if (modelDesc.getPackageName().length() != 0) {
            p.println("package %s;", modelDesc.getPackageName());
            p.println();
        }
        p.println("import java.io.Serializable;");
        p.println();
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("import com.google.appengine.api.datastore.Key;");
            p.println();
            p.println("import org.slim3.datastore.Attribute;");
            p.println("import org.slim3.datastore.Model;");
            p.println();
            p.println("import org.persistencejs.Sync;");
        } else {
            p.println("import org.slim3.datastore.Model;");
            p.println();
            p.println("import %s;", modelDesc.getSuperclassName());
        }
        p.println();
        p.println("@Model(%1$s = 1)", AnnotationConstants.schemaVersion);
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("public class %s implements Serializable {", modelDesc
                .getSimpleName());
        } else {
            p.println(
                "public class %s extends %s implements Serializable {",
                modelDesc.getSimpleName(),
                ClassUtil.getSimpleName(modelDesc.getSuperclassName()));
        }
        p.println();
        p.println("    private static final long serialVersionUID = 1L;");
        p.println();
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("    @Attribute(primaryKey = true)");
            p.println("    private Key key;");
            p.println();
            p.println("    @Attribute(version = true)");
            p.println("    private Long version;");
            p.println();
            p.println("    @Sync(timestamp = true)");
            p.println("    private Long lastChange;");
            p.println();
            p.println("    @Attribute(persistent = false)");
            p.println("    private boolean dirty = false;");
            p.println();  
            p.println("    @Attribute(persistent = false)");
            p.println("    private boolean syncDirty = false;");
            p.println();
            p.println("    /**");
            p.println("     * Returns the key.");
            p.println("     *");
            p.println("     * @return the key");
            p.println("     */");
            p.println("    public Key getKey() {");
            p.println("        return key;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Sets the key.");
            p.println("     *");
            p.println("     * @param key");
            p.println("     *            the key");
            p.println("     */");
            p.println("    public void setKey(Key key) {");
            p.println("        this.key = key;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Returns the version.");
            p.println("     *");
            p.println("     * @return the version");
            p.println("     */");
            p.println("    public Long getVersion() {");
            p.println("        return version;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Sets the version.");
            p.println("     *");
            p.println("     * @param version");
            p.println("     *            the version");
            p.println("     */");
            p.println("    public void setVersion(Long version) {");
            p.println("        this.version = version;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Sets the lastChange.");
            p.println("     *");
            p.println("     * @param lastChange");
            p.println("     *            the lastChange");
            p.println("     */");
            p.println("    public void setLastChange(Long lastChange) {");
            p.println("        this.lastChange = lastChange;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Returns the lastChange.");
            p.println("     *");
            p.println("     * @return the lastChange");
            p.println("     */");
            p.println("    public Long getLastChange() {");
            p.println("        return lastChange;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Sets the dirty.");
            p.println("     *");
            p.println("     * @param dirty");
            p.println("     *            the dirty");
            p.println("     */");
            p.println("    public void setDirty(boolean dirty) {");
            p.println("        this.dirty = dirty;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Returns the dirty.");
            p.println("     *");
            p.println("     * @return the dirty");
            p.println("     */");
            p.println("    public boolean isDirty() {");
            p.println("        return dirty;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Sets the syncDirty.");
            p.println("     *");
            p.println("     * @param syncDirty");
            p.println("     *            the syncDirty");
            p.println("     */");
            p.println("    public void setSyncDirty(boolean syncDirty) {");
            p.println("        this.syncDirty = syncDirty;");
            p.println("    }");
            p.println();
            p.println("    /**");
            p.println("     * Returns the syncDirty.");
            p.println("     *");
            p.println("     * @return the syncDirty");
            p.println("     */");
            p.println("     public boolean isSyncDirty() {");
            p.println("     	return syncDirty;");
            p.println("     }");
            p.println();
            p.println();
            p.println("    @Override");
            p.println("    public int hashCode() {");
            p.println("        final int prime = 31;");
            p.println("        int result = 1;");
            p.println("        result = prime * result + ((key == null) ? 0 : key.hashCode());");
            p.println("        return result;");
            p.println("    }");
            p.println();
            p.println("    @Override");
            p.println("    public boolean equals(Object obj) {");
            p.println("        if (this == obj) {");
            p.println("            return true;");
            p.println("        }");
            p.println("        if (obj == null) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        if (getClass() != obj.getClass()) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        %1$s other = (%1$s) obj;", modelDesc
                .getSimpleName());
            p.println("        if (key == null) {");
            p.println("            if (other.key != null) {");
            p.println("                return false;");
            p.println("            }");
            p.println("        } else if (!key.equals(other.key)) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        return true;");
            p.println("    }");
        }
        p.println("}");
    }
}
