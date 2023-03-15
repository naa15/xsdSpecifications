package com.gevamu.csvParserConverter;

import java.util.ArrayList;

public class CustomType implements Comparable<CustomType>  {
    private int lvl;
    private String name;
    private String tag="";
    private String occur;
    private String type="";
    private String value="";
    private String validationRule="";
    private String path;
    private String definition;
    private String fieldDefinition;
    private String date;
    private String dateTime;
    private String choiceType;
    private String maxLength;
    private String minLength;
    private String regex;
    private CustomType parent;
    private boolean isChecked = false;
    private ArrayList<CustomType> children = new ArrayList<>();
    public CustomType() {

    }


    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setName(String name) {
        this.name = formatString(name);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOccur(String occur) {
        if(occur.length() < 5) {
            this.occur = occur;
            return;
        }

        int minOccurs;
        int maxOccurs;

        minOccurs = occur.charAt(1) - '0';

        char ch = occur.charAt(4);
        if(ch == '*') {
            maxOccurs = 62;
        } else {
            maxOccurs = occur.charAt(4) - '0';
        }

        this.occur = "[" + minOccurs + ", " + maxOccurs + "]";
    }

    public void setType(String type) {
        for (int i = 0; i < type.length(); i++) {
            String str = "" + type.charAt(i);
            if (str.equals("\n") || str.equals("\r\n") || str.equals("\r")) {
                type = type.substring(0, i) + type.substring(i+1);
                i--;
            }
        }

        this.type = type;

        if(isDate()) {
            formatDate(type);
        }
        if(isTextWithLength()) {
            formatTextType(type);
        }
        if (isChoice()) {
            this.type = "";
        }
        if(isDecimal()) {
            this.type = "DecimalNumber";
        }
    }

    public boolean isDecimal() {
        return type.length() >= 13 && (type.startsWith("0 <= decimal", 1) || type.startsWith("decimal", 1));
    }

    public boolean isBoolean() {
        return type.length()>=7 && type.equals("boolean");
    }
    public boolean isText() {
        return type.length() >= 4 && type.startsWith("text");
    }

    public boolean isTextWithLength() {
        return type.length() >= 5 && type.startsWith("text", 1);
    }
    public boolean isChoice() {
        return type.length() >= 6 && type.startsWith("Choice");
    }

    public boolean isDate() {
        return type.length() >= 4 && type.startsWith("date");
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValidationRule(String validationRule) {
        this.validationRule = validationRule;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setFieldDefinition(String fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    public int getLvl() {
        return lvl;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getOccur() {
        return occur;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getValidationRule() {
        return validationRule;
    }

    public String getPath() {
        return path;
    }

    public String getDefinition() {
        return definition;
    }

    public String getFieldDefinition() {
        return fieldDefinition;
    }

//    public int getMinOccurs() {
//        return minOccurs;
//    }
//
//    public int getMaxOccurs() {
//        return maxOccurs;
//    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public String getMinLength() {
        return minLength;
    }

    public String getRegex() {
        return regex;
    }

    public CustomType getParent() {
        return parent;
    }

    public void setParent(CustomType parent) {
        this.parent = parent;
    }

    public boolean isChecked () {
        return this.isChecked;
    }

    public void setChecked (boolean checked) {
        isChecked = checked;
    }
    public ArrayList<CustomType> getChildren() {
        return children;
    }

    public void addChild(CustomType child) {
        children.add(child);
    }
    @Override
    public String toString() {
        return "{" +
                "lvl=" + lvl +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", occur='" + occur + '\'' +
                ", type='" + type + '\'' +
                ", regex='" + regex +'\'' +
                ", value='" + value + '\'' +
//                ", validationRule='" + validationRule + '\'' +
//                ", path='" + path + '\'' +
//                ", definition='" + definition + '\'' +
//                ", fieldDefinition='" + fieldDefinition + '\'' +
//                ", minOccurrence='" + minOccurs + '\'' +
//                ", maxOccurrence='" + maxOccurs + '\'' +
                ", parent='" + parent.getTag() + '\'' +
                '}';
    }

    public String formatString(String str) {
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != ' ') {
                index = i;
                break;
            }
        }
        return str.substring(index);
    }

    public void formatDate(String type) {
        if(type.equals("dateTime")) {
            this.type = "ISODateTime";
        } else {
            this.type = "ISODate";
        }
    }

    private void formatTextType(String type) {
        StringBuilder mx = new StringBuilder();

        for (int i = 0; i < type.length(); i++) {
            if (type.charAt(i) == '{') {
                i++;
                while(type.charAt(i) != '}') {
                    if(type.charAt(i) == ',') {
                        mx = new StringBuilder();
                    } else {
                        mx.append(type.charAt(i));
                    }
                    i++;
                }
                break;
            }
        }
        maxLength = "Max" + mx + "Text";
        regex = type.substring(5,type.length()-1);

        if (regex.length() > 8) {
            this.type = regex;
        } else {
            this.type = maxLength;
        }
//        if(mx.length() > 1) {
//            this.type = maxLength;
//        } else {
//            this.type = regex;
//        }
    }

    @Override
    public int compareTo(CustomType o) {
        if(tag.equals(o.tag)) return 0;
        if (lvl >= o.lvl) return 1;
        return -1;
    }

}
