-- phpMyAdmin SQL Dump
-- version OVH
-- http://www.phpmyadmin.net
--
-- Client: mysql5-15.perso
-- Généré le : Ven 31 Mai 2013 à 00:50
-- Version du serveur: 5.1.49
-- Version de PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Base de données: `jeremiesfolio`
--

-- --------------------------------------------------------

--
-- Structure de la table `takeaphotoforme_demandes`
--

CREATE TABLE IF NOT EXISTS `takeaphotoforme_demandes` (
  `id_demande` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `longitude` varchar(255) NOT NULL,
  `latitude` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `etat` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_demande`),
  KEY `id_user` (`id_user`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=141 ;
