<?php
 
class DB_Connect {
 
    // constructor
    function __construct() {
 
    }
 
    // destructor
    function __destruct() {
        // $this->close();
    }
 
    // Connecting to database
    public function connect() {
        require_once 'config.php';
        // connecting to mysql
        $con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
	
	//Debug Exception Handler
	if ($con->connect_error)
	{
	    throw new Exception(
	       sprintf('(#%d) %s', $con->connect_errorno,
			$con->connect_error)
	    );
		return "error";
	}
        // return database handler
        return $con;
    }
 
    // Closing database connection
    public function close() {
        mysqli_close();
    }
 
} 
?>
