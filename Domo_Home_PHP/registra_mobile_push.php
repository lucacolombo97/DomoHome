<?php
include("Connessione.php");

$regid=$_GET['regid'];

$sql="UPDATE Notifiche_Push SET regid='$regid', data=now() WHERE id=1";
$query=mysqli_query($connessione,$sql) or die("Errore query");


mysqli_close($connessione);

?>