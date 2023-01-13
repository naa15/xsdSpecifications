package org.example;

import org.apache.xmlbeans.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {

    private static ArrayList<PaymentRequestPayload> csvInfo = new ArrayList<PaymentRequestPayload>();

    private static ArrayList<String> results = new ArrayList<String>();
    private static ArrayList<PaymentRequestPayload> treeFromCSV = new ArrayList<PaymentRequestPayload>();
    private static ArrayList<PaymentRequestPayload> treeFromXSD = new ArrayList<PaymentRequestPayload>();
    public static void main(String[] args) throws XmlException, IOException {
        String filename = "new-Payment Request Payload.csv";
        readCSVfile(filename);

        buildTree();
//        printTree(treeFromCSV, "");

        String xsdFile = "pain.001.001.09.xsd";
        processXSDFile(xsdFile);

//        printTree(treeFromXSD, "");
//        printTree(treeFromCSV, "");
//        readXSDFile(xsdFile);

//        compareTrees(treeFromCSV.get(0), treeFromXSD.get(0));
//        String outputFile = "output.txt";
//        writeInFile(outputFile);
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
                for (int i = 0; i < results.size(); i++) {
                    myWriter.write(results.get(i));
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
                PaymentRequestPayload p = new PaymentRequestPayload();
                String lvl = myReader.next();
                p.setLvl(Integer.valueOf(lvl));
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

//    private static void readXSDFile(String xsdFile) throws XmlException, IOException {
//        SchemaTypeSystem sts = XmlBeans.compileXsd(
//                new XmlObject[] { XmlObject.Factory.parse(new File(xsdFile)) }, XmlBeans.getBuiltinTypeSystem(), null);
//
//        List allSeenTypes = new ArrayList();
//        allSeenTypes.addAll(Arrays.asList(sts.documentTypes()));
//        allSeenTypes.addAll(Arrays.asList(sts.attributeTypes()));
//        allSeenTypes.addAll(Arrays.asList(sts.globalTypes()));
//
//        for (int i = 0; i < allSeenTypes.size(); i++)
//        {
//            SchemaType sType = (SchemaType) allSeenTypes.get(i);
//
//            if(sType.getEnumerationValues() != null) {
//                for (int j = 0; j < sType.getEnumerationValues().length; j++) {
//                    String value = sType.getEnumerationValues()[j].getStringValue();
//
//                    PaymentRequestPayload p = new PaymentRequestPayload();
//                    p.setValue(value);
//
//                    if (! listContains(p)) {
//                        String result = "Enum value " + value + " of type " + formatName(sType.getName().toString()) + " is not in the CSV file ";
//                        results.add(result);
//                    }
//                }
//            }
//
//            if(sType.getProperties() != null) {
//                for (int j = 0; j < sType.getProperties().length; j++) {
//                    String name = formatName(sType.getProperties()[j].getName().toString());
//
//                    PaymentRequestPayload p = new PaymentRequestPayload();
//                    p.setTag(name);
//
//                    if (!listContains(p)) {
//                        String result = "";
//                        if (sType.getProperties()[j].getContainerType().getName() != null) {
//                            result = "Tag " + name + " of complexType " + formatName(sType.getProperties()[j].getContainerType().getName().toString()) + " is not in the CSV file ";
//                        } else {
//                            result = "Tag " + name + " is not in the CSV file ";
//                        }
//                        results.add(result);
//                    }
//                }
//            }
//
//            allSeenTypes.addAll(Arrays.asList(sType.getAnonymousTypes()));
//        }
//    }

    private static String formatName(String name) {
        return "<" + name + ">";
    }

    private static boolean listContains(PaymentRequestPayload p) {
        for (int i = 0; i < csvInfo.size(); i++) {
            PaymentRequestPayload q = csvInfo.get(i);
            String tag = q.getTag();

            if(! tag.equals("") && ! p.getTag().isEmpty() && p.getTag() != null) {
                if (tag.equals(p.getTag())) return true;
            } else if (tag.equals("") && ! p.getValue().isEmpty()){
                if (q.getValue().equals(p.getValue())) return true;
            }
        }
        return false;
    }

    public static void buildTree () {
        int depth = getDepth();
        ArrayList<Integer> indexes = new ArrayList<Integer>(depth+1);
        for (int i = 0; i < depth + 1; i++) {
            indexes.add(0);
        }
        int currLevel = -1;

        for (int i = 0; i < csvInfo.size(); i++) {
            PaymentRequestPayload current = csvInfo.get(i);
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

    public static void printTree(ArrayList<PaymentRequestPayload> arr, String tab) {
        for (int i = 0; i < arr.size(); i++) {
//            if (! arr.get(i).getTag().equals("") && ! arr.get(i).getTag().isEmpty() && arr.get(i).getTag() != null) {
//                System.out.println(tab + arr.get(i).getTag());
//            } else {
//                System.out.println(tab + arr.get(i).getValue());
//            }
            System.out.println(tab + arr.get(i).getTag() + " " + arr.get(i).getType() + " " + arr.get(i).getOccur());
            printTree(arr.get(i).getChildren(), tab + "\t");
        }
    }
    public static int getDepth () {
        int max = -1;
        int curLevel = -1;
        for (int i = 0; i < csvInfo.size(); i++) {
            curLevel = csvInfo.get(i).getLvl();
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

            PaymentRequestPayload p = new PaymentRequestPayload();
            p.setTag('<' + globalElement.getName().getLocalPart() + '>');
            p.setParent(null);
            p.setLvl(-1);
            if (type.isSimpleType()) {
                p.setType(type.getName().getLocalPart());
            }
            treeFromXSD.add(p);

            processType(type, 0, p);
        }
    }

    private static void processType(SchemaType type, int level, PaymentRequestPayload parent)
    {
        if (type.getEnumerationValues() != null)
        {
            for (int i = 0; i < type.getEnumerationValues().length; i++){
                PaymentRequestPayload p = new PaymentRequestPayload();
                p.setValue(type.getEnumerationValues()[i].getStringValue());
                p.setParent(parent);
                p.setLvl(level);
                parent.addChild(p);
            }
        }

        for (int i = 0; i < type.getProperties().length; i++)
        {
            SchemaProperty property = type.getProperties()[i];

            PaymentRequestPayload p = new PaymentRequestPayload();
            p.setTag('<' + property.getName().getLocalPart() + '>');
            p.setParent(parent);
            p.setLvl(level);

            if(property.getType().isSimpleType()) {
                SchemaType stype = property.getType();

//                System.out.println(stype.getName().getLocalPart());
                String baseType = stype.getPrimitiveType().getName().getLocalPart();

//                System.out.println(baseType);
                if (stype.getPatterns().length > 0) {
                    String primitiveType = stype.getPatterns()[0];
                    p.setType(primitiveType);
                } else if (baseType.equals("boolean")) {
                    p.setType(baseType);
                } else {
                    p.setType(stype.getName().getLocalPart());
//                    System.out.println(stype.getName().getLocalPart());
                }
            }
            p.setOccur("[" + property.getMinOccurs() + ", " + property.getMaxOccurs() + "]" );

            parent.addChild(p);
            processType(property.getType(), level + 1, p);
        }
    }

    private static boolean compareNodes (PaymentRequestPayload p, PaymentRequestPayload q) {
//        if (p.getTag().equals(q.getTag()) && p.getValue().equals(q.getValue()) && p.getType().equals(q.getType())) return true;
        if (p.getTag().equals(q.getTag()) && p.getMaxOccurs() == q.getMaxOccurs() && p.getMinOccurs() == q.getMinOccurs()
            && p.getValue().equals(q.getValue())) return true;
        System.out.println("csv: " + p);
        System.out.println("xsd: " + q);
        System.out.println("Error");
        return false;
    }
    private static void compareChildren (PaymentRequestPayload p, PaymentRequestPayload q) {
        if (p == null || q == null) return;

        ArrayList<PaymentRequestPayload> childrenP = p.getChildren();
        ArrayList<PaymentRequestPayload> childrenQ = q.getChildren();

        if (childrenP.size() == childrenQ.size()) {
            for (int i = 0; i < childrenQ.size(); i++) {
                compareTrees(childrenP.get(i), childrenQ.get(i));
            }
        } else {

        }
    }
    private static void compareTrees(PaymentRequestPayload p, PaymentRequestPayload q) {
        if (p.getTag().equals(q.getTag())) {
            compareNodes(p,q);
            compareChildren(p, q);
        } else {
            System.out.println("Element" + q.getTag() + " is not in the CSV file ");

            compareTrees(p, q.getChildren().get(0));
        }
    }

}