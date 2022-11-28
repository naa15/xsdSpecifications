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
}
