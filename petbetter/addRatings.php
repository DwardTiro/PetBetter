<?php

require 'init.php';

$ratelist = json_decode(file_get_contents('php://input'),true);

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($ratelist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		
		//$rater_id = $ratelist[$i]['rater_id'];
		//$rated_id = $ratelist[$i]['rated_id'];
		if($stmt = $mysqli->prepare("SELECT rating FROM ratings WHERE rater_id = ? AND rated_id = ? AND rating_type = ?")){
			//$stmt = mysql_query("SELECT rating FROM ratings WHERE rater_id = $rater_id AND rated_id = $rated_id");
			$stmt->bind_param("sss", $ratelist[$i]['rater_id'],$ratelist[$i]['rated_id'],$ratelist[$i]['rating_type']);
			$stmt->execute();
			$stmt->store_result();
			
			if($stmt->num_rows>0){
				//echo $ratelist[$i]['rating'];
			
				if($stmt = $mysqli->prepare("UPDATE  ratings SET rating = ? WHERE rater_id = ? AND rated_id = ? AND rating_type = ?")){
					$stmt->bind_param("ssss", $ratelist[$i]['rating'], $ratelist[$i]['rater_id'], $ratelist[$i]['rated_id'], $ratelist[$i]['rating_type']);
					$stmt->execute();
					$stmt->close();
					//UPDATE  ratings SET rating = ? WHERE rater_id = ? AND rated_id = ?
				}
			}
			else{
				if($stmt = $mysqli->prepare("INSERT INTO ratings (rater_id, rated_id, rating, comment, rating_type, date_created, is_deleted) VALUES (?,?,?,?,?,?,?)")){
					$stmt->bind_param("sssssss", $ratelist[$i]['rater_id'], $ratelist[$i]['rated_id'], $ratelist[$i]['rating'], $ratelist[$i]['comment'], $ratelist[$i]['rating_type'], 
					$ratelist[$i]['date_created'], $ratelist[$i]['is_deleted']);
					$stmt->execute();
					$stmt->close();
						
				}				
				else{
					$stmt->close();
				}
			}
		}
		$i = $i + 1;
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
	$response['email'] = $user['email'];
	$response['password'] = $user['password'];
	$response['age'] = $user['age'];
	$response['user_type'] = $user['user_type'];
}
else{
	$response['error'] = true; 
	$response['message'] = "Invalid email or password";
}
*/
/*
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['email']) and isset($_POST['password'])){
		$db = new DbOperations(); 

		if($db->userLogin($_POST['email'], $_POST['password'])){
			$user = $db->getUserByUsername($_POST['email']);
			$response['error'] = false;
			$response['_id'] = $user['_id'];
			$response['first_name'] = $user['first_name'];
			$response['last_name'] = $user['last_name'];
			$response['mobile_num'] = $user['mobile_num'];
			$response['phone_num'] = $user['phone_num'];
			$response['email'] = $user['email'];
			$response['password'] = $user['password'];
			$response['age'] = $user['age'];
			$response['user_type'] = $user['user_type'];
		}else{
			$response['error'] = true; 
			$response['message'] = "Invalid email or password";			
		}

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}
echo json_encode($response)
*/

?>