***TABELLE***

**- UTENTE:**
```SQL
CREATE TABLE IF NOT EXISTS UTENTE
(
    idUtente SERIAL,
    login varchar(25) UNIQUE,
    password varchar(25),
    nome varchar(30),
    cognome varchar(30),
    dataNascita Date,
    email varchar(50) check(email LIKE '%@%.%'),
    ruolo char(6) DEFAULT 'Utente',
    CONSTRAINT pk_utente PRIMARY KEY(idUtente)
)
```
**- PAGINA:**
```SQL
CREATE TABLE Pagina
(
    idPagina SERIAL,
    titolo varchar(80),
    dataOraCreazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    idAutore int NOT NULL,
    constraint pk_pagina PRIMARY KEY(idPagina),
    constraint Fk_utente FOREIGN KEY(idAutore) REFERENCES utente(idUtente)
)
```
**- VISIONA:**
```SQL
CREATE TABLE Visiona
(
    idUtente int,
    idPagina int,
    dataVisone DATE DEFAULT CURRENT_DATE,
    oraVisione TIME DEFAULT CURRENT_TIME,
    CONSTRAINT pk_Visona PRIMARY KEY(idUtente, idPagina),
    CONSTRAINT Fk_Utente FOREIGN KEY(idUtente) REFERENCES UTENTE(idUtente),
    CONSTRAINT Fk_pagina FOREIGN KEY(idPagina) REFERENCES PAGINA(idpagina)
)
```
**- FRASE CORRENTE**
```SQL
CREATE TABLE fraseCorrente
(
    StringaInserita varchar(1000),
    numerazione int,
    dataInserimento DATE DEFAULT CURRENT_DATE,
    oraInserimento TIME DEFAULT CURRENT_TIME,
    idPagina int,
    collegamento pk_pagina,
    CONSTRAINT pk_frase PRIMARY KEY(StringaInserita, numerazione, idPagina),
    CONSTRAINT fk_pagina FOREIGN KEY(idPagina) REFERENCES PAGINA(idPagina),
    CONSTRAINT fk_collegamento FOREIGN KEY(collegamento) REFERENCES PAGINA(idPagina)
)
```
**- MODIFICA PROPOSTA**
```SQL
CREATE TABLE ModificaProposta
(
    idModifica Serial,
    stringaProposta varchar(1000),
    stato int DEFAULT 0,
    dataProposta DATE DEFAULT CURRENT_DATE,
    oraProposta TIME DEFAULT CURRENT_TIME,
    dataValutazione DATE,
    oraValutazione TIME,
    UtenteP int NOT NULL,
    AutoreV int NOT NULL,
    StringaInserita varchar(1000),
    numerazione int,
    idPagina int,
    CONSTRAINT pk_modifica PRIMARY KEY(idModifica),
    CONSTRAINT fk_stringaInserita FOREIGN KEY(StringaInserita, numerazione, idPagina) REFERENCES fraseCorrente(stringaInserita, numerazione, idPagina) DELETE ON CASCADE,
    CONSTRAINT fk_AutoreV FOREIGN KEY(AutoreV) REFERENCES UTENTE(idUtente),
    CONSTRAINT fk_UtenteP FOREIGN KEY(utenteP) REFERENCES UTENTE(idUtente)
)
```
**- NOTIFICA**
```SQL
CREATE TABLE NOTIFICA 
(
    idPagina pk_utente NOT NULL,
    idUtente pk_utente NOT NULL,
    data DATE,
    ora TIME,
    idAutore pk_utente NOT NULL,
    titolo varchar(80),
    FOREIGN KEY(idPagina) REFERENCES pagina(idPagina),
    FOREIGN KEY(idUtente) REFERENCES utente(idUtente),
    FOREIGN KEY(idAutore) REFERENCES utente(idUtente)
)
```
***TRIGGER***

