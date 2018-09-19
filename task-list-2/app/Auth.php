<?php
session_start();
$user = $_POST['user'];
$password = $_POST['password'];

$file = file('usuarios.csv');
if (in_array($user . ";;;" . $password."\n", $file)) {
	$_SESSION['user'] = $user;
	header('location: index.php');
} else {
	header('location: login.php');
}

?>