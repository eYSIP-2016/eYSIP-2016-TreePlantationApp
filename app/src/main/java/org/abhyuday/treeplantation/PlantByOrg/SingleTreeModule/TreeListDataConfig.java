package org.abhyuday.treeplantation.PlantByOrg.SingleTreeModule;

/**
 * Configuration file for PlantList.java and SingleTreeData.java
 */
public class TreeListDataConfig {

    public static final String DATA_URL = "http://10.129.139.139:9898/getTreeData.php?id=";
    //public static final String DATA_URL = "http://192.168.56.1/androidPHP/getTreeData.php?id=";
    public static final String VIEW_ALL_URL = "http://10.129.139.139:9898/getAllTreeData.php";
    //public static final String VIEW_ALL_URL = "http://192.168.56.1/androidPHP/getAllTreeData.php";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_GENUS = "genus";
    public static final String KEY_SEEDSAPLING = "SeedSapling";
    public static final String KEY_IMAGEURL = "imageurl";
    public static final String JSON_ARRAY = "result";


    //Used for passing extras between activities(intents) PlantList -> SingleTreeData -> PlantHereMap -> TreePlantedMessage
    public final static String  TREE_ID_INTENT = "treeIdIntent";

    
}
