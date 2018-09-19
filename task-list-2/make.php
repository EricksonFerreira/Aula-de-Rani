#!/usr/bin/php
<?php

define("DS", DIRECTORY_SEPARATOR);
define("HOME", getenv("HOME"));

if (!isset($argv[1])) {
	echo "Comando inválido";
	var_dump($_SERVER);
	exit(0);
}

switch($argv[1]) {
	case 'config':
		config();
		break;
	case 'compile-tests':
		compileTest();
		break;
    case 'run-tests':
        runTests();
        break;
}

function config() {
	// $dest = HOME."/.ravac/libs/";
	// $libsFile = "test-libs.zip";
	// // var_dump(is_file($dest . DS . $libsFile));
	// // exit();
	// if (is_dir($dest) && is_file($dest . $libsFile)) {
	// 	echo "Nada a configurar...\n";
	// 	return;
	// }
	// if (!is_dir($dest)) {
	// 	system(sprintf("mkdir -p %s", $dest));
	// }

	// $paths = [
	// 	HOME."/Downloads/",
	// 	HOME."/Dropbox/IFPE/",
	// 	"./",
	// 	"test/"
	// ];
	// foreach($paths as $path) {
	// 	// var_dump($path, is_dir($path), $path . $libsFile, is_file($path.$libsFile));
	// 	if (is_dir($path) && is_file($path . $libsFile)) {
 //            echo $path . $libsFile;
	// 		system(sprintf("cp %s %s", $path . $libsFile, $dest . $libsFile));
	// 		system(sprintf("cd %s && unzip %s", $dest, $libsFile));
	// 		system(sprintf("cd %s/test-libs && chmod +x geckodriver.*", $dest));
	// 		return;
	// 	}
	// }
	// echo "test-libs.zip não encontrado. Faça o download a partir de http://ravac.net/test-libs, copie o arquivo para este diretório e tente novamente.\n";
	$dest = HOME."/.ravac/libs/";
	if (!is_dir("{$dest}")) {
		system("mkdir -p {$dest}");
	}
	if (is_dir("{$dest}libs") || is_dir("{$dest}test-libs")) {
		system("mv {$dest}* {$dest}");
		system("mv {$dest}*/* {$dest}");
		system("cd {$dest} && rm -rf libs test-libs test-libs*");
	}

	$libs = [
		"assertj-core-3.8.0.jar" => "http://central.maven.org/maven2/org/assertj/assertj-core/3.8.0/assertj-core-3.8.0.jar",
		"hamcrest-core-1.3.jar" => "http://central.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar",
		"junit-4.12.jar" => "http://central.maven.org/maven2/junit/junit/4.12/junit-4.12.jar",
		"selenium-server-standalone-3.13.0.jar" => "https://selenium-release.storage.googleapis.com/3.13/selenium-server-standalone-3.13.0.jar"
	];
	$drivers = [
		"geckodriver.linux64" => "https://github.com/mozilla/geckodriver/releases/download/v0.22.0/geckodriver-v0.22.0-linux64.tar.gz",
		"geckodriver.macos" => "https://github.com/mozilla/geckodriver/releases/download/v0.22.0/geckodriver-v0.22.0-macos.tar.gz"
	];
	foreach($libs as $lib => $url) {
		if (!is_file("{$dest}{$lib}")) {
			echo "Downloading {$lib}...\n";
			file_put_contents("{$dest}{$lib}", fopen($url, 'r'));
		}
	}
	foreach($drivers as $driver => $url) {
		if (!is_file("{$dest}{$driver}")) {
			echo "Downloading {$driver}...\n";
			file_put_contents("{$dest}{$driver}.tar.gz", fopen($url, 'r'));
			system("cd {$dest} && tar -zxvf {$driver}.tar.gz && mv geckodriver {$driver} && chmod +x {$driver} && rm {$driver}.tar.gz");
		}
	}
}

function compileTest() {
	echo "Compilando...\n";
	system("cd test && rm *.class");
	system(sprintf("cd test && javac -cp \".:%s/.ravac/libs/*\" Teste.java", HOME));
	echo "Ok\n";
}

function runTests() {
	system(sprintf("cd test && java -cp \".:%s/.ravac/libs/*\" Teste", HOME));
}


?>