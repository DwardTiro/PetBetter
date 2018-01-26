<?php

  define('DB_NAME', 'petbetter');
  define('DB_USER', 'root');
  define('DB_PASSWORD', '');
  define('DB_HOST', 'localhost');

  $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die ('unable to connect');

  $mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

  if($mysqli->connect_errno) {
    printf("Connect Failed: %s\n", $mysqli->connect_errno);
  }
  
  if($con){
	  //echo "Connection Successful.";
  }
  else{
	  echo "Connection Failed.";
  }



 ?>
