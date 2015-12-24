<?php 
header('content-type: application/json; charset=utf-8');
header("access-control-allow-origin: *");

$url = $_GET['link'];



if (!function_exists('curl_init')){ 
die('CURL is not installed!');
}
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$output = curl_exec($ch);
curl_close($ch);

echo json_encode($output);

?>