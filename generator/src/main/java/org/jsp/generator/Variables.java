package org.jsp.generator;

/**
 * Created by ANDROID-10 on 08-08-2016.
 */
public class Variables {

    private String field;
    private String findViewById;

    public Variables() {
    }

    public Variables(String field, String declaration) {
        this.field = field;
        this.findViewById = declaration;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFindViewById() {
        return findViewById;
    }

    public void setFindViewById(String findViewById) {
        this.findViewById = findViewById;
    }
}
