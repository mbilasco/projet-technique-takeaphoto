<?php
include("include/include.php");
	/* 2) Connection DB */
	$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
	
    /* 4) Insertion DB */
    $id = $_POST['id'];
	//$sql_insert = "INSERT INTO takeaphotoforme_demandes(id_user) VALUES($id)";
	//$query_insert = $connexion->prepare($sql_insert);
	//$query_insert->execute();
	
	$res['result'] = "TRUE";
	$res['id'] = "$id";
	print(json_encode($res));
	
?>