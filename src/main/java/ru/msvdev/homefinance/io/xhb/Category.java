package ru.msvdev.homefinance.io.xhb;

import lombok.Data;

@Data
public class Category {

    private String name;
    private String parent;

    @Override
    public String toString() {
        return parent == null ? name : String.format("%s (%s)", parent, name);
    }
}
