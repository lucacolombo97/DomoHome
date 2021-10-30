<?php
include("Connessione.php");

$temperatura=$_GET['temperatura'];

$sql="UPDATE Set_Temperatura SET valore='$temperatura',stato=1, data=now() WHERE id=1";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

echo "ok";

mysqli_close($connessione);

?>