**- Aggiunge una tupla all'interno di notifica ogni qualvolta viene proposta una modifica**
```SQL
CREATE OR REPLACE FUNCTION notificaM() RETURNS TRIGGER AS $$
DECLARE
    v_titolo VARCHAR(80);
BEGIN
    -- Controlla se la riga 'new' contiene valori non nulli
    IF new.idpagina IS NOT NULL AND new.utentep IS NOT NULL AND new.autorev IS NOT NULL THEN
        -- Recupera il titolo della pagina
        SELECT titolo INTO v_titolo
        FROM pagina
        WHERE idpagina = new.idpagina;

        -- Controlla se il titolo non è nullo
        IF v_titolo IS NOT NULL THEN
            IF new.utentep != (SELECT idautore FROM Pagina WHERE idPagina = new.idPagina) THEN
                -- Inserisci un nuovo record nella tabella NOTIFICA
                INSERT INTO NOTIFICA
                    VALUES (new.IdPagina, new.utentep, NOW(), NOW(), new.autorev, v_titolo);
                RAISE NOTICE 'è stata proposta una modifica alla pagina ';
                RETURN new;
            ELSE
                RETURN NULL;
            END IF;
        ELSE
            -- Gestisci il caso in cui il titolo è nullo
            RAISE EXCEPTION 'Titolo non trovato per idpagina: %', new.idpagina;
        END IF;
    ELSE
        -- Gestisci il caso in cui la riga 'new' contiene valori nulli
        RAISE EXCEPTION 'Valori NULL trovati nella nuova riga';
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER Notifica_modifica
AFTER INSERT ON ModificaProposta
FOR EACH ROW
    EXECUTE function notificaM();
```
**- quando un utente scrive per la prima volta una pagina, il suo ruolo passa da _utente_ ad _autore_**
```SQL
CREATE OR REPLACE FUNCTION diventaAutore() RETURNS TRIGGER AS $$
DECLARE
    ruoloUtente Utente.ruolo%TYPE;
BEGIN
   SELECT ruolo INTO ruoloUtente
   FROM UTENTE 
   WHERE idUtente = new.idAutore;

   IF ruoloUtente != 'Autore' THEN
           UPDATE Utente SET ruolo = 'Autore' WHERE idutente = new.idautore;
        RETURN new;
    ELSE
        RETURN NULL;
   END IF;
END;
$$ LANGUAGE plpgsql; 

CREATE OR REPLACE TRIGGER diventaAutore 
AFTER INSERT ON pagina
FOR EACH ROW
    EXECUTE FUNCTION diventaAutore();
```
**- quando una proposta di modifica viene fatto dall'autore della pagina, la proposta viene direttamente accettata**
```SQL
REATE OR REPLACE FUNCTION ModificaAutore ()
RETURNS TRIGGER
AS $$
BEGIN
    IF new.utentep = new.autorev THEN
        UPDATE modificaproposta SET stato = 1 WHERE idmodifica = new.idmodifica;
        RETURN new;
    ELSE
        RETURN NULL;
    END IF ;
END;
$$
language plpgsql;

CREATE OR REPLACE TRIGGER ModificaAutore
AFTER INSERT ON modificaproposta
FOR EACH ROW
EXECUTE FUNCTION ModificaAutore()
```
**- quando viene inserita una nuova frase all'interno di una pagina, la numerazione viene impostata dal trigger**
```SQL
CREATE OR REPLACE FUNCTION inserimento_frase() RETURNS TRIGGER
AS $$
DECLARE
valore INTEGER = -1;
BEGIN
    SELECT numerazione INTO valore
    FROM frasecorrente
    WHERE new.idpagina = idpagina
    ORDER BY numerazione DESC LIMIT 1;
    IF valore != -1 THEN 
           new.numerazione = valore+1;
    ELSE
        new.numerazione = 0;
    END IF;
    RETURN new;
END;
$$
language plpgsql;

CREATE OR REPLACE TRIGGER inserimento_frase
BEFORE INSERT ON frasecorrente
FOR EACH ROW
EXECUTE FUNCTION inserimento_frase()
```
    
