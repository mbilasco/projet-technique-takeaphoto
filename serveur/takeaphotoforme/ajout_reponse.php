<?php
/**
 * Ajout d'une reponse
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$url = $_POST['url'];
$id_demande = $_POST['idDemande'];
$json[] = array();
/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
	
if (!empty($url) && !empty($id_demande)){
	$sql_insert_reponse = "INSERT INTO takeaphotoforme_reponses(url, id_demande) VALUES ('$url','$id_demande')";
	$query_insert_reponse = $connexion->prepare($sql_insert_reponse);
	$query_insert_reponse->execute();
	if ($query_insert_reponse){
		/* Recuperation info demande */
		$sql_select_data_reponse = "SELECT * FROM takeaphotoforme_reponses WHERE url = '".$url."' AND id_reponse = '".$id_reponse."'";
		$query_select_date_reponse = $connexion->prepare($sql_select_data_reponse);
		$query_select_date_reponse->execute();
		$result['result'] = "TRUE";
		$result['id'] = 1;
		//Test de la requete
		if ($query_select_date_reponse){
			//Test du nombre de result
			if ($query_select_date_reponse->rowCount()){
				$data_reponse = $query_select_date_reponse->fetch(PDO::FETCH_ASSOC);
				$id_reponse = $data_reponse['id_reponse'];
				$result['result'] = "TRUE";
				$result['id'] = $id_reponse;
			}
		}
	}
	else{
		$result['result'] = "FALSE";
		$result['message'] = "Insertion";
	}
}
else{
	$result['result'] = "FALSE";
	$result['message'] = "Erreur dans les paramètres.";
}

/* 7) result */
print(json_encode($result));
?>
