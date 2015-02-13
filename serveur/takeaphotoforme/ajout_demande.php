<?php
/**
 * Ajout d'une demande
 * 
 * @param idUser
 * @param description
 * @param longitude
 * @param latitude
 */
include("include/include.php");

/* Récupération des informations envoyées par l'application */
$id_user = $_POST['idUser'];
$json[] = array();
/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* Récupération des informations sur la demande (description, longitude, latitude) */
$description = $_POST['description'];
$longitude   = $_POST['longitude'];
$latitude    = $_POST['latitude'];
$etat = 0; //0 (aucune réponse), 1 (reponse), 2 (validé) 
	
if (!empty($description) && !empty($longitude) && !empty($latitude)){
	/* Vérification longitude latitude */
	if (!latLongAlreadyExist($longitude, $latitude, $connexion)){
		/* Ajout de la demande */
		$sql_insert_demande = "INSERT INTO takeaphotoforme_demandes(id_user, longitude, latitude, description, etat) VALUES ('$id_user','$longitude','$latitude','$description',$etat)";
		$query_insert_demande = $connexion->prepare($sql_insert_demande);
		$query_insert_demande->execute();
		if ($query_insert_demande){
 			/* Recuperation info demande */
			$sql_select_data_demande = "SELECT * FROM takeaphotoforme_demandes WHERE longitude = '".$longitude."' AND latitude = '".$latitude."'";
			$query_select_date_demande = $connexion->prepare($sql_select_data_demande);
			$query_select_date_demande->execute();
		
			//Test de la requete
			if ($query_select_date_demande){
				//Test du nombre de result
				if ($query_select_date_demande->rowCount()){
					$data_demande = $query_select_date_demande->fetch(PDO::FETCH_ASSOC);
					$id_demande = $data_demande['id_demande'];
					
					/* 6) Création répertoir de la demande */
					mkdir("users/" . $id_user . "/" . $id_demande);
					
					$result['result'] = "TRUE";
					$result['id'] = $id_demande;
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
		$result['message'] = "Le couple Latitude Longitude existe déjà.";
	}
}
else{
	$result['result'] = "FALSE";
	$result['message'] = "Erreur dans les paramètres.";
}

/* result */
print(json_encode($result));
?>
