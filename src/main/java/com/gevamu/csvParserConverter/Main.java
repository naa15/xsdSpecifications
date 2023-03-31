package com.gevamu.csvParserConverter;

import org.apache.xmlbeans.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {

    private static final ArrayList<CustomType> csvInfo = new ArrayList<>();

    private static final ArrayList<String> results = new ArrayList<>();
    private static final ArrayList<String> fullOutputResults = new ArrayList<>();
    private static final ArrayList<CustomType> treeFromCSV = new ArrayList<>();
    private static final ArrayList<CustomType> treeFromXSD = new ArrayList<>();
    private static boolean skipTheRoot = false;
    public static void main(String[] args) throws XmlException, IOException {
        String filename = "new-Payment Request Payload.csv";
        String xsdFile = "pain.001.001.09.xsd";
        String outputFile = "output.txt";

        readCSVfile(filename);
        buildTree();

        //In this case we're comparing the trees in a normal way, not skipping the Document root
//        skipTheRoot = false;
//        processXSDFile(xsdFile);
//        compareTrees(treeFromCSV.get(0), treeFromXSD.get(0));

        //In this case we're skipping the Document root
        skipTheRoot = true;
        processXSDFile(xsdFile);
        compareTreesSkippingTheRootTag(treeFromCSV.get(0), treeFromXSD.get(0));

        printTreeWithErrors(treeFromCSV, "");
        writeInFile(outputFile);
    }

    private static void writeInFile(String outputFile) {
        try {
            File myObj = new File(outputFile);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            try {
                FileWriter myWriter = new FileWriter(outputFile);
//                for (String result : results) {
//                    myWriter.write(result);
//                    myWriter.write('\n');
//                }
                for (String result : fullOutputResults) {
                    myWriter.write(result);
                    myWriter.write('\n');
                }
                myWriter.close();
                System.out.println("Successfully wrote to the output file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void readCSVfile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",");
            while (myReader.hasNext()) {
                CustomType p = new CustomType();
                String lvl = myReader.next();
                p.setLvl(Integer.parseInt(lvl));
                p.setName(myReader.next());
                p.setTag(myReader.next());
                p.setOccur(myReader.next());
                String type = myReader.next();
                if(p.getTag().equals("")) {
                    p.setValue(type);
                } else {
                    while(true) {
                        String s = myReader.next();
                        if(s.length() != 0 && ! s.startsWith("-")) {
                            type = type + ',' + s;
                        } else {
                            p.setType(type);
                            p.setValidationRule(s);
                            break;
                        }
                    }
                }
                p.setPath(myReader.next());
                String definition = myReader.next();
                while(myReader.hasNext()) {
                    String s = myReader.next();
                    if(!s.endsWith("\n")) {
                        definition = definition + s;
                    } else{
                        break;
                    }
                }
                p.setDefinition(definition);

                csvInfo.add(p);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File name that was provided doesn't exist.");
            e.printStackTrace();
        }
    }

    public static void buildTree () {
        int depth = getDepth();
        ArrayList<Integer> indexes = new ArrayList<>(depth+1);
        for (int i = 0; i < depth + 1; i++) {
            indexes.add(0);
        }
        int currLevel;

        for (int i = 0; i < csvInfo.size(); i++) {
            CustomType current = csvInfo.get(i);
            currLevel = current.getLvl();

            if (currLevel == 0) {
                treeFromCSV.add(current);
            } else if (currLevel == 1) {
                treeFromCSV.get(treeFromCSV.size() - 1).addChild(current);
                current.setParent(treeFromCSV.get(treeFromCSV.size() - 1));
                indexes.set(1, i);
             } else {
                csvInfo.get(indexes.get(currLevel-1)).addChild(current);
                current.setParent(csvInfo.get(indexes.get(currLevel-1)));
                indexes.set(currLevel, i);
            }
        }
    }

    public static int getDepth () {
        int max = -1;
        int curLevel;
        for (CustomType customType : csvInfo) {
            curLevel = customType.getLvl();
            if (curLevel > max) {
                max = curLevel;
            }
        }
        return max;
    }


    public static void processXSDFile(String filename) throws XmlException, IOException
    {
        SchemaTypeSystem sts = XmlBeans.compileXsd(
                new XmlObject[] { XmlObject.Factory.parse(new File(filename)) }, XmlBeans.getBuiltinTypeSystem(),
                null);

        for (int i = 0; i < sts.globalElements().length; i++)
        {
            SchemaGlobalElement globalElement = sts.globalElements()[i];
            SchemaType type = globalElement.getType();

            CustomType p = new CustomType();
            p.setTag('<' + globalElement.getName().getLocalPart() + '>');
            p.setParent(null);
            p.setLvl(-1);
            if (! skipTheRoot) {
                p.setPath(globalElement.getName().getLocalPart());
            }
            if (type.isSimpleType()) {
                p.setType(type.getName().getLocalPart());
            }
            treeFromXSD.add(p);

            processType(type, 0, p);
        }
    }

    private static void processType(SchemaType type, int level, CustomType parent)
    {
        if (type.getEnumerationValues() != null)
        {
            for (int i = 0; i < type.getEnumerationValues().length; i++){
                CustomType p = new CustomType();
                p.setValue(type.getEnumerationValues()[i].getStringValue());
                p.setParent(parent);
                p.setOccur("");
                p.setLvl(level);
                String parentPath = parent.getPath();
                if (parentPath == null || parentPath.equals("")) {
                    p.setPath(p.getValue());
                } else {
                    p.setPath(parentPath + "/" + p.getValue());
                }
                parent.addChild(p);
            }
        }

        for (int i = 0; i < type.getProperties().length; i++)
        {
            SchemaProperty property = type.getProperties()[i];

            CustomType p = new CustomType();
            p.setTag('<' + property.getName().getLocalPart() + '>');
            p.setParent(parent);
            String parentPath = parent.getPath();
            if (parentPath == null || parentPath.equals("")) {
                p.setPath(property.getName().getLocalPart());
            } else {
                p.setPath(parentPath + "/" + property.getName().getLocalPart());
            }
            p.setLvl(level);

            if(property.getType().isSimpleType()) {
                SchemaType stype = property.getType();
                String baseType = stype.getPrimitiveType().getName().getLocalPart();
                if (stype.getPatterns().length > 0) {
                    String primitiveType = stype.getPatterns()[0];
                    p.setType(primitiveType);
                } else if (baseType.equals("boolean")) {
                    p.setType(baseType);
                } else if (baseType.equals("decimal")) {
                    p.setType("DecimalNumber");
                } else if (stype.getName().getLocalPart().endsWith("Code")) {
                    if (stype.getEnumerationValues() != null) {
                        p.setType("text");
                    } else {
                        p.setType(" text{" + stype.getFacet(1).getStringValue() + "," + stype.getFacet(2).getStringValue() + "}");
                    }
                } else {
                    p.setType(stype.getName().getLocalPart());
                }
            }
            p.setOccur("[" + property.getMinOccurs() + ", " + property.getMaxOccurs() + "]" );

            parent.addChild(p);

            if (p.getType().isEmpty()) {
                if (property.getType().getBaseType() != null) {
                    if (property.getType().getBaseType().getBaseType() != null) {
                        if(! property.getType().getBaseType().getBaseType().getName().getLocalPart().isEmpty()) {
                            String basetype = property.getType().getBaseType().getBaseType().getName().getLocalPart();
                            if (basetype.equals("decimal")) {
                                p.setType("DecimalNumber");
                            } else {
                                p.setType(basetype);
                            }
                        }
                    } else {
                        p.setType("object");
                    }
                }
            }
            processType(property.getType(), level + 1, p);
        }
    }

    private static void compareNodes (CustomType p, CustomType q) {
        String tabs = "";
        for (int i = 0; i < p.getLvl(); i++) {
            tabs += "\t";
        }
        if (! p.getTag().equals(q.getTag())) {
            results.add(tabs + q.getPath() + ": tags are different. In csv file: " + p.getTag() + " in XSD file " + q.getTag());
            p.addError("tags are different. In csv file: " + p.getTag() + " in XSD file " + q.getTag());
        }
        if (! p.getOccur().equals(q.getOccur())) {
            results.add(tabs + q.getPath() + ": " + p.getTag() + " occurrences are different, in CSV file: " + p.getOccur() + " in XSD document: " + q.getOccur());
            p.addError("occurrences are different, in CSV file: " + p.getOccur() + " in XSD document: " + q.getOccur());
        }
        if (! p.getValue().equals(q.getValue())) {
            results.add(tabs + q.getPath() + ": " + p.getTag() + " values are different, in CSV file: " + p.getValue() + " in XSD document: " + q.getValue());
            p.addError("values are different, in CSV file: \" + p.getValue() + \" in XSD document: \" + q.getValue()");
        }
        if (! p.getType().equals(q.getType())) {
            String csvType = p.getType();
            String xsdType = q.getType();
            if (! csvType.isEmpty()) {
                if (csvType.equals("text") && xsdType.equals("object")) {
                    String children = " with children: {";
                    for (int i = 0; i < q.getChildren().size(); i++) {
                        CustomType child = q.getChildren().get(i);
                        if (child.getTag().equals("")) {
                            children += child.getValue();
                        } else {
                            children += child.getTag();
                        }
                    }
                    children += '}';
                    results.add(tabs + q.getPath() + ": " + p.getTag() + " types are different, in CSV file: " + csvType + " in XSD file: " + xsdType);
                    p.addError("types are different, in CSV file: " + csvType + " in XSD file: " + xsdType + children);
                } else {
                    results.add(tabs + q.getPath() + ": " + p.getTag() + " types are different, in CSV file: " + csvType + " in XSD file: " + xsdType);
                    p.addError("types are different, in CSV file: " + csvType + " in XSD file: " + xsdType);
                }
            }
        }
    }
    private static void compareChildren (CustomType p, CustomType q) {
        if (p == null || q == null) return;

        ArrayList<CustomType> childrenP = p.getChildren();
        ArrayList<CustomType> childrenQ = q.getChildren();

        for (CustomType currentXSD : childrenQ) {
            for (CustomType currentCSV : childrenP) {
                if (! currentCSV.isChecked()) {
                    if (currentXSD.getTag().equals("") || currentXSD.getTag().isEmpty()) {
                        if (currentXSD.getValue().equals(currentCSV.getValue())) {
                            currentCSV.setChecked(true);
                            currentXSD.setChecked(true);
                            compareNodes(currentCSV, currentXSD);
                        }
                    } else if (currentXSD.getTag().equals(currentCSV.getTag())) {
                        currentCSV.setChecked(true);
                        currentXSD.setChecked(true);
                        compareNodes(currentCSV, currentXSD);
                        compareChildren(currentCSV, currentXSD);
                    }
                }
            }
//            if (! currentXSD.isChecked()) {
//                String current = currentXSD.getTag();
//                if (currentXSD.getTag().isEmpty()) {
//                    current = currentXSD.getValue();
//                }
//                String tabs = "";
//                for (int i = 0; i < p.getLvl(); i++) {
//                    tabs += "\t";
//                }
//                results.add(tabs + currentXSD.getPath() + ": In CSV file object " + p.getTag() + " doesn't have a child " + current);
//                p.addError("in CSV file object " + p.getTag() + " doesn't have a child " + current);
//            }
        }
    }
    private static void compareTrees(CustomType p, CustomType q) {
        if (p.getTag().equals(q.getTag())) {
            compareNodes(p,q);
            compareChildren(p, q);
        } else {
            results.add("Element" + q.getTag() + " is not in the CSV file ");
        }
    }

    private static void compareTreesSkippingTheRootTag(CustomType p, CustomType q) {
        if (p.getTag().equals(q.getTag())) {
            compareNodes(p,q);
            compareChildren(p, q);
        } else {
            if (q.getChildren().size() > 1) {
                results.add("Trees have a different structure and can't be compared.");
            } else {
                p.setChecked(true);
                compareTrees(p, q.getChildren().get(0));
            }
        }
    }

    /**
     * Printing tree for testing purposes
     */
     public static void printTree(ArrayList<CustomType> arr, String tab) {
         for (CustomType customType : arr) {
             if (!customType.getTag().equals("") && !customType.getTag().isEmpty() && customType.getTag() != null) {
                 System.out.println(tab + customType.getTag() + " " + customType.getType() + " " + customType.getOccur());
             } else {
                 System.out.println(tab + customType.getValue() + " " + customType.getType() + " " + customType.getOccur());
             }
             printTree(customType.getChildren(), tab + "\t");
         }
     }
    public static void printTreeWithErrors(ArrayList<CustomType> arr, String tab) {
        for (CustomType element : arr) {
            if (! element.isChecked()) {
                if (!element.getTag().equals("") && !element.getTag().isEmpty() && element.getTag() != null) {
                    fullOutputResults.add(tab + "-- " + element.getTag() + " could not be checked because its parent object was missing");
                } else {
                    fullOutputResults.add(tab + "-- " + element.getValue() + " could not be checked because its parent object was missing");
                }
            } else {
                if (!element.getTag().equals("") && !element.getTag().isEmpty() && element.getTag() != null) {
                    fullOutputResults.add(tab + element.getTag());
                    if (element.getErrors().size() > 0) {
                        for (String str : element.getErrors()) {
                            fullOutputResults.add(tab + "-- " + str);
                        }
                    }
                } else {
//                    fullOutputResults.add(tab + element.getValue());
                    if (element.getErrors().size() != 0) {
                        for (String str : element.getErrors()) {
                            fullOutputResults.add(tab + "-- "  + str);
                        }
                    }
                }
            }
            printTreeWithErrors(element.getChildren(), tab + "\t");
        }
    }
}