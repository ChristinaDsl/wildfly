package org.jboss.as.jpa.util;

import org.jboss.as.jpa.messages.JpaLogger;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;

import java.util.ArrayList;

public class ImportsInspector {

    private static final String JAVA_X_PERSISTENCE = "javax.persistence";
    private final CompositeIndex compositeIndex;
    private ArrayList<DotName> imports;

    public ImportsInspector(CompositeIndex compositeIndex) {
        this.compositeIndex = compositeIndex;
    }

    public void processImports() {
        gatherAllImports();
        importsBelongToJavax();
    }

    private void gatherAllImports() {
        imports = new ArrayList<>();

        for (Index index : compositeIndex.getIndexes()) {
            for (ClassInfo myClass : index.getKnownClasses()) {
                for (AnnotationInstance annotation : myClass.annotations()) {
                    imports.add(annotation.name());
                }
            }
        }
    }

    private void importsBelongToJavax() {
        for (DotName annotation : imports) {
            if (annotation.toString().startsWith(JAVA_X_PERSISTENCE)) {
                JpaLogger.ROOT_LOGGER.containsJavaxPersistenceImports();
                return;
            }
        }
    }
}