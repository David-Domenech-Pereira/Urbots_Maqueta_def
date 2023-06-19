DROP DATABASE IF EXISTS maqueta_bd;
CREATE DATABASE maqueta_bd;
USE maqueta_bd;

CREATE TABLE `ElementCiutat` (
    `ip` varchar(12) NOT NULL,
    PRIMARY KEY (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `Solar` (
    `ip` varchar(12) NOT NULL,
    `posicio` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_solar_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `Eolica` (
    `ip` VARCHAR(12) NOT NULL,
    `vent` int(5) DEFAULT 0,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_eolica_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `Nuclear` (
    `ip` varchar(12) NOT NULL,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_nuclear_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `Ciutat` (
    `ip` varchar(12) NOT NULL,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_ciutat_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `ElementInteractuar` (
    `id` int(5) NOT NULL AUTO_INCREMENT,
    `potEncendre` tinyint(1) DEFAULT 0, --intentar usar nomes un bit no va molt bé
    `enabled` tinyint(1) DEFAULT 0,
    `ciutat` varchar(12) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_interactuar_element` FOREIGN KEY (`ciutat`) REFERENCES `ElementCiutat` (`ip`)
)  ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO ElementCiutat(ip) VALUES
('192.168.0.10'), /*IP Solar*/
('192.168.0.20'), /*IP Aerogenerador*/
('192.168.0.30'), /*IP Ciutat*/
('192.168.0.40'); /*IP Nuclear*/

SELECT * FROM ElementCiutat;

INSERT INTO ElementInteractuar(potEncendre, enabled, ciutat) VALUES
(1, 1, '192.168.0.10'), --Insert Solar
(0, 0, '192.168.0.10'),
(0, 1, '192.168.0.10'),
(1, 0, '192.168.0.10'),
(1, 1, '192.168.0.10'),
(0, 0, '192.168.0.20'), --Insert Eolic 
(0, 1, '192.168.0.20'),
(1, 0, '192.168.0.20'),
(1, 1, '192.168.0.20'),
(0, 0, '192.168.0.20'),
(1, 1, '192.168.0.30'), --Insert Ciutat
(1, 1, '192.168.0.40'); --Insert Nuclear

SELECT * FROM ElementInteractuar;



