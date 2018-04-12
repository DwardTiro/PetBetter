<?php

require 'init.php';

$user_id = $_POST['user_id'];

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if($stmt = $mysqli->prepare("SELECT * FROM bookmarks WHERE user_id = ?")){
		$stmt->bind_param("s", $user_id);
		$stmt->execute();
		$stmt->bind_result($_id, $item_id, $bookmark_type, $user_id);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'item_id'=>$item_id,
				'bookmark_type'=>$bookmark_type,
				'user_id'=>$user_id));
			}while($stmt->fetch());
			
			
			$stmt->close();
			echo json_encode($response);

		}
		else{
			
			$stmt->close();
			echo 'SQL Query Error';
		}

	}
}


?>