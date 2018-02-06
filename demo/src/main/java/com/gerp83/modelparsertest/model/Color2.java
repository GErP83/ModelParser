package com.gerp83.modelparsertest.model;

import com.gerp83.modelparser.ModelClass;
import com.gerp83.modelparser.ModelParser;

import org.json.JSONObject;

/**
 * Created by GErP83
 */

public class Color2 implements ModelClass {

    private String color;
    private String category;
    private boolean bool;
    private String type;
    private ColorCode code;

    public Color2(JSONObject object) {

        ModelParser.parse(this, object, "color", "color");
        ModelParser.parse(this, object, "category", "long_named_category");
        ModelParser.parse(this, object, "bool", "boolean_is_bool_if_short");
        ModelParser.parse(this, object, "type", "type_that_is_too_long_here");
        ModelParser.parse(this, object, "code", "code_array");

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ColorCode getCode() {
        return code;
    }

    public void setCode(ColorCode code) {
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("color: ").append(color).append("\n");
        builder.append("category: ").append(category).append("\n");
        builder.append("bool: ").append(bool).append("\n");
        builder.append("type: ").append(type).append("\n");

        if (code != null) {
            builder.append("colorCode: ").append("\n").append(code.toString()).append("\n");
        }
        return builder.toString();
    }

}
