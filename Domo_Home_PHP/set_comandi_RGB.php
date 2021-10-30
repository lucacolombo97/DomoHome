<?php
include("Connessione.php");

$stato=$_GET['stato'];
$r=$_GET['red'];
$g=$_GET['green'];
$b=$_GET['blue'];
$atmosfera=$_GET['atmosfera'];

if($stato==1)
	$sql="UPDATE Comandi_RGB SET stato=1,R='$r',G='$g',B='$b',atmosfera='$atmosfera' WHERE id=1";
else
	$sql="UPDATE Comandi_RGB SET stato=0,R=0,G=0,B=0,atmosfera='Off' WHERE id=1";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

echo "ok";

mysqli_close($connessione);
?>
