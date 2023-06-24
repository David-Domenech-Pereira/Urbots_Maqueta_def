CREATE TABLE `ElementCiutat` (
    `ip` varchar(12) NOT NULL,
    PRIMARY KEY (`ip`)
);

CREATE TABLE `Solar` (
    `ip` varchar(12) NOT NULL,
    `posicio` int(5) DEFAULT 0,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_solar_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `Eolica` (
    `ip` VARCHAR(12) NOT NULL,
    `vent` int(5) DEFAULT 20,
    `energia` int(5) DEFAULT 0,
    PRIMARY KEY (`ip`),
    CONSTRAINT `fk_eolica_element` FOREIGN KEY (`ip`) REFERENCES `ElementCiutat` (`ip`)
);

CREATE TABLE `Nuclear` (
    `ip` varchar(12) NOT NULL,
    `energia` int(5) DEFAULT 0,
    `potencia` int(5) DEFAULT 0,
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
    `id` INTEGER PRIMARY KEY,
    `potEncendre` INTEGER(1) DEFAULT 0,
    `enabled` INTEGER(1) DEFAULT 0,
    `ciutat` varchar(12) NOT NULL,
    CONSTRAINT `fk_interactuar_element` FOREIGN KEY (`ciutat`) REFERENCES `ElementCiutat` (`ip`)
);

INSERT INTO ElementCiutat(ip)
VALUES ('192.168.0.10'),
    ('192.168.0.20'),
    ('192.168.0.30'),
    ('192.168.0.40');

INSERT INTO Solar(ip, posicio, energia)
VALUES ('192.168.0.10', 0, 0);

INSERT INTO Eolica(ip, vent, energia)
VALUES ('192.168.0.20', 0, 0);

INSERT INTO Nuclear(ip, energia)
VALUES ('192.168.0.30', 0);

INSERT INTO Ciutat(ip, energia)
VALUES('192.168.0.40', 0);

INSERT INTO ElementInteractuar(potEncendre, enabled, ciutat)
VALUES (1, 1, '192.168.0.10'),
    (1, 0, '192.168.0.10'),
    (1, 1, '192.168.0.10'),
    (1, 0, '192.168.0.10'),
    (1, 1, '192.168.0.10'),
    (1, 0, '192.168.0.20'),
    (1, 1, '192.168.0.20'),
    (1, 0, '192.168.0.20'),
    (1, 1, '192.168.0.20'),
    (1, 0, '192.168.0.20'),
    (1, 1, '192.168.0.30'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40'),
    (1, 1, '192.168.0.40');

CREATE VIEW IF NOT EXISTS vSolar (ip, posicio, energia) AS SELECT S.posicio,
    S.energia,
    E.ciutat FROM ElementInteractuar E,
    Solar S WHERE E.ciutat = '192.168.0.10';

CREATE VIEW IF NOT EXISTS vEolica (ip, vent, energia) AS SELECT O.vent,
    O.energia,
    E.ciutat FROM ElementInteractuar AS E,
    Eolica AS O WHERE E.ciutat = '192.168.20';

CREATE VIEW IF NOT EXISTS vNuclear (ip, energia) AS SELECT N.energia,
    E.ciutat FROM ElementInteractuar AS E,
    Nuclear AS N WHERE E.ciutat = '192.168.40';

CREATE VIEW IF NOT EXISTS vCiutat (ip, energia) AS SELECT C.energia,
    E.ciutat FROM ElementInteractuar AS E,
    Ciutat AS C WHERE E.ciutat = '192.168.30';