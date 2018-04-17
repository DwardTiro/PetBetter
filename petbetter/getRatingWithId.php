<?php

require 'init.php';

$vet_id = $_POST['vet_id'];
$rating_type = $_POST['rating_type'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE vet_id = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE vet_id = '$vet_id' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if(!(isset($_POST['vet_id']))){
		echo 'No vet id';
		exit(0);
	}

	if($stmt = $mysqli->prepare("SELECT rating FROM ratings WHERE rated_id = ? AND rating_type = ?")){
		$stmt->bind_param("ss", $vet_id, $rating_type);
		$stmt->execute();
		$stmt->bind_result($rating);
		$stmt->store_result();
	
		if($stmt->fetch()){
			do{
				array_push($response, array('rating'=>$rating));
			}while($stmt->fetch());
			$stmt->close();
			$n = count($response);
			$avg = 0;
			for($i = 0; $i<$n;$i++){
				$avg = $avg + $response[$i]['rating'];
			}
			$avg = $avg / $n;
			
			//echo json_encode($response);
			echo json_encode($avg);
		}
		
		else{
			
			$stmt->close();
			echo 0;
		}
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}
}

/*

if(mysqli_fetch_array($result)){
	$user = mysqli_fetch_array($result);
	$response['error'] = false;
	$response['_id'] = $user['_id'];
	$response['first_name'] = $user['first_name'];
	$response['last_name'] = $user['last_name'];
	$response['mobile_num'] = $user['mobile_num'];
	$response['phone_num'] = $user['phone_num'];
	$response['vet_id'] = $user['vet_id'];
	$response['password'] = $user['password'];
	$response['age'] = $user['age'];
	$response['user_type'] = $user['user_type'];
}
else{
	$response['error'] = true; 
	$response['message'] = "Invalid vet_id or password";
}
*/
/*
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['vet_id']) and isset($_POST['password'])){
		$db = new DbOperations(); 

		if($db->userLogin($_POST['vet_id'], $_POST['password'])){
			$user = $db->getUserByUsername($_POST['vet_id']);
			$response['error'] = false;
			$response['_id'] = $user['_id'];
			$response['first_name'] = $user['first_name'];
			$response['last_name'] = $user['last_name'];
			$response['mobile_num'] = $user['mobile_num'];
			$response['phone_num'] = $user['phone_num'];
			$response['vet_id'] = $user['vet_id'];
			$response['password'] = $user['password'];
			$response['age'] = $user['age'];
			$response['user_type'] = $user['user_type'];
		}else{
			$response['error'] = true; 
			$response['message'] = "Invalid vet_id or password";			
		}

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}
echo json_encode($response)
*/

?>