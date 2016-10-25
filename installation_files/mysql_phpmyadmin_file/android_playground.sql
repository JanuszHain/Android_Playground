-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 25 Paź 2016, 12:08
-- Wersja serwera: 10.1.8-MariaDB
-- Wersja PHP: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `android_playground`
--

DELIMITER $$
--
-- Procedury
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadPerson` (IN `id` INT)  READS SQL DATA
SELECT * FROM persons WHERE id = persons.id$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadPersonsWithLimit` (IN `lastId` INT, IN `resultLimit` INT)  READS SQL DATA
SELECT * FROM persons WHERE persons.id > lastId ORDER BY persons.secondName ASC, persons.id DESC LIMIT resultLimit$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadPersonsWithLimitSecName` (IN `lastId` INT, IN `resultLimit` INT, IN `lastSecName` VARCHAR(20))  READS SQL DATA
SELECT * FROM persons WHERE persons.secondName >= lastSecName AND (persons.secondName > lastSecName OR persons.id < lastId) ORDER BY persons.secondName ASC, persons.id DESC LIMIT resultLimit$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadRelationBetweenSetPersons` (IN `idPerson1` INT, IN `idPerson2` INT)  READS SQL DATA
SELECT * FROM relations WHERE (relations.idPerson1 = idPerson1 AND relations.idPerson2 = idPerson2) OR (relations.idPerson1 = idPerson2 AND relations.idPerson2 = idPerson1)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadRelations` (IN `personId` INT)  READS SQL DATA
SELECT * FROM relations WHERE relations.idPerson1 = personId OR relations.idPerson2 = personId$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadRelationsWithLimit` (IN `idPerson` INT(2), IN `resultLimit` INT(5), IN `lastId` INT(1))  READS SQL DATA
SELECT * FROM relations WHERE relations.id > lastId AND (relations.idPerson1 = idPerson OR relations.idPerson2 = idPerson) LIMIT resultLimit$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `LoadRestOfRelations` (IN `personId` INT, IN `lastId` INT)  READS SQL DATA
SELECT * FROM relations WHERE (relations.idPerson1 = personId OR relations.idPerson2 = personId) AND relations.id > lastId$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `persons`
--

CREATE TABLE `persons` (
  `id` int(10) UNSIGNED NOT NULL,
  `firstName` varchar(20) NOT NULL,
  `secondName` varchar(20) NOT NULL,
  `birthDate` date NOT NULL,
  `city` varchar(30) NOT NULL,
  `photoUrl` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `persons`
--

INSERT INTO `persons` (`id`, `firstName`, `secondName`, `birthDate`, `city`, `photoUrl`) VALUES
(1, 'Janusz', 'Hain', '1993-10-15', 'Pleszew', 'http://www.fancyicons.com/free-icons/103/pretty-office-4/png/256/man1_256.png'),
(2, 'Adam', 'Kowalski', '1998-10-04', 'Kowalewo', 'http://www.fancyicons.com/free-icons/103/pretty-office-4/png/256/man1_256.png'),
(3, 'Aleksander', 'Hain', '1992-10-11', 'Warszawa', ''),
(4, 'Ala', 'Kowalczyk', '1992-10-04', 'Warszawa', ''),
(6, 'Ola', 'Chryzamtewicz', '1991-10-04', 'Poznan', ''),
(7, 'Mirek', 'Wróblewski', '1990-10-02', 'Wroclaw', ''),
(8, 'Janek', 'Ziemiewicz', '1982-02-04', 'Opole', ''),
(9, 'Kasia', 'Brzeczyszczykiewicz', '1984-07-11', 'Lublin', 'http://www.relpe.org/wp-content/uploads/2014/09/Office-Client-Female-Light-icon.png'),
(10, 'Ela', 'Mroczek', '1999-10-17', 'Mroczkowo', ''),
(11, 'Zosia', 'Amberylska', '1968-10-17', 'Gdansk', ''),
(12, 'Pepe', 'Pepowsky', '2005-01-01', 'Dankville', 'http://owlgrenade.com/pepe/r9k/png/__(66).png'),
(13, 'Arkadiusz', 'Sikorski', '1999-10-17', 'Sopot', ''),
(14, 'Monika', 'Lubowicz', '1984-10-09', 'Warszawa', '');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `relations`
--

CREATE TABLE `relations` (
  `id` int(10) UNSIGNED NOT NULL,
  `idPerson1` int(10) UNSIGNED NOT NULL,
  `idPerson2` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `relations`
--

INSERT INTO `relations` (`id`, `idPerson1`, `idPerson2`) VALUES
(1, 1, 2),
(2, 3, 1),
(3, 2, 3),
(4, 4, 6),
(5, 7, 6),
(6, 8, 9),
(7, 4, 9),
(8, 4, 7),
(9, 4, 8),
(10, 10, 7),
(11, 11, 8),
(12, 12, 1),
(13, 12, 2);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `persons`
--
ALTER TABLE `persons`
  ADD PRIMARY KEY (`id`),
  ADD KEY `firstName` (`firstName`),
  ADD KEY `secondName` (`secondName`);

--
-- Indexes for table `relations`
--
ALTER TABLE `relations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idPerson1` (`idPerson1`),
  ADD KEY `idPerson2` (`idPerson2`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `persons`
--
ALTER TABLE `persons`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT dla tabeli `relations`
--
ALTER TABLE `relations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
