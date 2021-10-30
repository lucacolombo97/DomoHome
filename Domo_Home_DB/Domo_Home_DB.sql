-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Giu 17, 2016 alle 23:22
-- Versione del server: 5.1.71-community-log
-- PHP Version: 5.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `my_lucacolombo`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Aggiorna_Temperatura`
--

CREATE TABLE IF NOT EXISTS `Aggiorna_Temperatura` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `valore` float NOT NULL,
  `data` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Aggiorna_Temperatura`
--

INSERT INTO `Aggiorna_Temperatura` (`id`, `valore`, `data`) VALUES
(1, 23.24, '2016-06-17 10:26:44');

-- --------------------------------------------------------

--
-- Struttura della tabella `Comandi_Allarme`
--

CREATE TABLE IF NOT EXISTS `Comandi_Allarme` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `stato` tinyint(1) NOT NULL,
  `suonato` tinyint(1) NOT NULL,
  `descrizione` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Comandi_Allarme`
--

INSERT INTO `Comandi_Allarme` (`id`, `stato`, `suonato`, `descrizione`) VALUES
(1, 0, 0, 'Stato Allarme');

-- --------------------------------------------------------

--
-- Struttura della tabella `Comandi_Led`
--

CREATE TABLE IF NOT EXISTS `Comandi_Led` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `stato` tinyint(1) NOT NULL,
  `descrizione` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `Comandi_Led`
--

INSERT INTO `Comandi_Led` (`id`, `stato`, `descrizione`) VALUES
(1, 0, 'luci sala');

-- --------------------------------------------------------

--
-- Struttura della tabella `Comandi_Motore`
--

CREATE TABLE IF NOT EXISTS `Comandi_Motore` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pin_motore` int(10) NOT NULL,
  `stato` tinyint(1) NOT NULL,
  `direzione` tinyint(1) NOT NULL,
  `descrizione` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dump dei dati per la tabella `Comandi_Motore`
--

INSERT INTO `Comandi_Motore` (`id`, `pin_motore`, `stato`, `direzione`, `descrizione`) VALUES
(1, 3, 0, 0, 'apertura/chiusura box'),
(2, 5, 0, 0, 'apertura/chiusura  cancello');

-- --------------------------------------------------------

--
-- Struttura della tabella `Comandi_RGB`
--

CREATE TABLE IF NOT EXISTS `Comandi_RGB` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `R` int(255) NOT NULL,
  `G` int(255) NOT NULL,
  `B` int(255) NOT NULL,
  `stato` tinyint(1) NOT NULL,
  `atmosfera` varchar(40) NOT NULL,
  `descrizione` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=51 ;

--
-- Dump dei dati per la tabella `Comandi_RGB`
--

INSERT INTO `Comandi_RGB` (`id`, `R`, `G`, `B`, `stato`, `atmosfera`, `descrizione`) VALUES
(1, 0, 0, 0, 0, 'Off', 'Led RGB camera');

-- --------------------------------------------------------

--
-- Struttura della tabella `Notifiche_Push`
--

CREATE TABLE IF NOT EXISTS `Notifiche_Push` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `regid` varchar(200) NOT NULL,
  `data` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Notifiche_Push`
--

INSERT INTO `Notifiche_Push` (`id`, `regid`, `data`) VALUES
(1, 'APA91bF0xZAZ2Fc2gs0FJruGUy5dj4bKkMgXtnni2O5RR2So_t8xWz7saXTjS9le-uzFlwwRbZY_4LC_rnfGM-CxAMzhsdehiYAUx5KBPV_mpn1i-maJRbiiudjaoMQqlKq0iAwCJ0ExlJWwpr_aLyEF2qmKWl7S7g', '2016-06-17 02:05:08');

-- --------------------------------------------------------

--
-- Struttura della tabella `Set_Temperatura`
--

CREATE TABLE IF NOT EXISTS `Set_Temperatura` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `stato` tinyint(1) NOT NULL,
  `valore` float NOT NULL,
  `data` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `Set_Temperatura`
--

INSERT INTO `Set_Temperatura` (`id`, `stato`, `valore`, `data`) VALUES
(1, 0, 20, '2016-06-17 10:25:21');

-- --------------------------------------------------------

--
-- Struttura della tabella `Utente`
--

CREATE TABLE IF NOT EXISTS `Utente` (
  `user` varchar(10) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Utente`
--

INSERT INTO `Utente` (`user`, `password`) VALUES
('luca', 'ff377aff39a9345a9cca803fb5c5c081');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
