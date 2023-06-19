--He tret el drop y create perqué peta

CREATE TABLE `ElementCiutat` (
    `ip` varchar(12) NOT NULL,
    PRIMARY KEY (`ip`)
); --es veu que no accepta el engine y el tipus de codificació

CREATE TABLE `Solar` (
    `ip` varchar(12) NOT NULL,
    `posicio` int(5) DEFAULT 0,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_solar_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `Eolica` (
    `ip` VARCHAR(12) NOT NULL,
    `vent` int(5) DEFAULT 0,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_eolica_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `Nuclear` (
    `ip` varchar(12) NOT NULL,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_nuclear_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `Ciutat` (
    `ip` varchar(12) NOT NULL,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_ciutat_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `ElementInteractuar` (
    `id` INTEGER PRIMARY KEY, -- per algun motiu això va així
    `potEncendre` INTEGER(1) DEFAULT 0, --intentar usar nomes un bit no va molt bé
    `enabled` INTEGER(1) DEFAULT 0,
    `ciutat` varchar(12) NOT NULL,
    CONSTRAINT `fk_interactuar_element` FOREIGN KEY (`ciutat`) REFERENCES `ElementCiutat` (`ip`)
);

INSERT INTO ElementCiutat(ip) VALUES
('192.168.0.10'), --IP Solar*/
('192.168.0.20'), --IP Aerogenerador*/
('192.168.0.30'), --IP Ciutat*/
('192.168.0.40'); --IP Nuclear*/
INSERT INTO Solar(ip, posicio, energia) VALUES ('192.168.0.10',0,0)
INSERT INTO Eolica(ip,posicio,energia) VALUES ('192.168.0.20',0,0)
INSERT INTO Nuclear(ip,energia) VALUES ('192.168.0.30',0)
INSERT INTO Ciutat(ip, energia) VALUES('192.168.0.40',0)
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




