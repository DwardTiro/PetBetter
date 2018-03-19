<?php

require 'init.php';

$item_id = $_POST['item_id'];
$user_id = $_POST['user_id'];
$bookmark_type = $_POST['bookmark_type'];

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

	if(!(isset($_POST['item_id']) and isset($_POST['user_id']) and isset($_POST['bookmark_type']))){
		echo 'Details are lacking';
		exit(0);
	}

	if($stmt = $mysqli->prepare("DELETE FROM bookmarks WHERE user_id = ? AND item_id = ? AND bookmark_type = ?")){
		$stmt->bind_param("sss", $user_id, $item_id, $bookmark_type);
		$stmt->execute();
		$stmt->close();
		echo 'Follower removed';
		
	}
	else{
		echo 'SQL Query Error';
	}
}



?>