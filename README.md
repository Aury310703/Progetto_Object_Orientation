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
    idAutore pk_utente NOT NULL,
	idModifica pk_utente NOT NULL,
    data DATE,
    ora TIME,
    titolo varchar(80),
    FOREIGN KEY(idModifica) REFERENCES modificaproposta(idModifica),
    FOREIGN KEY(idAutore) REFERENCES utente(idUtente)
)
```
**- COLLEGAMENTO**
```SQL
CREATE TABLE COLLEGAMENTO
(
	stringaInserita varchar(1000),
	numerazione INTEGER,
	idPagina pk_utente,
	paginaCollegata pk_utente,
	PRIMARY KEY(StringaInserita, numerazione, idPagina, paginaCollegata),
	CONSTRAINT pk_fraseInserita FOREIGN KEY(StringaInserita, numerazione, idPagina) REFERENCES fraseCorrente(stringaInserita, numerazione, idPagina)
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
                    VALUES (new.autorev, new.idModifica, NOW(), NOW(), v_titolo);
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

**- setta la data di valutazione e l'ora di valutazione quando viene accettata o rifiutata una proposta di modifica**
```SQL
CREATE OR REPLACE settaggioDataOraValutazione() RETURNS TRIGGER
AS $$
BEGIN
	UPDATE modificaProposta SET dataValutazione = NOW() WHERE idModifica = old.idModifica;
	UPDATE modificaProposta SET oraValutazione = NOW() WHERE idModifica = old.idModifica;
	RETURN new;
END;

CREATE OR REPLACE TRIGGER settaggioDataOraValutazione
AFTER UPDATE modificaProposta
FOR EACH ROW
WHEN (old.stato = 0)
	EXECUTE FUNCTION settaggioDataOraValutazione();
 ```
***PROCEDURE***

**- data una data inserita si visualizza la versione piú recente fino a quella data**
```SQL
CREATE OR REPLACE PROCEDURE ricostruzione_versione(pagina IN pagina.idpagina%type, dataInsertita IN DATE)
AS $$
DECLARE
    frase varchar(1000);
    num INTEGER;
    dataVal DATE;
    sProposta varchar(1000);
    k RECORD;
BEGIN
    CREATE TABLE tempVersione
    (
        stringa varchar(1000),
        numerazione INTEGER
    );

    FOR k IN (SELECT stringaInserita, numerazione, datainserimento FROM frasecorrente WHERE pagina = idpagina) LOOP
        SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
        FROM modificaproposta 
        WHERE pagina = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1 AND datavalutazione <= dataInsertita
        ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
        IF num IS NULL THEN 
            INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
        ELSE
            INSERT INTO tempVersione VALUES (sProposta, num);
        END IF;
    END LOOP;

    DROP TABLE tempVersione;
END;
$$
LANGUAGE PLPGSQL;
```
