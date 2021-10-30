<?php
include("Connessione.php");

$user=$_POST['user'];
$password=$_POST['psw'];

$sql="SELECT * FROM Utente WHERE user='$user' AND password=md5('$password')";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

if(mysqli_num_rows($query)>0)
{
	echo "ok";
}

mysqli_close();

?>