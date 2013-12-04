<?php

class DB_Functions extends mysqli {

    private $db;	//connect to database
    private $con;	//establish connection

    //put your code here
    // constructor
    function __construct() {
        include_once 'db_connect.php';
        // connecting to database
        $this->con = new DB_Connect();
        $this->db = $this->con->connect();
        //debug exceptions
        if ($this->db == "error"){
	  return false;
        }
    }

    // destructor
    function __destruct() {
        $this->db->close();
    }
    
     
    /**
    * Function To create/Assign a New Nursery On the Database
    *	Adds Admin User too
    **/
    public function newStory($title, $full_text, $source_text, $category, $source_url, $img_url, $date_published){
	//database connection declaration:
	$db = $this->db;
/* change character set to utf8 */
	if (!$db->set_charset("utf8")) {
	    //printf("Error loading character set utf8: %s\n", $db->error);
	} else {
	   // printf("Current character set: %s\n", $db->character_set_name());
	}
	$sql = ("INSERT INTO articles (id, title, full_text, source_text, category, source_url, img_url, date_published) VALUES(NULL, '$title', '$full_text', '$source_text', '$category', '$source_url', '$img_url', '$date_published')");

	$result = mysqli_query($db, $sql);
	
	if ($result){
	   //Get New Nursery ID and assign that to new admin user
	      return "success";
	} else {
	   return "Something Went Wrong: \n".mysqli_error($db);
	}
	return false;
	
    }
   
    
    /**
    *	API Data Requests Class;
    *	Handles data read requests and replies with a json array	
    **/
    public function listTables ($table_name) {
	$json = array();
	$db = $this->db;
	/* change character set to utf8 */
	if (!$db->set_charset("utf8")) {
	    //printf("Error loading character set utf8: %s\n", $db->error);
	} else {
	   // printf("Current character set: %s\n", $db->character_set_name());
	}
	$sql = ("SELECT * FROM articles ORDER BY date_published DESC");
	$result = mysqli_query($db, $sql);
	
	switch ($table_name){
	    case anything:
	      while($row = mysqli_fetch_array($result))
	      {
		  //$bus = array();
		  $bus = array(
		      'id' => $row['id'],
		      'title' => $row['title'],
		      'full_text' => $row['full_text'],
		      'source_text' => $row['source_text'],   
		      'category' => $row['category'],
		      'source_url' => $row['source_url'],
		      'img_url' => $row['img_url'],
		      'date_published' => 'date: ' . $row['date_published']
		  );
		  array_push($json, $bus);
	      }
	      $jsonstring = json_encode($json);
	      return $jsonstring;
	      break;
	}
	
	return true;
    }
    
   

}

?>
