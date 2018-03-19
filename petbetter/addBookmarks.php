<?php

require 'init.php';

$bookmarklist = json_decode(file_get_contents('php://input'),true);


if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($bookmarklist);
	//echo $n;
	$i = 0;
	//echo $vetlist[$i]['_id'];
	
	while($i<$n){
		if($stmt = $mysqli->prepare("INSERT INTO bookmarks (item_id, bookmark_type, user_id) VALUES (?,?,?)")){
			$stmt->bind_param(
				"sss",
				$locationlist[$i]['item_id'],
				$locationlist[$i]['bookmark_type'],
				$locationlist[$i]['user_id']);
			$stmt->execute();
			$stmt->close();
			$i = $i + 1;
		}
		else{
			echo 'Failed to add to db';
			break;
		}
		
	}
	
	
}


?>