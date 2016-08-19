package org.jsp.generator;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Jayesh S. Parkariya on 08-08-2016.
 * <p/>
 * Android Generator is open source project that reduce the time to define view id from xml layout
 * file to java file. Its automatically identify the id from layout file and define the class member
 * respect to that id and also write the findViewById code into java file by apply just two annotation.
 */
public class Generator {

    /**
     * @param aClass A class where fields and methods need to generate
     */
    private static File getOutputFile(Class<?> aClass) {
        return new File("app\\src\\main\\java\\" + aClass.getName().replace(".", "\\") + ".java");
    }

    /**
     * @param fileName Its must be xml file that's from resource file from layout
     */
    private static File getInputFile(String fileName) {
        return new File("app\\src\\main\\res\\layout\\" + fileName + ".xml");
    }

    /**
     * @param mClass A class of activity where code need to generate
     */
    public static void generateForActivity(Class<? extends Activity> mClass) throws Exception {

        //Get the @Field annotation from following class
        Field field = getFieldAnnotation(mClass);
        Set<String> layouts = new HashSet<>();
        Collections.addAll(layouts, field.layout());

        //Get the @FindViewById annotation from following class
        Method method = getFindViewByIdAnnotation(mClass);
        HashMap<String, List<Variables>> mMap = generateCodeFromXmlForActivity(layouts);
        String allFields = getAllFields(mMap, field.fieldType().toString());
        String allFindViewById = getFieldInitialization(mMap);
        writeGeneratedCode(mClass, allFields, allFindViewById, method.getName());
    }

    /**
     * @param mClass Is a class of Fragment where findViewById operation perform respect to them.
     */
    public static void generateForV4Fragment(Class<? extends android.support.v4.app.Fragment> mClass) throws Exception {

        //Get the @Field annotation from following class
        Field field = getFieldAnnotation(mClass);
        Set<String> layouts = new HashSet<>();
        Collections.addAll(layouts, field.layout());

        //Get the @FindViewById annotation from following class
        Method method = getFindViewByIdAnnotation(mClass);
        HashMap<String, List<Variables>> mMap = generateCodeFromXmlForFragment(layouts);
        String allFields = getAllFields(mMap, field.fieldType().toString());
        String allFindViewById = getFieldInitialization(mMap);
        writeGeneratedCode(mClass, allFields, allFindViewById, method.getName());
    }

    /**
     * @param mClass Is a class of Fragment where findViewById operation perform respect to them.
     */
    public static void generateForAppFragment(Class<? extends Fragment> mClass) throws Exception {

        //Get the @Field annotation from following class
        Field field = getFieldAnnotation(mClass);
        Set<String> layouts = new HashSet<>();
        Collections.addAll(layouts, field.layout());

        //Get the @FindViewById annotation from following class
        Method method = getFindViewByIdAnnotation(mClass);
        HashMap<String, List<Variables>> mMap = generateCodeFromXmlForFragment(layouts);
        String allFields = getAllFields(mMap, field.fieldType().toString());
        String allFindViewById = getFieldInitialization(mMap);
        writeGeneratedCode(mClass, allFields, allFindViewById, method.getName());
    }

    /**
     * @param mClass Check Field annotation is available or not if available then return true else false
     */
    private static boolean hasFieldAnnotation(Class<?> mClass) {
        return mClass.isAnnotationPresent(Field.class);
    }

