package org.example;

public class PaymentRequestPayload {
    private int lvl;
    private String name;
    private String tag="";
    private String occur;
    private String type="";
    private String code="";
    private String validationRule="";
    private String path;
    private String definition;
    private String fieldDefinition;

    public PaymentRequestPayload() {

    }


    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOccur(String occur) {
        this.occur = occur;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCode() {
        return code;
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
                ", code='" + code + '\'' +
                ", validationRule='" + validationRule + '\'' +
                ", path='" + path + '\'' +
                ", definition='" + definition + '\'' +
                ", fieldDefinition='" + fieldDefinition + '\'' +
                '}';
    }

}
