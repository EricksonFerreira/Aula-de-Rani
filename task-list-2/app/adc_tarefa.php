<?<?php
session_start();
if (!isset($_SESSION['user'])) {
	header('location: login.php');
}
$tarefa = $_POST['tarefa']."\n";
$arquivo = fopen($_SESSION['user'].".txt", "a");
$escrever = fwrite($arquivo, $tarefa);
header('location: index.php');

?>