    /**
     * @param mClass Check FindViewById annotation to get method where findViewById code need
     *               to generate respect to that class field
     */
    private static Method getFindViewByIdAnnotation(Class<?> mClass) throws Exception {
        Method[] methods = mClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(FindViewById.class)) {
                return method;
            }
        }
        throw new Exception("@FindViewById annotation not found exception");
    }

    /**
     * @param mClass Try to manipulate Field annotation if available otherwise throw Exception
     * @throws Exception @Field annotation not found exception
     */
    private static Field getFieldAnnotation(Class<?> mClass) throws Exception {
        if (hasFieldAnnotation(mClass)) {
            return mClass.getAnnotation(Field.class);
        } else {
            throw new Exception("@Field annotation not found exception");
        }
    }

    /**
     * This function read the entire following java file as String its store into HasMap.
     *
     * @return return HasMap with all the lines of Java class as String.
     */
    private static HashMap<Integer, String> getClassFile(Class<?> aClass) throws FileNotFoundException {
        HashMap<Integer, String> lines = new HashMap<>();
        Scanner scanner = new Scanner(getOutputFile(aClass), "UTF-8");
        int index = 0;
        while (scanner.hasNext()) {
            lines.put(index, scanner.nextLine());
            index++;
        }
        return lines;
    }

    /**
     * This function going to read the all the xml layout files from the following directory
     * app\src\main\res\layout\ and manipulate it fetch the all the fields and generate the
     * fields initialization according to Activity`s fields initialization and those are the save
     * into HasMap
     *
     * @param layouts Its array of xml layout files
     * @return return the HasMap that contains the metadata of fields and its initialization.
     */
    private static HashMap<String, List<Variables>> generateCodeFromXmlForActivity(Set<String> layouts) throws Exception {

        HashMap<String, List<Variables>> mMap = new HashMap<>();
        for (String layout : layouts) {
            try {
                File inputFile = getInputFile(layout);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("*");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    Element eElement = (Element) nNode;
                    if (eElement.hasAttribute("android:id")) {
                        if (mMap.containsKey(eElement.getTagName())) {
                            mMap.get(eElement.getTagName())
                                    .add(new Variables(eElement.getAttribute("android:id").replace("@+id/", ""),
                                            eElement.getAttribute("android:id").replace("@+id/", "")
                                                    + " = "
                                                    + "(" + eElement.getTagName() + ") "
                                                    + "findViewById(R.id." + eElement.getAttribute("android:id").replace("@+id/", "") + ");"));
                        } else {
                            List<Variables> mList = new ArrayList<>();
                            mList.add(new Variables(eElement.getAttribute("android:id").replace("@+id/", ""),
                                    eElement.getAttribute("android:id").replace("@+id/", "")
                                            + " = "
                                            + "(" + eElement.getTagName() + ") "
                                            + "findViewById(R.id." + eElement.getAttribute("android:id").replace("@+id/", "") + ");"));
                            mMap.put(eElement.getTagName(), mList);
                        }
                    }
                }
            } catch (SAXException | IOException e) {
                throw new Exception("Resource " + layout + " not found exception");
            }
        }
        return mMap;
    }

    /**
     * This function going to read the all the xml layout files from the following directory
     * app\src\main\res\layout\ and manipulate it fetch the all the fields and generate the
     * fields initialization according to Fragment`s fields initialization and those are the save
     * into HasMap
     *
     * @param layouts Its array of xml layout files
     * @return return the HasMap that contains the metadata of fields and its initialization.
     */
    private static HashMap<String, List<Variables>> generateCodeFromXmlForFragment(Set<String> layouts) throws Exception {

        HashMap<String, List<Variables>> mMap = new HashMap<>();
        for (String layout : layouts) {
            try {
                File inputFile = getInputFile(layout);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("*");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    Element eElement = (Element) nNode;
                    if (eElement.hasAttribute("android:id")) {
                        if (mMap.containsKey(eElement.getTagName())) {
                            mMap.get(eElement.getTagName())
                                    .add(new Variables(eElement.getAttribute("android:id").replace("@+id/", ""),
                                            eElement.getAttribute("android:id").replace("@+id/", "")
                                                    + " = "
                                                    + "(" + eElement.getTagName() + ") "
                                                    + "view.findViewById(R.id." + eElement.getAttribute("android:id").replace("@+id/", "") + ");"));
                        } else {
                            List<Variables> mList = new ArrayList<>();
                            mList.add(new Variables(eElement.getAttribute("android:id").replace("@+id/", ""),
                                    eElement.getAttribute("android:id").replace("@+id/", "")
                                            + " = "
                                            + "(" + eElement.getTagName() + ") "
                                            + "view.findViewById(R.id." + eElement.getAttribute("android:id").replace("@+id/", "") + ");"));
                            mMap.put(eElement.getTagName(), mList);
                        }
                    }
                }
            } catch (SAXException | IOException e) {
                throw new Exception("Resource " + layout + " not found exception");
            }
        }
        return mMap;
    }

    /**
     * This function going to generate the fields initialization in single line.
     *
     * @param hashMap Its contains the metadata of fields initialization.
     * @return This function return the mata data of fields initialization into single line of string.
     */
    private static String getAllFields(HashMap<String, List<Variables>> hashMap, String fieldType) {
        String lines = "";
        for (String key : hashMap.keySet()) {
            String variables = "\n\t" + (fieldType.equalsIgnoreCase(FieldTypes.Default.toString()) ? "" : fieldType.toLowerCase()) + " " + key + " ";
            for (Variables v : hashMap.get(key)) {
                variables += v.getField() + ", ";
            }
            lines += variables.substring(0, variables.length() - 2) + ";";
        }
        return lines;
    }

    /**
     * This function going to generate the fields initialization in single line.
     *
     * @param hashMap Its contains the metadata of fields initialization.
     * @return This function return the mata data of fields initialization into single line of string.
     */
    private static String getFieldInitialization(HashMap<String, List<Variables>> hashMap) {
        String lines = "";
        for (String key : hashMap.keySet()) {
            for (Variables v : hashMap.get(key)) {
                lines += "\n\t\t" + v.getFindViewById();
            }
        }
        return lines;
    }

    /**
     * This function responsible to write or update the generated code into following java class.
     *
     * @param mClass              Is java class where generated code need to write or update
     * @param fields              Is contains all the metadata of fields that going to declared as
     *                            class members.
     * @param fieldInitialization Is contains all the metadata of fields initialization
     * @param where               Is place of fields initialization where findViewById operation going to take place.
     */
    private static void writeGeneratedCode(Class<?> mClass, @NonNull String fields, @NonNull String fieldInitialization, @NonNull String where) throws FileNotFoundException, UnsupportedEncodingException {

        HashMap<Integer, String> lines = getClassFile(mClass);

        /*Write the fields*/
        if (needToUpdateField(lines)) {
            System.err.println("Updating fields...");
            boolean startToDelete = false;
            Iterator<Map.Entry<Integer, String>> iterator = lines.entrySet().iterator();
            int updateAt = 0;
            String line = "";
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                line = entry.getValue();
                updateAt = entry.getKey();
                if (!startToDelete && line.contains("Declaration")) {
                    iterator.remove();
                    startToDelete = true;
                } else if (line.contains("EndDeclaration")) {
                    line = Field.startIndicator + fields + "\n" + line;
                    break;
                } else if (startToDelete) {
                    iterator.remove();
                }
            }
            lines.put(updateAt, line);
        } else {
            System.err.println("Generating fields...");
            boolean searchEnd = false;
            for (int key : lines.keySet()) {
                if (lines.get(key).contains(mClass.getSimpleName()) && lines.get(key).contains("{") && lines.get(key).contains("}")) {
                    String field = "\n" + Field.startIndicator + fields + Field.endIndicator;
                    lines.put(key, lines.get(key).replace("}", field + "\n}"));
                    break;
                } else if (lines.get(key).contains(mClass.getSimpleName()) && lines.get(key).contains("{")) {
                    String field = lines.get(key) + "\n" + Field.startIndicator + fields + Field.endIndicator;
                    lines.put(key, field);
                    break;
                } else if (lines.get(key).contains(mClass.getSimpleName())) {
                    searchEnd = true;
                } else if (searchEnd && lines.get(key).contains("{")) {
                    String field = lines.get(key) + "\n" + Field.startIndicator + fields + Field.endIndicator;
                    lines.put(key, field);
                    break;
                }
            }
        }

        /*Write the field initialization*/
        if (needToUpdateFindViewById(lines)) {
            System.err.println("Updating FindViewById...");
            boolean startToDelete = false;
            Iterator<Map.Entry<Integer, String>> iterator = lines.entrySet().iterator();
            int updateAt = 0;
            String line = "";
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                line = entry.getValue();
                updateAt = entry.getKey();
                if (!startToDelete && line.contains("Initialization")) {
                    iterator.remove();
                    startToDelete = true;
                } else if (line.contains("EndInitialization")) {
                    line = FindViewById.startIndicator + fieldInitialization + "\n" + line;
                    break;
                } else if (startToDelete) {
                    iterator.remove();
                }
            }
            lines.put(updateAt, line);

        } else {
            System.err.println("Generating FindViewById...");
            boolean searchEnd = false;
            for (int key : lines.keySet()) {
                if (lines.get(key).contains(where) && lines.get(key).contains("{") && lines.get(key).contains("}")) {
                    String fieldInitializations = "\n" + FindViewById.startIndicator + fieldInitialization + FindViewById.endIndicator;
                    lines.put(key, lines.get(key).replace("}", fieldInitializations + "\n\t}"));
                    break;
                } else if (lines.get(key).contains(where) && lines.get(key).contains("{")) {
                    String fieldInitializations = lines.get(key) + "\n" + FindViewById.startIndicator + fieldInitialization + FindViewById.endIndicator;
                    lines.put(key, fieldInitializations);
                    break;
                } else if (lines.get(key).contains(where)) {
                    searchEnd = true;
                } else if (searchEnd && lines.get(key).contains("{")) {
                    String field = lines.get(key) + "\n" + FindViewById.startIndicator + fields + FindViewById.endIndicator;
                    lines.put(key, field);
                    break;
                }
            }
        }
        PrintWriter writer = new PrintWriter(getOutputFile(mClass), "UTF-8");
        for (int key : lines.keySet()) {
            writer.println(lines.get(key));
        }
        writer.close();
        System.out.println("****Your code generated successfully****");
    }

    /**
     * This function going to check weather fields need to update or not if field need to update
     * then this function return true else false
     *
     * @param lines is the string lines of file as HasMap.
     * @return return true if field need to update then this function return true else false
     */
    private static boolean needToUpdateField(HashMap<Integer, String> lines) {
        for (int key : lines.keySet()) {
            if (lines.get(key).contains("Declaration")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function going to check weather field initialization need to update or not if field
     * initialization need to update then this function return true else false
     *
     * @param lines is the string lines of file as HasMap.
     * @return return true if field initialization need to update else false.
     */
    private static boolean needToUpdateFindViewById(HashMap<Integer, String> lines) {
        for (int key : lines.keySet()) {
            if (lines.get(key).contains("Initialization")) {
                return true;
            }
        }
        return false;
    }

}