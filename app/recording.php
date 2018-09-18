<?php
$user=$_POST['user'];
$password=$_POST['password'];
$password2=$_POST['password2'];

if ($password == $password2) {
	$file= file('usuarios.csv');
	$fopen= fopen('usuarios.csv', 'a');
	
	foreach ($file as $linha => $coluna) {
		$explodir=explode(";;;", $coluna);
		if ($user == $explodir[0]) {
		header('Location: erro.php');
		exit();
		}
	}
		$escrever= $user.";;;".$password."\n";
		$execução=fwrite($fopen, $escrever);
		header('Location: login.php');
}else{
	header('location: register.php');
}

?>