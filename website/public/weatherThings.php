<?php 
header('content-type: application/json; charset=utf-8');
header("access-control-allow-origin: *");

$url = $_POST['link'];

$output = file_get_contents($url);

echo json_encode($output);

?>