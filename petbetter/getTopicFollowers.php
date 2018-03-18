<?php

require 'init.php';

$topic_id = $_POST['topic_id'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("SELECT * FROM followers WHERE topic_id = ? AND is_allowed = 1")){
		$stmt->bind_param("s", $topic_id);
		$stmt->execute();
		$stmt->bind_result($_id, $topic_id, $user_id, $is_allowed);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'topic_id'=>$topic_id,
				'user_id'=>$user_id,
				'is_allowed'=>$is_allowed));
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