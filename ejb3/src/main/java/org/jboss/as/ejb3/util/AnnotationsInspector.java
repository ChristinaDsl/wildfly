package org.jboss.as.ejb3.util;

import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;

import java.util.ArrayList;

public class AnnotationsInspector {

    private static final String JAVA_X_EJB = "javax.ejb";
    private final CompositeIndex compositeIndex;
    private ArrayList<DotName> annotations;

    public AnnotationsInspector(CompositeIndex compositeIndex){
        this.compositeIndex = compositeIndex;
    }

    public void processAnnotations(){
        gatherAllAnnotations();
        annotationsBelongToJavax();
    }

    private void gatherAllAnnotations(){
        annotations = new ArrayList<>();

        for (Index index : compositeIndex.getIndexes()) {
            for (ClassInfo myClass : index.getKnownClasses()) {
                for (AnnotationInstance annotation : myClass.annotations()) {
                    annotations.add(annotation.name());
                }
            }
        }
    }

    private void annotationsBelongToJavax(){
        for (DotName annotation: annotations){
            if (annotation.toString().startsWith(JAVA_X_EJB)){
                EjbLogger.DEPLOYMENT_LOGGER.containsJavaxEJBAnnotations();
                return;
            }
        }
    }
}


