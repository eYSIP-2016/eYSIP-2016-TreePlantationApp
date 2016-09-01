package org.abhyuday.treeplantation.PlantByOrg.PlantingTreeModule;

/**
 * Created by shreyansh on 24/5/16.
 */
public class PlantingTreeConfig {
    public static final String INSERT_URL = "http://10.129.139.139:9898/insertInPlantedTrees.php";
    //public static final String INSERT_URL = "http://192.168.56.1/androidPHP/insertInPlantedTrees.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_TREE_ID = "treeId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_TREE_NAME = "treeName";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";


    //If server response is equal to this that means request is unsuccessful
    public static final String MESSAGE_ERROR = "Error";

    //If server response is equal to this that means request is successful
    public static final String MESSAGE_SUCCESS = "Success";



}
