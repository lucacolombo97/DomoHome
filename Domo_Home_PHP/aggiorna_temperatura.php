<?php
include("Connessione.php");

$temperatura=$_GET["temperatura"];

$sql="UPDATE Aggiorna_Temperatura SET valore='$temperatura', data=now()";

$query=mysqli_query($connessione,$sql) or die("Errore query");

echo "ok";

mysqli_close($connessione);
?>
