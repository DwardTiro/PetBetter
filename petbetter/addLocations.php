<?php

require 'init.php';

$locationlist = json_decode(file_get_contents('php://input'),true);


if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($locationlist);
	//echo $n;
	$i = 0;
	//echo $vetlist[$i]['_id'];
	
	while($i<$n){
		if($stmt = $mysqli->prepare("INSERT INTO markers (bldg_name, longitude, latitude, location, user_id, type, faci_id) VALUES (?,?,?,?,?,?,?)")){
			$stmt->bind_param(
				"sssssss",
				$locationlist[$i]['bldg_name'],
				$locationlist[$i]['longitude'],
				$locationlist[$i]['latitude'],
				$locationlist[$i]['location'],
				$locationlist[$i]['user_id'],
				$locationlist[$i]['type'], 
				$locationlist[$i]['faci_id']);
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