<?php
/* Creation d'un dossier
 * bool mkdir ( string $pathname [, int $mode = 0777 [, bool $recursive = false [, resource $context ]]] )
 * @param path
 * @return boolean
 */
function creerDossier($path){
	if (!mkdir($path))
	    return false;
	else
		return true;
}

/* Test si l'utilisateur existe
 *
 * @param login utilisateur
 * @return boolean
 */
function userExist($login, $connexion){
	$sql_select_login = "SELECT * FROM takeaphotoforme_users WHERE login = '".$login."'";
	$query_select_login = $connexion->prepare($sql_select_login);
	$query_select_login->execute();
	$existe = $query_select_login->rowCount();
	
	if ($existe)
		return true;
	else
		return false;
}

/* Récupère l'ID de l'utilisateur
 *
 * @param login utilisateur
 * @return ID_User
 */
function getID_User($login, $connexion){
	$sql_select_login = "SELECT * FROM takeaphotoforme_users WHERE login = '$login'";
	$query_select_login = $connexion->prepare($sql_select_login);
	$query_select_login->execute();
	$data = $query_select_login->fetch(PDO::FETCH_ASSOC);
	return $data['id_user'];
}

/* Test si l'utilisateur & mdp sont valide
 *
 * @param login utilisateur
 * @param password password de l'utilisateur
 
 */
function isUserValide($login, $password, $connexion){
	$sql_select_validite = "SELECT * FROM takeaphotoforme_users WHERE login = '".$login."' AND password = '".$password."'";
	$query_select_validite = $connexion->prepare($sql_select_validite);
	$query_select_validite->execute();
	$existe = $query_select_validite->rowCount();

	if ($existe)
		return true;
	else
		return false;
}

/* Test si la longitude et la latitude existe déjà
 *
 * @param longitude 
 * @param latitude
 * @return boolean
 */
function latLongAlreadyExist($longitude,$latitude, $connexion)
{
	$sql_select_lat_long = "SELECT * FROM takeaphotoforme_demandes WHERE longitude = $longitude AND latitude = $latitude";
	$query_select_lat_long = $connexion->prepare($sql_select_lat_long);
	$query_select_lat_long->execute();
	$existe = $query_select_lat_long->rowCount();
	
	if ($existe > 0)
		return true;
	else
		return false;
}
?>