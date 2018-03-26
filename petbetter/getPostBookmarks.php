<?php

require 'init.php';

$user_id = $_POST['user_id'];

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.user_id AS user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, p.topic_id AS topic_id, p.date_created AS date_created, p.post_photo AS post_photo, p.id_link AS id_link, p.is_deleted AS is_deleted FROM bookmarks AS b INNER JOIN posts AS p ON b.item_id = p._id WHERE b.bookmark_type = 2 AND b.user_id = ? AND p.is_deleted = 0")){
		$stmt->bind_param("s", $user_id);
		$stmt->execute();
		$stmt->bind_result($_id, $user_id, $topic_name, $topic_content, $topic_id, $date_created, $post_photo, $id_link, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'user_id'=>$user_id,
				'topic_name'=>$topic_name,
				'topic_content'=>$topic_content,
				'topic_id'=>$topic_id,
				'date_created'=>$date_created,
				'post_photo'=>$post_photo,
				'id_link'=>$id_link,
				'is_deleted'=>$is_deleted));
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