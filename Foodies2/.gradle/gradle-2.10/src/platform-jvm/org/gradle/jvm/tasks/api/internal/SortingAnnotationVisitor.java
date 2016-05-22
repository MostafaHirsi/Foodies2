/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.jvm.tasks.api.internal;

import com.google.common.collect.Lists;
import org.objectweb.asm.AnnotationVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.ASM5;

public class SortingAnnotationVisitor extends AnnotationVisitor {

    private final List<AnnotationValue<?>> annotationValues = Lists.newLinkedList();
    private final AnnotationMember annotation;

    private SortingAnnotationVisitor parentVisitor;
    private String annotationValueName;
    private String arrayValueName;

    public SortingAnnotationVisitor(AnnotationMember parentAnnotation, AnnotationVisitor av) {
        super(ASM5, av);
        this.annotation = parentAnnotation;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationMember annotation = new AnnotationMember(desc, true);
        SortingAnnotationVisitor visitor =
            new SortingAnnotationVisitor(annotation, super.visitAnnotation(name, desc));
        visitor.parentVisitor = this;
        visitor.annotationValueName = (name == null) ? "value" : name;
        return visitor;
    }

    @Override
    public void visit(String name, Object value) {
        annotationValues.add(new SimpleAnnotationValue(name, value));
        super.visit(name, value);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        SortingAnnotationVisitor visitor = new SortingAnnotationVisitor(annotation, super.visitArray(name));
        visitor.arrayValueName = name;
        return visitor;
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        annotationValues.add(new EnumAnnotationValue(name == null ? "value" : name, value, desc));
        super.visitEnum(name, desc, value);
    }

    @Override
    public void visitEnd() {
        if (annotationValueName != null) {
            AnnotationAnnotationValue value = new AnnotationAnnotationValue(annotationValueName, annotation);
            parentVisitor.annotationValues.add(value);
            annotationValueName = null;
        } else if (arrayValueName != null) {
            ArrayAnnotationValue value = new ArrayAnnotationValue(
                arrayValueName, annotationValues.toArray(new AnnotationValue<?>[annotationValues.size()]));
            annotation.addValue(value);
            arrayValueName = null;
        }
        annotation.addValues(annotationValues);
        annotationValues.clear();
        super.visitEnd();
    }
}
