<?php session_start();
$sessao= $_SESSION['user'];
if (isset($_SESSION['user'])) {
	header('location: index.php');
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>register.php</title>
	<link rel="stylesheet" href="_base/style.css">

</head>
<body>
<h1>Cadastro</h1>
	<form action="recording.php" method="post">
		<input type="text" name="user" placeholder="user"><br>
		<input type="password" name="password" placeholder="password">
		<input type="password" name="password2" placeholder="password">
		<input type="submit" value="Registar">
	</form>
	<a href="login.php">login</a>
</body>
</html>