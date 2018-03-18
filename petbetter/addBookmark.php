<?php

require 'init.php';


$bookmark = json_decode(file_get_contents('php://input'),true);

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("INSERT INTO bookmarks (item_id, bookmark_type, user_id) VALUES (?,?,?)")){
		$stmt->bind_param("sss", $bookmark['item_id'], $bookmark['bookmark_type'], $bookmark['user_id']);
		$stmt->execute();
		$stmt->close();
		echo 'Bookmark Added';
		}
	else{
		echo 'SQL Query Error';
	}
}


?>