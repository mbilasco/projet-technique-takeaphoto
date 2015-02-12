<?php
include("include/include.php");

$json = json_encode(
array("result" => array(
 		array(
        'id_demande' => 1,
        'id_user' => 2,
        'longitude' => 4,
        'latitude' => 5,
        'etat' =>0,
        'description' => "toto"
        ), 
	array(
        'id_demande' => 13,
        'id_user' => 12,
        'longitude' => 14,
        'latitude' => 15,
        'etat' =>10,
        'description' => "ttootototo"
        )       
       )
));

$mcrypt = new MCrypt();
#Encrypt
$encrypted = $mcrypt->encrypt("Text to encrypt");
echo $encrypted;
#Decrypt
$decrypted = $mcrypt->decrypt($encrypted);
echo $decrypted;

//print_r($json);
?>
