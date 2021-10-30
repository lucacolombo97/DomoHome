<?php

$user="root";
$psw="";
$host="localhost";
$database="my_lucacolombo";

$connessione=@mysqli_connect($host,$user,$psw,$database) or die ("Errore di connessione");
?>