package com.example.app;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Field {
    final private String id;
    final private String value;
    List<Field> children = new LinkedList<Field>();

    public static Field create(String id, String value) {
        return new Field(id, value);
    }

    public static Field group(String id) {
        return new Field(id);
    }

    public Field(String id) {
        this(id, null);
    }

    public Field(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public Field addChild(Field field) {
        if (field != null)
            children.add(field);
        return this;
    }

    public Collection<Field> childFields() {
        return Collections.unmodifiableList(children);
    }

    public int childrenCount() {
        return children.size();
    }

    Field getChild(int index) {
        return children.get(index);
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
