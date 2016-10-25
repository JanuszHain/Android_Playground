<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$mysqli = $db->connect();

$lastId = -1;
$limit = 5;
$secondName = "";

if(isset($_GET['lastId'])){
	$lastId = $_GET['lastId'];
}

if(isset($_GET['limit'])){
	$limit = $_GET['limit'];
}

if(isset($_GET['lastSecName'])){
	$secondName = $_GET['lastSecName'];
}


if(strcmp($secondName, "")==0){
	$result = $mysqli->query("CALL LoadPersonsWithLimit($lastId,$limit)");
}
else{
	$result = $mysqli->query("CALL LoadPersonsWithLimitSecName($lastId,$limit,'$secondName')");
}


 $response = array();
 while ($row = $result->fetch_assoc()){
  $data = array();
  $data["id"]=$row["id"];
	$data["firstName"]=$row["firstName"];
	$data["secondName"]=$row["secondName"];
	$data["birthDate"]=$row["birthDate"];
	$data["city"]=$row["city"];
	$data["photoUrl"]=$row["photoUrl"];
  array_push($response, $data);

 }
   echo json_encode($response, JSON_UNESCAPED_SLASHES);
	 
	 $mysqli->close();
	 
?>




