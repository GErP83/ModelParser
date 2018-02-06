package com.gerp83.modelparsertest.model;

import com.gerp83.modelparser.ModelClass;
import com.gerp83.modelparser.ModelParser;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by GErP83
 */

public class ColorCode implements ModelClass{

    private ArrayList<Integer> rgba;
    private String hex;

    public ColorCode(JSONObject object) {
        ModelParser.parseAll(this, object);
    }

    public ArrayList<Integer> getRgba() {
        return rgba;
    }

    public void setRgba(ArrayList<Integer> rgba) {
        this.rgba = rgba;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("rgba: ");
        if(rgba != null) {
            for(Integer i : rgba){
                builder.append( i + ", ");
            }
        }
        builder.append("\n");
        builder.append("hex: ").append(hex).append("\n");
        return builder.toString();
    }

}
