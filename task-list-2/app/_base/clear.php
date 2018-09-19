<?php

session_start();

system("rm ../*.csv");
system("rm ../*/*.csv");

session_destroy();

header("location: /index.php");

?>