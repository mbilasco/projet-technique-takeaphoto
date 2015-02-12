<?php
/**
 * Télécharge le fichier de réponse a une demande
 *
 * 1) Récupération des informations envoyées par l'application (login, password)
 * 2) Connection DB
 * 3) Récupération info demande (id_demande)
 * 4) Récupération info user (id_user)
 * 5) Upload photo
 * 6) MAJ etat
 * 7) Retour
 *
 * Liens : 
 * 
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$id_user = $_GET[‘idUser’];

/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
		
/* 3) Récupération info demande */
$id_demande = $_GET['id_demande'];

/* 4) Récupération info user (id_user) */
$sql_select_demande = "SELECT * FROM takeaphotoforme_demandes WHERE id_demande = $id_demande";
$query_select_demande = $connexion->prepare($sql_select_demande);
$query_select_demande->execute();
$data = $query_select_demande->fetch(PDO::FETCH_ASSOC);
	
/* 5) Upload photo */
$target_path = "users/" . $id_user . "/" . $id_demande ."/";
$target_path = $target_path . basename( $_FILES['uploaded_file']['name']);
if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_path)) {
	/* 6) MAJ etat */
	$sql_update_demande = "UPDATE takeaphotoforme_demandes SET etat = 2 WHERE id_demande = ".$id_demande;
	$query_update_demande = $connexion->prepare($sql_update_demande);
	$query_update_demande->execute();
				 
	$result['result'] = "TRUE";
	$result['message'] = "JAYJAY";
} else{
	$result['result'] = "FALSE";
	$result['message'] = "Le déplacement du fichier a échoué.";
}

/* 7) Retour */
print(json_encode($result));
?>


