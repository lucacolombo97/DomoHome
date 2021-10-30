<?php

include("Connessione.php");

$id=$_GET['id'];

$sql="UPDATE Comandi_Motore SET stato=1 WHERE id=$id";
$query=mysqli_query($connessione,$sql) or die("Errore query");

mysqli_close($connessione);
?>