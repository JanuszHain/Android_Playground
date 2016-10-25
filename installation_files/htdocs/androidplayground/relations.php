<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$mysqli = $db->connect();

$personId = -1;
$lastId = -1;
$limit = 0;


if(isset($_GET['personId'])){
	$personId = $_GET['personId'];
}

if(isset($_GET['lastId'])){
	$lastId = $_GET['lastId'];
}

if(isset($_GET['limit'])){
	$limit = $_GET['limit'];
}


if($limit > 0){
	$result = $mysqli->query("CALL LoadRelationsWithLimit('$personId','$limit','$lastId')");
}
else{
$result = $mysqli->query("CALL LoadRelations('$personId')");
}



 $response = array();
 while ($row = $result->fetch_assoc()){
  $data = array();
  $data["id"]=$row["id"];
	$data["idPersonA"]=$row["idPerson1"];
	$data["idPersonB"]=$row["idPerson2"];
  array_push($response, $data);

 }
   echo json_encode($response, JSON_UNESCAPED_SLASHES);
	 
	 $mysqli->close();
	 
?>
