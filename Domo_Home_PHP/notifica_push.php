<?php
include("Connessione.php");

$sql="SELECT regid FROM Notifiche_Push WHERE id=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");
$row=mysqli_fetch_array($query);

$regid=$row['regid'];
 
$url = 'https://android.googleapis.com/gcm/send';
 
define("GOOGLE_API_KEY", "<API_KEY>");
 
$registration_ids = array($regid);
 
$message=array("message" => “”);
 
$fields = array('registration_ids' => $registration_ids,'data' =>$message);
 
$headers = array('Authorization: key=' . GOOGLE_API_KEY,'Content-Type: application/json');
 
     
 
$ccurl = curl_init();

curl_setopt($ccurl, CURLOPT_URL, $url);
 
curl_setopt($ccurl, CURLOPT_POST, true);
 
curl_setopt($ccurl, CURLOPT_HTTPHEADER, $headers);
 
curl_setopt($ccurl, CURLOPT_RETURNTRANSFER, true);
 
curl_setopt($ccurl, CURLOPT_SSL_VERIFYPEER, false);
 
curl_setopt($ccurl, CURLOPT_POSTFIELDS, json_encode($fields));
 
$result = curl_exec($ccurl);
 
curl_close($ccurl);
 
?>
