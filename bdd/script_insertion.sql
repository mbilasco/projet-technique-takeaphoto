-- phpMyAdmin SQL Dump
-- version 4.1.9
-- http://www.phpmyadmin.net
--
-- Généré le :  Ven 27 Février 2015 à 16:39
-- Version du serveur :  5.1.73-2+squeeze+build1+1-log
-- Version de PHP :  5.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `takeaphotoforme`
--

--
-- Contenu de la table `takeaphotoforme_demandes`
--

INSERT INTO `takeaphotoforme_demandes` (`id_demande`, `id_user`, `longitude`, `latitude`, `description`, `etat`, `timestamp`) VALUES
(172, '127616641@N02', '2.2901232', '48.8618424', 'Trocadero', 2, '2015-01-07 15:13:48'),
(177, '127616641@N02', '3.1300054863095284', '50.61190849238861', 'Stade Pierre Mauroy de nuit', 0, '2015-02-20 08:22:10'),
(175, '127616641@N02', '6.640135', '43.271124', 'Photo de la facade de la gendarmerie de St Tropez', 0, '2015-01-07 15:13:48'),
(176, '128758626@N02', '3.1379156559705734', '50.60997918018832', 'Posters', 2, '2015-02-17 08:48:02'),
(174, '127616641@N02', '3.136939', '50.613664', 'IUT A de Lille', 2, '2015-01-07 15:13:48'),
(173, '127616641@N02', '-74.0445', '40.689249', 'Statue de la Liberte', 2, '2015-01-07 15:13:48');

--
-- Contenu de la table `takeaphotoforme_reponses`
--

INSERT INTO `takeaphotoforme_reponses` (`id_reponse`, `url`, `id_demande`) VALUES
(59, 'https://farm8.staticflickr.com/7312/16368313207_dbeb0c01e9.jpg', 2),
(51, 'https://farm9.staticflickr.com/8599/16552548221_e1966e757f.jpg', 172),
(58, 'https://farm9.staticflickr.com/8599/16552548221_e1966e757f.jpg', 2),
(57, 'https://farm9.staticflickr.com/8590/16368312617_790e5b6626.jpg', 2),
(56, 'https://farm8.staticflickr.com/7343/16367942439_9227070cb9.jpg', 174),
(55, 'https://farm8.staticflickr.com/7317/16553127542_59660c1e29.jpg', 174),
(50, 'https://farm9.staticflickr.com/8590/16368312617_790e5b6626.jpg', 172),
(53, 'https://farm8.staticflickr.com/7408/15934046303_627cf88397.jpg', 173),
(54, 'https://farm8.staticflickr.com/7302/16552548781_25fbe7b9e9.jpg', 174),
(52, 'https://farm8.staticflickr.com/7312/16368313207_dbeb0c01e9.jpg', 172),
(64, 'https://farm8.staticflickr.com/7294/16588559305_bd46e9eeed.jpg', 176),
(60, 'https://farm8.staticflickr.com/7408/15934046303_627cf88397.jpg', 3),
(61, 'https://farm8.staticflickr.com/7302/16552548781_25fbe7b9e9.jpg', 4),
(62, 'https://farm8.staticflickr.com/7317/16553127542_59660c1e29.jpg', 4),
(63, 'https://farm8.staticflickr.com/7343/16367942439_9227070cb9.jpg', 4);

--
-- Contenu de la table `takeaphotoforme_users`
--

INSERT INTO `takeaphotoforme_users` (`id_user`, `login`, `password`, `date_creation`) VALUES
(55, 'test@test.fr', '6c48c91615341a90e6ccf1863c763f87', '2014-11-08 10:44:18'),
(56, 'toto@toto.fr', '6c48c91615341a90e6ccf1863c763f87', '2014-11-08 14:27:12'),
(57, 'test@test.com', '6c48c91615341a90e6ccf1863c763f87', '2015-01-05 14:14:15');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
