<?php session_start();
$section= $_SESSION['user'];
if (isset($_SESSION['user'])) {
	header('location:index.php');
}
$create_file = fopen($section.".txt", "a");
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" href="_base/style.css">
</head>
<body>
	<h1>Login</h1>
	<form action="Auth.php" method="POST">
		<input type="text" name="user" placeholder="user">
		<input type="password" name="password" placeholder="password">
		<input type="submit" value="Enviar">
	</form>
	<a href="register.php">cadastrar</a>
</body>
</html>