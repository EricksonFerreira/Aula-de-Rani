<?php session_start();
$sessao= $_SESSION['user'];
if (!isset($_SESSION['user'])) {
	header('location: login.php');
}
$create_file = fopen($_SESSION['user'].".txt", "a");
?>

<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" href="_base/style.css">
</head>
<body>
	
	<h1>Minha lista de tarefas</h1>	
	<p>Usuário: <?=" ".$_SESSION['user'];?>  | <a class="logout" href="logout.php">Sair</a></p>
	<form class="addtask" action="adc_tarefa.php" method="Post">
		<table class="tasks">
            <tr>
                <th>Tarefa</th>
                <th>Ações</th>
             <?php $file= file($sessao.".txt");?>
			 <?php foreach ($file as $linha => $coluna): ?>
            <tr>
                <td class="todo"><?= $file[$linha];?></td><br>
                <td class="action">
                	<a class="taskdone" href="#">Ok</a>
                	<a class="rmtask" href="">0</a>
                </td>
            </tr>
             <?php endforeach ?>
            <tr>        
                <td><input type="text" name="tarefa" placeholder="Tarefa" required></td>
                <td class="action"><input type="submit" value="ok"></td>
            </tr>
		</table>	
</body>
</html>


<!--         	    <tr class="task">
                    <td class="todo">123</td>
                    <td class="action">
                       <a class="taskdone" href="taskDone.php?id=2">&#10004;</a>
                       <a class="rmtask" href="rmTask.php?id=2">&#10007;</a>
                    </td>
                </tr> -->