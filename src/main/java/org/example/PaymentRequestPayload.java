package org.example;

public class PaymentRequestPayload {
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
    private int minOccurs = 0;
    private int maxOccurs = 0;
    private String date;
    private String dateTime;
    private String choiceType;
    private String length;
    private String bool;

    public PaymentRequestPayload() {

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
        this.occur = occur;

        formatOccurs(occur);
    }

    public void setType(String type) {
        this.type = type;

        if(isDate()) {
            formatDate(type);
        }
        if(isChoice()) {
            formatChoiceType(type);
        }
        if(isText()) {

        }
        if(isTextWithLength()) {
            formatTextType(type);
        }
        if(isBoolean()) {

        }
    }

    public boolean isBoolean() {
        return type.length()>=7 && type.equals("boolean");
    }
    public boolean isText() {
        return type.length() >= 4 && type.substring(0,4).equals("text");
    }

    public boolean isTextWithLength() {
        return type.length() >= 5 && type.substring(1,5).equals("text");
    }
    public boolean isChoice() {
        return type.length() >= 6 && type.substring(0,6).equals("Choice");
    }

    public boolean isDate() {
        return type.length() >= 4 && type.substring(0,4).equals("date");
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

    public int getMinOccurs() {
        return minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLength() {
        return length;
    }


    @Override
    public String toString() {
        return "PaymentRequestPayload{" +
                "lvl=" + lvl +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", occur='" + occur + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", validationRule='" + validationRule + '\'' +
                ", path='" + path + '\'' +
                ", definition='" + definition + '\'' +
                ", fieldDefinition='" + fieldDefinition + '\'' +
                ", minOccurance='" + minOccurs + '\'' +
                ", maxOccurance='" + maxOccurs + '\'' +
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

    public void formatOccurs(String s) {
        if(s.length() < 5) return;
        minOccurs = s.charAt(1) - '0';

        char ch = s.charAt(4);
        if(ch == '*') {
            maxOccurs = Integer.MAX_VALUE;
        } else {
            maxOccurs = s.charAt(4) - '0';
        }
    }

    public void formatDate(String type) {
        if(type.equals("dateTime")) {
            dateTime = "ISODateTime";
        } else {
            date = "ISODate";
        }
    }

    private void formatChoiceType(String type) {
        choiceType = "Choice";
    }


    private void formatTextType(String type) {
        String res = "";

        for (int i = 0; i < type.length(); i++) {
            if (type.charAt(i) == '{') {
                i++;
                while(type.charAt(i) != '}') {
                    if(type.charAt(i) == ',') {
                        res = "";
                    } else {
                        res += type.charAt(i);
                    }
                    i++;
                }
                break;
            }
        }
        length = "Max" + res + "Text";
    }
}
