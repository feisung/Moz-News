<?php

// response json
$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
 if (isset($_GET["source"]) ){
 echo queryDB("anything");
 } else {
 //For mobile devices
echo '{ "articles":'.queryDB("anything").'}';
}

    function queryDB($query){
	  // Get Database connector
	  include_once 'config/db_functions.php';
	  $db = new DB_Functions();
	  $listTables = $db->listTables($query);
	  
	  return $listTables;
    }
    
    function searchDB($query, $extra){
	  // Get Database connector
	  include_once 'config/db_functions.php';
	  $db = new DB_Functions();
	  $result = $db->searchTable($query, $extra);
	  //$result =$extra;
	  return $result;
    }
?>