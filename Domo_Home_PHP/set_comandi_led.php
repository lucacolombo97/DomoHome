<?php

include("Connessione.php");

$sql="UPDATE Comandi_Led SET stato=NOT stato WHERE id=1";
$query=mysqli_query($connessione,$sql) or die("Errore query");

mysqli_close($connessione);
?>