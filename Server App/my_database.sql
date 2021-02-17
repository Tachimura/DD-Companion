-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 17, 2021 alle 11:46
-- Versione del server: 5.6.33-log
-- PHP Version: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `my_jdj0k3r`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `classes`
--

CREATE TABLE IF NOT EXISTS `classes` (
  `C_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `C_ID_NAME` int(11) unsigned NOT NULL,
  `C_HD` tinyint(4) unsigned NOT NULL DEFAULT '1',
  `C_HD_NEXT` tinyint(4) unsigned NOT NULL DEFAULT '1',
  `C_MAIN` tinyint(4) unsigned NOT NULL DEFAULT '1',
  `C_S1` tinyint(4) unsigned NOT NULL DEFAULT '1',
  `C_S2` tinyint(4) unsigned NOT NULL DEFAULT '2',
  `C_ABILITY_POINTS` tinyint(4) unsigned NOT NULL DEFAULT '1',
  `C_LB` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_LN` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_LM` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_NB` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_NN` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_NM` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_CB` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_CN` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `C_CM` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=8 ;

--
-- Dump dei dati per la tabella `classes`
--

INSERT INTO `classes` (`C_ID`, `C_ID_NAME`, `C_HD`, `C_HD_NEXT`, `C_MAIN`, `C_S1`, `C_S2`, `C_ABILITY_POINTS`, `C_LB`, `C_LN`, `C_LM`, `C_NB`, `C_NN`, `C_NM`, `C_CB`, `C_CN`, `C_CM`) VALUES
(1, 1, 12, 8, 0, 0, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1),
(2, 2, 8, 6, 5, 1, 5, 4, 1, 0, 1, 1, 1, 1, 1, 0, 1),
(3, 3, 8, 6, 4, 4, 5, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0),
(4, 4, 8, 6, 4, 3, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1),
(5, 5, 8, 6, 1, 1, 3, 8, 0, 0, 0, 1, 1, 1, 1, 1, 1),
(6, 6, 6, 5, 3, 3, 4, 2, 1, 1, 0, 1, 1, 0, 1, 1, 0),
(7, 7, 8, 6, 5, 4, 5, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `classes_description`
--

CREATE TABLE IF NOT EXISTS `classes_description` (
  `CD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CD_LAN` tinyint(4) NOT NULL DEFAULT '1',
  `CD_NAME` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`CD_ID`,`CD_LAN`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=8 ;

--
-- Dump dei dati per la tabella `classes_description`
--

INSERT INTO `classes_description` (`CD_ID`, `CD_LAN`, `CD_NAME`) VALUES
(1, 1, 'Barbarian'),
(1, 2, 'Barbaro'),
(2, 1, 'Bard'),
(2, 2, 'Bardo'),
(3, 1, 'Cleric'),
(3, 2, 'Chierico'),
(4, 1, 'Druid'),
(4, 2, 'Druido'),
(5, 1, 'Rogue'),
(5, 2, 'Ladro'),
(6, 1, 'Wizard'),
(6, 2, 'Mago'),
(7, 1, 'Warlock'),
(7, 2, 'Stregone');

-- --------------------------------------------------------

--
-- Struttura della tabella `game_manuals`
--

CREATE TABLE IF NOT EXISTS `game_manuals` (
  `GM_ID` int(11) NOT NULL AUTO_INCREMENT,
  `GM_VERSION` decimal(3,1) NOT NULL,
  `GM_RELEASE_DATA` date NOT NULL,
  `GM_USABLE_POINTS` tinyint(4) unsigned NOT NULL,
  `GM_BASE_FOR` tinyint(4) unsigned NOT NULL,
  `GM_BASE_DEX` tinyint(4) unsigned NOT NULL,
  `GM_BASE_COS` tinyint(4) unsigned NOT NULL,
  `GM_BASE_INT` tinyint(4) unsigned NOT NULL,
  `GM_BASE_SAG` tinyint(4) unsigned NOT NULL,
  `GM_BASE_CAR` tinyint(4) unsigned NOT NULL,
  PRIMARY KEY (`GM_ID`),
  UNIQUE KEY `manual_version` (`GM_VERSION`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

--
-- Dump dei dati per la tabella `game_manuals`
--

INSERT INTO `game_manuals` (`GM_ID`, `GM_VERSION`, `GM_RELEASE_DATA`, `GM_USABLE_POINTS`, `GM_BASE_FOR`, `GM_BASE_DEX`, `GM_BASE_COS`, `GM_BASE_INT`, `GM_BASE_SAG`, `GM_BASE_CAR`) VALUES
(1, '1.0', '2020-06-01', 10, 8, 8, 8, 8, 8, 8),
(2, '2.0', '2020-06-12', 14, 10, 10, 10, 10, 10, 10),
(3, '2.5', '2020-07-15', 20, 9, 9, 9, 9, 9, 9);

-- --------------------------------------------------------

--
-- Struttura della tabella `game_manual_classes`
--

CREATE TABLE IF NOT EXISTS `game_manual_classes` (
  `GM_C_ID_MANUAL` int(11) NOT NULL,
  `GM_C_ID_CLASS` int(11) NOT NULL,
  `GM_C_NEW` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`GM_C_ID_MANUAL`,`GM_C_ID_CLASS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dump dei dati per la tabella `game_manual_classes`
--

INSERT INTO `game_manual_classes` (`GM_C_ID_MANUAL`, `GM_C_ID_CLASS`, `GM_C_NEW`) VALUES
(1, 1, 1),
(2, 1, 0),
(2, 2, 1),
(2, 3, 1),
(3, 1, 0),
(3, 2, 0),
(3, 3, 0),
(3, 4, 1),
(3, 5, 1),
(3, 6, 1),
(3, 7, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `game_manual_notes`
--

CREATE TABLE IF NOT EXISTS `game_manual_notes` (
  `GM_N_ID` int(11) NOT NULL,
  `GM_N_LAN` tinyint(4) NOT NULL DEFAULT '1',
  `GM_N_NOTES` text CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`GM_N_ID`,`GM_N_LAN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dump dei dati per la tabella `game_manual_notes`
--

INSERT INTO `game_manual_notes` (`GM_N_ID`, `GM_N_LAN`, `GM_N_NOTES`) VALUES
(1, 1, 'Base manual that contain a class and one race to allow basic game sessions.'),
(1, 2, 'Manuale di base contenente una classe ed una razza per permettere sessioni di gioco basilari.'),
(2, 1, 'Manual 2.0 increase the pool of classes and races, allowing a better gameplay and an increased diversity of options the player can choose to play with.'),
(2, 2, 'Il manuale 2.0 aumenta il numero di razze e classi, permettendo un migliore gameplay ed un numero maggiore di opzioni con cui il giocatore puo giocare.'),
(3, 1, 'Manual 2.5 further increase the pool of classes, races and talents the player can choose to play with during the game sessions. Also Escanor has arrived.'),
(3, 2, 'Il manuale 2.5 aumenta ulteriormente il numero di classi, razze e talenti che il giocatore puo scegliere per giocare nelle sessioni di gioco. Escanor e anche qui.');

-- --------------------------------------------------------

--
-- Struttura della tabella `game_manual_races`
--

CREATE TABLE IF NOT EXISTS `game_manual_races` (
  `GM_R_ID_MANUAL` int(11) NOT NULL,
  `GM_R_ID_RACE` int(11) NOT NULL,
  `GM_R_NEW` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`GM_R_ID_MANUAL`,`GM_R_ID_RACE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dump dei dati per la tabella `game_manual_races`
--

INSERT INTO `game_manual_races` (`GM_R_ID_MANUAL`, `GM_R_ID_RACE`, `GM_R_NEW`) VALUES
(1, 1, 1),
(2, 1, 0),
(2, 2, 1),
(2, 3, 1),
(2, 4, 1),
(3, 1, 0),
(3, 2, 0),
(3, 3, 0),
(3, 4, 0),
(3, 5, 1),
(3, 6, 1),
(3, 7, 1),
(3, 8, 1),
(3, 9, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `game_manual_talents`
--

CREATE TABLE IF NOT EXISTS `game_manual_talents` (
  `GM_T_ID_MANUAL` int(11) NOT NULL,
  `GM_T_ID_TALENT` int(11) NOT NULL,
  `GM_T_NEW` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`GM_T_ID_MANUAL`,`GM_T_ID_TALENT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dump dei dati per la tabella `game_manual_talents`
--

INSERT INTO `game_manual_talents` (`GM_T_ID_MANUAL`, `GM_T_ID_TALENT`, `GM_T_NEW`) VALUES
(1, 1, 1),
(2, 1, 0),
(2, 2, 1),
(2, 3, 1),
(2, 4, 1),
(2, 5, 1),
(2, 6, 1),
(3, 1, 0),
(3, 2, 0),
(3, 3, 0),
(3, 4, 0),
(3, 5, 0),
(3, 6, 0),
(3, 7, 1),
(3, 8, 1),
(3, 9, 1),
(3, 10, 1),
(3, 11, 1),
(3, 12, 1),
(3, 13, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `languages`
--

CREATE TABLE IF NOT EXISTS `languages` (
  `L_ID` int(11) NOT NULL AUTO_INCREMENT,
  `L_NAME` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`L_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `languages`
--

INSERT INTO `languages` (`L_ID`, `L_NAME`) VALUES
(1, 'English'),
(2, 'Italiano');

-- --------------------------------------------------------

--
-- Struttura della tabella `races`
--

CREATE TABLE IF NOT EXISTS `races` (
  `R_ID` int(11) NOT NULL AUTO_INCREMENT,
  `R_ID_NAME` int(11) NOT NULL,
  `R_MOD_STR` tinyint(4) NOT NULL DEFAULT '0',
  `R_MOD_DEX` tinyint(4) NOT NULL DEFAULT '0',
  `R_MOD_COS` tinyint(4) NOT NULL DEFAULT '0',
  `R_MOD_INT` tinyint(4) NOT NULL DEFAULT '0',
  `R_MOD_SAG` tinyint(4) NOT NULL DEFAULT '0',
  `R_MOD_CAR` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`R_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=10 ;

--
-- Dump dei dati per la tabella `races`
--

INSERT INTO `races` (`R_ID`, `R_ID_NAME`, `R_MOD_STR`, `R_MOD_DEX`, `R_MOD_COS`, `R_MOD_INT`, `R_MOD_SAG`, `R_MOD_CAR`) VALUES
(1, 1, 0, 0, 1, 0, 0, 0),
(2, 2, 0, 2, 0, 0, 0, 0),
(3, 3, 0, 0, 2, 0, 0, 0),
(4, 4, 0, 2, 0, 0, 0, 0),
(5, 5, 2, 0, 0, 0, 0, 1),
(6, 6, 0, 0, 0, 0, 0, 2),
(7, 7, 2, 0, 1, 0, 0, 0),
(8, 8, 0, 0, 0, 2, 1, 0),
(9, 9, 2, 0, 1, -2, 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `races_description`
--

CREATE TABLE IF NOT EXISTS `races_description` (
  `RD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `RD_LAN` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `RD_NAME` varchar(32) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`RD_ID`,`RD_LAN`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=10 ;

--
-- Dump dei dati per la tabella `races_description`
--

INSERT INTO `races_description` (`RD_ID`, `RD_LAN`, `RD_NAME`) VALUES
(1, 1, 'Human'),
(1, 2, 'Umano'),
(2, 1, 'Elf'),
(2, 2, 'Elfo'),
(3, 1, 'Dwarf'),
(3, 2, 'Nano'),
(4, 1, 'Halfling'),
(4, 2, 'Mezzuomo'),
(5, 1, 'Dragonborn'),
(5, 2, 'Dragonide'),
(6, 1, 'Half-Elf'),
(6, 2, 'Mezzelfo'),
(7, 1, 'Half-Orc'),
(7, 2, 'Mezzorco'),
(8, 1, 'Vedalken'),
(8, 2, 'Vedalken'),
(9, 1, 'Orc'),
(9, 2, 'Orco');

-- --------------------------------------------------------

--
-- Struttura della tabella `races_for_classes`
--

CREATE TABLE IF NOT EXISTS `races_for_classes` (
  `RC_ID_RACE` int(11) NOT NULL,
  `RC_ID_CLASS` int(11) NOT NULL,
  PRIMARY KEY (`RC_ID_RACE`,`RC_ID_CLASS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `talents`
--

CREATE TABLE IF NOT EXISTS `talents` (
  `T_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_ID_NAME` int(11) NOT NULL,
  `T_MIN_LVL` int(11) NOT NULL DEFAULT '1',
  `T_MOD_STR` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_DEX` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_COS` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_INT` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_SAG` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_CAR` tinyint(4) NOT NULL DEFAULT '0',
  `T_SAL_TEM` tinyint(4) NOT NULL DEFAULT '0',
  `T_SAL_RIF` tinyint(4) NOT NULL DEFAULT '0',
  `T_SAL_VOL` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_TC` tinyint(4) NOT NULL DEFAULT '0',
  `T_MOD_CA` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`T_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=14 ;

--
-- Dump dei dati per la tabella `talents`
--

INSERT INTO `talents` (`T_ID`, `T_ID_NAME`, `T_MIN_LVL`, `T_MOD_STR`, `T_MOD_DEX`, `T_MOD_COS`, `T_MOD_INT`, `T_MOD_SAG`, `T_MOD_CAR`, `T_SAL_TEM`, `T_SAL_RIF`, `T_SAL_VOL`, `T_MOD_TC`, `T_MOD_CA`) VALUES
(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2),
(2, 2, 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0),
(3, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
(4, 4, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0),
(5, 5, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0),
(6, 6, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
(7, 7, 4, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0),
(8, 8, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0),
(9, 9, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0),
(10, 10, 10, 2, 1, 2, 1, 1, 1, 1, 1, 2, 2, 2),
(11, 11, 3, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0),
(12, 12, 6, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0),
(13, 13, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `talents_description`
--

CREATE TABLE IF NOT EXISTS `talents_description` (
  `TD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TD_LAN` tinyint(3) unsigned NOT NULL,
  `TD_NAME` text COLLATE utf8_bin NOT NULL,
  `TD_DESCRIPTION` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`TD_ID`,`TD_LAN`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=14 ;

--
-- Dump dei dati per la tabella `talents_description`
--

INSERT INTO `talents_description` (`TD_ID`, `TD_LAN`, `TD_NAME`, `TD_DESCRIPTION`) VALUES
(1, 1, 'Heavily Armored', 'Gain the ability to use heavy armors'),
(1, 2, 'Armature Pesanti', 'Proficienza nell''uso di armature pesanti'),
(2, 1, 'Keen Mind', 'You have a mind that can track time'),
(2, 2, 'Mente acuta', 'Possiedi una mente che tiene traccia del tempo'),
(3, 1, 'Lightly Armored ', 'Gain the ability to use light armors'),
(3, 2, 'Armature Leggere', 'Proficienza nell''uso di armature leggere'),
(4, 1, 'Linguist', 'You have studied languages and codes'),
(4, 2, 'Linguista', 'Hai studiato linguaggi e codici'),
(5, 1, 'Second Chance(A)', 'Fortune favors you when someone tries to strike you'),
(5, 2, 'Seconda Possibilita(A)', 'La fortuna ti favorisce quando ricevi un colpo'),
(6, 1, 'Second Chance(B)', 'Fortune favors you when someone tries to strike you'),
(6, 2, 'Seconda Possibilita(B)', 'La fortuna ti favorisce quando ricevi un colpo'),
(7, 1, 'Lightning Reflexes', 'You have faster reflexes than normal'),
(7, 2, 'Riflessi fulminei', 'Il personaggio possiede riflessi piu veloci del normale'),
(8, 1, 'Improved Fighting', 'The player is an expert fighter'),
(8, 2, 'Combattere Migliorato', 'Il personaggio e un esperto nel combattere'),
(9, 1, 'Iron Will', 'You are able to grit your teeth and shake off mental influences'),
(9, 2, 'Volonta di ferro', 'Il personaggio possiede una volonta piu forte del normale'),
(10, 1, 'Sunshine', 'The power of sun irradiates the body'),
(10, 2, 'Splendore', 'Il potere del sole irradia il tuo corpo'),
(11, 1, 'Arcane Intellect', 'Years of study increased your knowledge'),
(11, 2, 'Intelletto Arcano', 'Anni di studio hanno migliorato la tua conoscenza'),
(12, 1, 'Magic Affinity', 'Your magic affinity is higher than normal'),
(12, 2, 'Affinita Magica', 'La tua affinita magica e superiore al normale'),
(13, 1, 'Mighty Temper', 'Resistance to poison, illness and other afflictions'),
(13, 2, 'Tempra Possente', 'Resistenza a veleni, malattie ed altre afflizioni. ');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
