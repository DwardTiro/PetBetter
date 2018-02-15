<?php
	require 'init.php';
	
	$description = $_POST['description'];
	$image = $_POST['image'];
	
	$upload_path = "uploads/$title.jpg";
	
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		if($stmt = $mysqli->prepare("INSERT INTO ")){
		$stmt->bind_param("ss", $email , $password);
		$stmt->execute();
		$stmt->bind_result($user_id, $first_name, $last_name, $mobile_num, $phone_num, $email,  $password, $age, $user_type, $user_photo);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			$stmt->close();
			//echo json_encode($response);
			echo json_encode(array('user_id'=>$user_id,
			'first_name'=>$first_name,
			'last_name'=>$last_name,
			'mobile_num'=>$mobile_num,
			'phone_num'=>$phone_num,
			'email'=>$email,
			'password'=>$password,
			'age'=>$age,
			'user_type'=>$user_type,
			'user_photo'=>$user_photo));
		}
		else{
			$stmt->close();
			echo 'SQL Query Error';
		}
	}
		
	}

?>