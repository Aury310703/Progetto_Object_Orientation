PGDMP  4    5                |            progetto    16.1    16.1 F    =           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            >           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            ?           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            @           1262    25605    progetto    DATABASE     {   CREATE DATABASE progetto WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE progetto;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            A           0    0    SCHEMA public    COMMENT         COMMENT ON SCHEMA public IS '';
                   postgres    false    6            B           0    0    SCHEMA public    ACL     +   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
                   postgres    false    6                        3079    25606 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            C           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            a           1247    25617    chiavi_primarie    DOMAIN     1   CREATE DOMAIN public.chiavi_primarie AS integer;
 $   DROP DOMAIN public.chiavi_primarie;
       public          postgres    false    6            �            1255    25618    crea_pagina(text, text, text) 	   PROCEDURE     �  CREATE PROCEDURE public.crea_pagina(IN titoloscelto text, IN nomeutente text, IN testopagina text)
    LANGUAGE plpgsql
    AS $$
DECLARE
	frase varchar(100000) := '';
	pos_prec INTEGER := 1;
	pagina pagina.idPagina%TYPE;
	autore utente.idUtente%TYPE;
	pos INTEGER := 1;
	num INTEGER := 0;
	H RECORD;
	J RECORD;
    k RECORD;
BEGIN
	SELECT idUtente INTO autore FROM utente WHERE LOWER(login) = LOWER(nomeUtente) LIMIT 1;
	
	INSERT INTO PAGINA(titolo, idAutore) VALUES (titoloScelto, autore);
	SELECT idPagina INTO pagina FROM PAGINA WHERE PAGINA.titolo = titoloScelto AND PAGINA.idAutore = autore AND PAGINA.dataOraCreazione = now();

	LOOP
		EXIT WHEN pos > LENGTH(testoPagina);
		IF SUBSTRING(testoPagina FROM pos FOR 1) IN ('.', ',', ';', '!', '?') THEN
            frase := REGEXP_REPLACE(SUBSTRING(testoPagina FROM pos_prec FOR (pos - pos_prec)+1), '^\s+', '');
			pos_prec := pos + 1;
			CALL inserimento_frase(frase, pagina);
			RAISE NOTICE '%', frase;
		END IF;
		pos := pos + 1;
	END LOOP;

	IF pos_prec < LENGTH(testoPagina) THEN
        frase := REGEXP_REPLACE(SUBSTRING(testoPagina FROM pos_prec FOR LENGTH(testoPagina)), '^\s+', '');
		frase := frase || '.'; 
		CALL inserimento_frase(frase, pagina);
		RAISE NOTICE '%', frase;
	END IF;
END;
$$;
 b   DROP PROCEDURE public.crea_pagina(IN titoloscelto text, IN nomeutente text, IN testopagina text);
       public          postgres    false    6            �            1255    25619    diventaautore()    FUNCTION     |  CREATE FUNCTION public.diventaautore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
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
$$;
 &   DROP FUNCTION public.diventaautore();
       public          postgres    false    6            �            1255    25620    frase_collegamento(integer) 	   PROCEDURE       CREATE PROCEDURE public.frase_collegamento(IN paginaselezionata integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
	titoloPagina TEXT;
	num INTEGER;
	dataVal DATE;
	sProposta TEXT;
	pCollegata chiavi_primarie;
	controllo INTEGER := -1;
	K RECORD;

BEGIN 

		
	SELECT titolo INTO titoloPagina FROM PAGINA WHERE idpagina = paginaSelezionata;
	RAISE INFO '%', 'TITOLO PAGINA: ' || titoloPagina;

    FOR k IN (SELECT stringaInserita, numerazione FROM frasecorrente WHERE paginaSelezionata = idpagina ORDER BY numerazione ASC) LOOP
        SELECT paginacollegata INTO pCollegata FROM COLLEGAMENTO WHERE paginaSelezionata = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione;
		IF pCollegata IS NOT NULL THEN
		controllo = 1;
			SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
			FROM modificaproposta 
			WHERE paginaSelezionata = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1
			ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
			IF num IS NULL THEN 
				SELECT titolo INTO titoloPagina FROM PAGINA WHERE idpagina = pcollegata;
				RAISE INFO '%', k.numerazione || ' - ' || k.stringainserita || ' -> ' || titoloPagina;
			ELSE
				SELECT titolo INTO titoloPagina FROM PAGINA WHERE idpagina = pcollegata;
				RAISE INFO '%', k.numerazione || ' - ' || sproposta || ' -> ' || titoloPagina;
			END IF;
		END IF;
		END LOOP;
		
		IF controllo = -1 THEN
			RAISE INFO '%', 'nessuna frase ha un collegamento ad una pagina';
		END IF;

END
$$;
 H   DROP PROCEDURE public.frase_collegamento(IN paginaselezionata integer);
       public          postgres    false    6            �            1255    25621    inserimento_frase()    FUNCTION     w  CREATE FUNCTION public.inserimento_frase() RETURNS trigger
    LANGUAGE plpgsql
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
$$;
 *   DROP FUNCTION public.inserimento_frase();
       public          postgres    false    6            �            1255    25622 -   inserimento_frase(character varying, integer) 	   PROCEDURE     c  CREATE PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
valore INTEGER;
BEGIN
	SELECT numerazione INTO valore
	FROM frasecorrente
	WHERE pagina = idpagina
	ORDER BY numerazione DESC LIMIT 1;
	INSERT INTO frasecorrente (stringainserita, idpagina) VALUES (stringa, pagina);
END;
$$;
 Z   DROP PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer);
       public          postgres    false    6            �            1255    25623    modificaautore()    FUNCTION       CREATE FUNCTION public.modificaautore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF new.utentep = new.autorev THEN
		UPDATE modificaproposta SET stato = 1 WHERE idmodifica = new.idmodifica;
		RETURN new;
	ELSE
		RETURN NULL;
	END IF ;
END;
$$;
 '   DROP FUNCTION public.modificaautore();
       public          postgres    false    6            �            1255    25624    numerazione_frasi(integer) 	   PROCEDURE     �  CREATE PROCEDURE public.numerazione_frasi(IN paginaselezionata integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    frase varchar(1000);
    num INTEGER;
    dataVal DATE;
	dataCreazione DATE;
    sProposta varchar(1000);
    k RECORD;
BEGIN
    CREATE TABLE tempVersione
    (
        stringa TEXT,
        numerazione INTEGER
    );	
    FOR k IN (SELECT stringaInserita, numerazione, datainserimento FROM frasecorrente WHERE paginaSelezionata = idpagina) LOOP
		SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
        FROM modificaproposta 
        WHERE paginaSelezionata = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1
        ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
        IF num IS NULL THEN 
            INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
        ELSE
            INSERT INTO tempVersione VALUES (sProposta, num);
        END IF;
    END LOOP;
	
 FOR k IN (SELECT * FROM tempVersione ORDER BY numerazione ASC) LOOP
			RAISE INFO '%', K.numerazione || ' - ' || k.stringa;
 END LOOP;
   
    DROP TABLE tempVersione;
END;
$$;
 G   DROP PROCEDURE public.numerazione_frasi(IN paginaselezionata integer);
       public          postgres    false    6            �            1255    25625 (   numero_modifiche(public.chiavi_primarie)    FUNCTION       CREATE FUNCTION public.numero_modifiche(utente public.chiavi_primarie) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    modifiche INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO modifiche
    FROM MODIFICAPROPOSTA
    WHERE utentep = utente;

    RETURN modifiche;
END;
$$;
 F   DROP FUNCTION public.numero_modifiche(utente public.chiavi_primarie);
       public          postgres    false    865    6            �            1255    25626    numero_notifiche(integer)    FUNCTION       CREATE FUNCTION public.numero_notifiche(autore integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    notifiche INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO notifiche
    FROM MODIFICAPROPOSTA
    WHERE stato = 0 AND autorev = autore;

    RETURN notifiche;
END;
$$;
 7   DROP FUNCTION public.numero_notifiche(autore integer);
       public          postgres    false    6            �            1255    25627 A   proponi_modifica(character varying, integer, text, text, integer) 	   PROCEDURE     s  CREATE PROCEDURE public.proponi_modifica(IN loginutente character varying, IN paginaselezionata integer, IN frasedamodificare text, IN frasedaproporre text, IN numfrase integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    autore Utente.idUtente%TYPE;
    utente Utente.idUtente%TYPE;
    fraseInserita TEXT := '';
BEGIN
    SELECT idUtente INTO utente FROM Utente WHERE login = loginUtente;
    SELECT idAutore INTO autore FROM Pagina WHERE idPagina = paginaSelezionata;

    SELECT stringaInserita INTO fraseInserita FROM FraseCorrente WHERE idpagina = paginaSelezionata AND fraseDaModificare = stringaInserita AND numerazione = numFrase LIMIT 1;
	
	IF fraseInserita IS NULL THEN
        SELECT stringaInserita INTO fraseInserita FROM ModificaProposta WHERE idpagina = paginaSelezionata AND fraseDaModificare = stringaProposta AND numerazione = numFrase LIMIT 1;
	END IF;

    INSERT INTO ModificaProposta(stringaProposta, utenteP, autoreV, stringaInserita, numerazione, idPagina)
    VALUES (fraseDaProporre, utente, autore, fraseInserita, numFrase, paginaSelezionata);

    RAISE INFO '%', 'Operazione completata con successo';
END;
$$;
 �   DROP PROCEDURE public.proponi_modifica(IN loginutente character varying, IN paginaselezionata integer, IN frasedamodificare text, IN frasedaproporre text, IN numfrase integer);
       public          postgres    false    6            �            1255    25628     ricerca_testi(character varying) 	   PROCEDURE     �  CREATE PROCEDURE public.ricerca_testi(IN titoloinserito character varying)
    LANGUAGE plpgsql
    AS $$
DECLARE
	frase varchar(100000) := '';
	titolotesto varchar(100);
	stringa varchar(1000);
	pagina pagina.idpagina%type;
    num INTEGER;
    dataVal DATE;
    sProposta varchar(1000);
	H RECORD;
	J RECORD;
    k RECORD;
BEGIN
	FOR H IN (SELECT idPagina, titolo FROM PAGINA WHERE LOWER(titolo) LIKE '%' || LOWER(titoloInserito) || '%' ORDER BY titolo) LOOP
	RAISE INFO '%', 'TITOLO PAGINA:';
	RAISE INFO '%', H.titolo;
	
	CREATE TABLE tempVersione
    (
        stringaIns varchar(1000),
        numerazione INTEGER
    );
	

		FOR k IN (SELECT stringaInserita, numerazione, datainserimento FROM frasecorrente WHERE H.idpagina = idpagina ORDER BY numerazione ASC) LOOP
			SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
			FROM modificaproposta 
			WHERE H.idpagina = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1
			ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
			IF num IS NULL THEN 
				INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
			ELSE
				INSERT INTO tempVersione VALUES (sProposta, num);
			END IF;
		END LOOP;
		FOR J IN (SELECT stringaIns FROM tempVersione) LOOP
			stringa := J.stringaIns;
			frase := frase || ' ' || stringa;
		END LOOP;
		RAISE INFO '%', frase;
		RAISE INFO '%', '';
		frase := '';
		DROP TABLE tempVersione;
	END LOOP;
	
END;
$$;
 J   DROP PROCEDURE public.ricerca_testi(IN titoloinserito character varying);
       public          postgres    false    6            �            1255    25629 %   ricostruzione_versione(integer, date) 	   PROCEDURE     �  CREATE PROCEDURE public.ricostruzione_versione(IN paginaselezionata integer, IN datainsertita date)
    LANGUAGE plpgsql
    AS $$
DECLARE
    frase varchar(1000);
    num INTEGER;
    dataVal DATE;
	dataCreazione DATE;
    sProposta varchar(1000);
	testo TEXT:= '';
    k RECORD;
BEGIN
    CREATE TABLE tempVersione
    (
        stringa TEXT,
        numerazione INTEGER
    );
	SELECT dataoracreazione::DATE INTO dataCreazione FROM pagina WHERE paginaSelezionata = idpagina;
	
	IF dataInsertita < dataCreazione THEN
		RAISE INFO 'data precedente alla creazione del testo';
		RAISE INFO 'TESTO ORIGINALE:';
	END IF;
    FOR k IN (SELECT stringaInserita, numerazione, datainserimento FROM frasecorrente WHERE paginaSelezionata = idpagina) LOOP
		SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
        FROM modificaproposta 
        WHERE paginaSelezionata = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1 AND datavalutazione <= dataInsertita
        ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
        IF num IS NULL THEN 
            INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
        ELSE
            INSERT INTO tempVersione VALUES (sProposta, num);
        END IF;
    END LOOP;
	
 FOR k IN (SELECT * FROM tempVersione ORDER BY numerazione ASC) LOOP
			testo := testo || k.stringa || ' ';
    END LOOP;
    RAISE INFO '%', testo;
    DROP TABLE tempVersione;
END;
$$;
 c   DROP PROCEDURE public.ricostruzione_versione(IN paginaselezionata integer, IN datainsertita date);
       public          postgres    false    6            �            1255    25630    settaggiodataoravalutazione()    FUNCTION     9  CREATE FUNCTION public.settaggiodataoravalutazione() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	
BEGIN
	UPDATE modificaProposta SET dataValutazione = NOW() WHERE idModifica = old.idModifica;
	UPDATE modificaProposta SET oraValutazione = NOW() WHERE idModifica = old.idModifica;
	RETURN new;
END;
$$;
 4   DROP FUNCTION public.settaggiodataoravalutazione();
       public          postgres    false    6            �            1255    25631 1   visiona_modificheproposte(public.chiavi_primarie) 	   PROCEDURE     �  CREATE PROCEDURE public.visiona_modificheproposte(IN utente public.chiavi_primarie)
    LANGUAGE plpgsql
    AS $$
DECLARE
	modifiche INTEGER := 0;
	fraseProposta varchar(100):= '';
	K RECORD;
	titoloNotifica TEXT;
BEGIN
	modifiche = numero_modifiche(utente);
	
	RAISE INFO '%', 'Hai proposto ' || modifiche || ' modifiche';
	RAISE INFO '%', '';
	IF modifiche > 0 THEN 
		FOR K IN (SELECT * FROM modificaProposta WHERE utentep = utente ORDER BY DataProposta ASC, oraProposta ASC)
		LOOP
			SELECT stringaProposta INTO fraseProposta FROM modificaProposta WHERE utentep = utente AND idPagina = K.idPagina AND numerazione = K.numerazione AND stato = 1 AND datavalutazione <= K.dataProposta AND oravalutazione < K.oraProposta ORDER BY datavalutazione DESC, oravalutazione DESC LIMIT 1;
			
			RAISE INFO '%', '---------------------------------------------------------------------------------------------------------------------------------';
			SELECT titolo INTO titoloNotifica FROM PAGINA WHERE idPagina = K.idpagina;
			RAISE INFO '%', 'TITOLO PAGINA: ' || titoloNotifica;
			IF fraseProposta != '' THEN
				RAISE INFO '%', 'FRASE SELEZIONATA: ' || fraseproposta;
			ELSE
				RAISE INFO '%', 'FRASE SELEZIONATA: ' || K.stringaInserita;
			END IF;
		
			RAISE INFO '%', 'FRASE PROPOSTA: ' || K.stringaProposta;
			
			IF K.stato = 0 THEN
				RAISE INFO '%', 'STATO: non valutata';
			ELSIF K.stato = 1 THEN
				RAISE INFO '%', 'STATO: accettata';
			ELSE
				RAISE INFO '%', 'STATO: rifutata';
			END IF;
			
			RAISE INFO '%', '';
		END LOOP;
		RAISE INFO '%', '---------------------------------------------------------------------------------------------------------------------------------';
	END IF;
END;
$$;
 S   DROP PROCEDURE public.visiona_modificheproposte(IN utente public.chiavi_primarie);
       public          postgres    false    6    865            �            1255    25632    visiona_notifiche(integer) 	   PROCEDURE     �  CREATE PROCEDURE public.visiona_notifiche(IN autore integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
	notifiche INTEGER := 0;
	fraseProposta varchar(100):= '';
	K RECORD;
	titoloNotifica TEXT;
BEGIN
	notifiche = numero_notifiche(autore);
	
	RAISE INFO '%', 'Ci sono ' || notifiche || ' notifiche da visualizare';
	RAISE INFO '%', '';
	IF notifiche > 0 THEN 
		FOR K IN (SELECT * FROM modificaProposta WHERE stato = 0 AND autorev = autore ORDER BY DataProposta ASC, oraProposta ASC)
		LOOP
			SELECT stringaProposta INTO fraseProposta FROM modificaProposta WHERE autorev = autore AND idPagina = K.idPagina AND numerazione = K.numerazione AND stato = 1 AND datavalutazione <= K.dataProposta AND oravalutazione < K.oraProposta ORDER BY datavalutazione DESC, oravalutazione DESC LIMIT 1;
			
			RAISE INFO '%', '---------------------------------------------------------------------------------------------------------------------------------';
			SELECT titolo INTO titoloNotifica FROM PAGINA WHERE idPagina = K.idpagina;
			RAISE INFO '%', 'TITOLO PAGINA: ' || titoloNotifica;
			IF fraseProposta != '' THEN
				RAISE INFO '%', 'FRASE SELEZIONATA: ' || fraseproposta;
			ELSE
				RAISE INFO '%', 'FRASE SELEZIONATA: ' || K.stringaInserita;
			END IF;
		
			RAISE INFO '%', 'FRASE PROPOSTA: ' || K.stringaProposta;
			RAISE INFO '%', '';
		END LOOP;
		RAISE INFO '%', '---------------------------------------------------------------------------------------------------------------------------------';
	END IF;
END;
$$;
 <   DROP PROCEDURE public.visiona_notifiche(IN autore integer);
       public          postgres    false    6            �            1255    25633 3   visiona_testo(character varying, character varying) 	   PROCEDURE     t  CREATE PROCEDURE public.visiona_testo(IN titoloinserito character varying, IN nomeutente character varying)
    LANGUAGE plpgsql
    AS $$
DECLARE
	frase varchar(100000) := '';
	titolotesto varchar(100);
	stringa varchar(1000);
	pagina pagina.idpagina%type;
    num INTEGER;
    dataVal DATE;
    sProposta varchar(1000);
	visualizzatore utente.idUtente%TYPE;
    k RECORD;
BEGIN
CREATE TABLE tempVersione
    (
        stringaIns varchar(1000),
        numerazione INTEGER
    );
	
	SELECT idPagina, titolo INTO pagina, titolotesto FROM PAGINA WHERE LOWER(titolo) LIKE '%' || LOWER(titoloInserito) || '%' ORDER BY titolo DESC LIMIT 1;
	RAISE INFO '%', titolotesto;

    FOR k IN (SELECT stringaInserita, numerazione FROM frasecorrente WHERE pagina = idpagina ORDER BY numerazione ASC) LOOP
        SELECT numerazione, dataValutazione, stringaProposta INTO num, dataVal, sProposta
        FROM modificaproposta 
        WHERE pagina = idpagina AND k.stringainserita = stringainserita AND numerazione = k.numerazione AND stato = 1
        ORDER BY dataValutazione DESC, oraValutazione DESC LIMIT 1;
        IF num IS NULL THEN 
            INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
        ELSE
            INSERT INTO tempVersione VALUES (sProposta, num);
        END IF;
    END LOOP;
	
	FOR K IN (SELECT stringaIns FROM tempVersione) LOOP
	stringa := K.stringaIns;
	frase := frase || ' ' || stringa;
	END LOOP;
	
	SELECT idUtente INTO visualizzatore FROM UTENTE WHERE login = nomeUtente;
	INSERT INTO VISIONA(idUtente, idPagina) VALUES (visualizzatore, pagina);
	RAISE INFO '%', frase;
    DROP TABLE tempVersione;
END;
$$;
 k   DROP PROCEDURE public.visiona_testo(IN titoloinserito character varying, IN nomeutente character varying);
       public          postgres    false    6            �            1259    25634    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    idpagina public.chiavi_primarie NOT NULL,
    paginacollegata public.chiavi_primarie NOT NULL
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false    865    6    865            �            1259    25639    frasecorrente    TABLE       CREATE TABLE public.frasecorrente (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    datainserimento date DEFAULT CURRENT_DATE,
    orainserimento time without time zone DEFAULT CURRENT_TIME,
    idpagina public.chiavi_primarie NOT NULL
);
 !   DROP TABLE public.frasecorrente;
       public         heap    postgres    false    865    6            �            1259    25646    modificaproposta    TABLE       CREATE TABLE public.modificaproposta (
    idmodifica public.chiavi_primarie NOT NULL,
    stringaproposta character varying(1000),
    stato integer DEFAULT 0,
    dataproposta date DEFAULT CURRENT_DATE,
    oraproposta time without time zone DEFAULT CURRENT_TIME,
    datavalutazione date,
    oravalutazione time without time zone,
    utentep public.chiavi_primarie NOT NULL,
    autorev public.chiavi_primarie NOT NULL,
    stringainserita character varying(1000),
    numerazione integer,
    idpagina public.chiavi_primarie
);
 $   DROP TABLE public.modificaproposta;
       public         heap    postgres    false    865    865    865    6    865            �            1259    25654    modificaproposta_idmodifica_seq    SEQUENCE     �   CREATE SEQUENCE public.modificaproposta_idmodifica_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.modificaproposta_idmodifica_seq;
       public          postgres    false    6    218            D           0    0    modificaproposta_idmodifica_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.modificaproposta_idmodifica_seq OWNED BY public.modificaproposta.idmodifica;
          public          postgres    false    219            �            1259    25655    pagina    TABLE     �   CREATE TABLE public.pagina (
    idpagina integer NOT NULL,
    titolo character varying(80),
    dataoracreazione timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idautore public.chiavi_primarie NOT NULL
);
    DROP TABLE public.pagina;
       public         heap    postgres    false    6    865            �            1259    25659    pagina_idpagina_seq    SEQUENCE     �   CREATE SEQUENCE public.pagina_idpagina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.pagina_idpagina_seq;
       public          postgres    false    6    220            E           0    0    pagina_idpagina_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.pagina_idpagina_seq OWNED BY public.pagina.idpagina;
          public          postgres    false    221            �            1259    25660    utente    TABLE     z  CREATE TABLE public.utente (
    idutente integer NOT NULL,
    login character varying(25),
    password character varying(25),
    nome character varying(30),
    cognome character varying(30),
    datanascita date,
    email character varying(50),
    ruolo character(6) DEFAULT 'utente'::bpchar,
    CONSTRAINT utente_email_check CHECK (((email)::text ~~ '%@%.%'::text))
);
    DROP TABLE public.utente;
       public         heap    postgres    false    6            �            1259    25665    utente_idutente_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_idutente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.utente_idutente_seq;
       public          postgres    false    222    6            F           0    0    utente_idutente_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.utente_idutente_seq OWNED BY public.utente.idutente;
          public          postgres    false    223            �            1259    25666    visiona    TABLE     �   CREATE TABLE public.visiona (
    idutente public.chiavi_primarie,
    idpagina public.chiavi_primarie,
    datavisone date DEFAULT CURRENT_DATE,
    oravisione time without time zone DEFAULT CURRENT_TIME
);
    DROP TABLE public.visiona;
       public         heap    postgres    false    6    865    865            |           2604    25671    modificaproposta idmodifica    DEFAULT     �   ALTER TABLE ONLY public.modificaproposta ALTER COLUMN idmodifica SET DEFAULT nextval('public.modificaproposta_idmodifica_seq'::regclass);
 J   ALTER TABLE public.modificaproposta ALTER COLUMN idmodifica DROP DEFAULT;
       public          postgres    false    865    219    218            �           2604    25672    pagina idpagina    DEFAULT     r   ALTER TABLE ONLY public.pagina ALTER COLUMN idpagina SET DEFAULT nextval('public.pagina_idpagina_seq'::regclass);
 >   ALTER TABLE public.pagina ALTER COLUMN idpagina DROP DEFAULT;
       public          postgres    false    221    220            �           2604    25673    utente idutente    DEFAULT     r   ALTER TABLE ONLY public.utente ALTER COLUMN idutente SET DEFAULT nextval('public.utente_idutente_seq'::regclass);
 >   ALTER TABLE public.utente ALTER COLUMN idutente DROP DEFAULT;
       public          postgres    false    223    222            2          0    25634    collegamento 
   TABLE DATA           _   COPY public.collegamento (stringainserita, numerazione, idpagina, paginacollegata) FROM stdin;
    public          postgres    false    216   [�       3          0    25639    frasecorrente 
   TABLE DATA           p   COPY public.frasecorrente (stringainserita, numerazione, datainserimento, orainserimento, idpagina) FROM stdin;
    public          postgres    false    217   ]�       4          0    25646    modificaproposta 
   TABLE DATA           �   COPY public.modificaproposta (idmodifica, stringaproposta, stato, dataproposta, oraproposta, datavalutazione, oravalutazione, utentep, autorev, stringainserita, numerazione, idpagina) FROM stdin;
    public          postgres    false    218   �       6          0    25655    pagina 
   TABLE DATA           N   COPY public.pagina (idpagina, titolo, dataoracreazione, idautore) FROM stdin;
    public          postgres    false    220   (�       8          0    25660    utente 
   TABLE DATA           e   COPY public.utente (idutente, login, password, nome, cognome, datanascita, email, ruolo) FROM stdin;
    public          postgres    false    222   G�       :          0    25666    visiona 
   TABLE DATA           M   COPY public.visiona (idutente, idpagina, datavisone, oravisione) FROM stdin;
    public          postgres    false    224   <�       G           0    0    modificaproposta_idmodifica_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.modificaproposta_idmodifica_seq', 11, true);
          public          postgres    false    219            H           0    0    pagina_idpagina_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.pagina_idpagina_seq', 10, true);
          public          postgres    false    221            I           0    0    utente_idutente_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.utente_idutente_seq', 6, true);
          public          postgres    false    223            �           2606    25675    collegamento collegamento_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pkey PRIMARY KEY (stringainserita, numerazione, idpagina, paginacollegata);
 H   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pkey;
       public            postgres    false    216    216    216    216            �           2606    25677    frasecorrente pk_frase 
   CONSTRAINT     x   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT pk_frase PRIMARY KEY (stringainserita, numerazione, idpagina);
 @   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT pk_frase;
       public            postgres    false    217    217    217            �           2606    25679    modificaproposta pk_modifica 
   CONSTRAINT     b   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT pk_modifica PRIMARY KEY (idmodifica);
 F   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT pk_modifica;
       public            postgres    false    218            �           2606    25681    pagina pk_pagina 
   CONSTRAINT     T   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pk_pagina PRIMARY KEY (idpagina);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pk_pagina;
       public            postgres    false    220            �           2606    25683    utente pk_utente 
   CONSTRAINT     T   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT pk_utente PRIMARY KEY (idutente);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT pk_utente;
       public            postgres    false    222            �           2606    25685     frasecorrente unique_numerazione 
   CONSTRAINT     l   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT unique_numerazione UNIQUE (numerazione, idpagina);
 J   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT unique_numerazione;
       public            postgres    false    217    217            �           2606    25687    frasecorrente uniquefrase 
   CONSTRAINT     v   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT uniquefrase UNIQUE (stringainserita, numerazione, idpagina);
 C   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT uniquefrase;
       public            postgres    false    217    217    217            �           2606    25691    utente utente_login_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_login_key UNIQUE (login);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_login_key;
       public            postgres    false    222            �           2620    25692    pagina diventaautore    TRIGGER     q   CREATE TRIGGER diventaautore AFTER INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.diventaautore();
 -   DROP TRIGGER diventaautore ON public.pagina;
       public          postgres    false    236    220            �           2620    25693    frasecorrente inserimento_frase    TRIGGER     �   CREATE TRIGGER inserimento_frase BEFORE INSERT ON public.frasecorrente FOR EACH ROW EXECUTE FUNCTION public.inserimento_frase();
 8   DROP TRIGGER inserimento_frase ON public.frasecorrente;
       public          postgres    false    217    237            �           2620    25694    modificaproposta modificaautore    TRIGGER     }   CREATE TRIGGER modificaautore AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.modificaautore();
 8   DROP TRIGGER modificaautore ON public.modificaproposta;
       public          postgres    false    239    218            �           2620    25695 ,   modificaproposta settaggiodataoravalutazione    TRIGGER     �   CREATE TRIGGER settaggiodataoravalutazione AFTER UPDATE ON public.modificaproposta FOR EACH ROW WHEN ((old.stato = 0)) EXECUTE FUNCTION public.settaggiodataoravalutazione();
 E   DROP TRIGGER settaggiodataoravalutazione ON public.modificaproposta;
       public          postgres    false    218    218    246            �           2606    25696    modificaproposta fk_autorev    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_autorev FOREIGN KEY (autorev) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_autorev;
       public          postgres    false    218    222    4756            �           2606    25701    frasecorrente fk_pagina    FK CONSTRAINT     ~   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 A   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT fk_pagina;
       public          postgres    false    220    4754    217            �           2606    25706    visiona fk_pagina    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_pagina;
       public          postgres    false    220    224    4754            �           2606    25711 #   modificaproposta fk_stringainserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_stringainserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_stringainserita;
       public          postgres    false    217    217    218    217    4746    218    218            �           2606    25716    pagina fk_utente    FK CONSTRAINT     w   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT fk_utente FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT fk_utente;
       public          postgres    false    4756    222    220            �           2606    25721    visiona fk_utente    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_utente FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_utente;
       public          postgres    false    222    4756    224            �           2606    25726    modificaproposta fk_utentep    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_utentep FOREIGN KEY (utentep) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_utentep;
       public          postgres    false    218    4756    222            �           2606    25731    collegamento pk_fraseinserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT pk_fraseinserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina);
 G   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT pk_fraseinserita;
       public          postgres    false    216    216    4746    217    216    217    217            2   �   x�%�1N�@E�S� m-I�[R�(hh��ٵ4��d&E*���7�$�P~}����X���uĬ���8¬)3%���ծ��%(ğ�O�K����cd��Q�u����}Dx��'��f����P��3��? �"##�_J&ɘyU,�oZ	�Rt�,:)����rFX��(̔ چ��2sUvB�)hkh���J0'�@�i2��ϼ����4Q`�\{p����a�(�l\�z����{���g_      3      x��}KsI�����5�0!T�<� ��$1٫�!�2�ꬌ�|�5<�ߐٮ��f��$�2�D��{dfed1�0= PO���wYrm�������ԹOʍ/��$寵I�,�lR�dS��-Lb���آ�{G{ӣ������dorzy<�<������M^�5�,�z��V��W>3�=ܛ����6Y����S�� �&�ť�%����79Jֶ*\��$��Փ����/�ǯ�N:3��-�Yf�-!qye��|s>7�;ܛ��(�G鱎���~����u�m��b���SyZ�rQ��`���#����!�/[ܒ�ܑ�8ʓ��Ӆ.��o�ך��q$KS$6�
�Df�]�������mQ�ý��p���}n�'������}B�'��M�&�bM����Op,~��g����Y�:�;��DWɳ�V� j�@K��+��Y�W�����]">��%�)��p�D����bmp��e��<&�ryMcn�����g:��dXŢ&���{?�W�����RL�z�S=�c�0s�5|_�٘����>��cUy|�~˗����$Va��p������R�a{6�	aeKm�sv�":M�I�����`o2�^��<�y�䦪���T���\[�{r��h,���l����aaf�^Om�K�� ���Ň_��g��֏40���g� z�>���v�N��X%�eIG���$�.����+�^Bj�a@VK�B��@KA���\lш0�!�0!#����;1CI(.�C�J�C�^Y�Ta����r�*�2�=�hj�i�#�^��^Q{B0f;�WH,P>��cr�@�˧�����tzx6�N.�z^�xe-��*FȒX�Ʊ��/s��A̤-3��DZ4�,� zv)�F���:���n���]��sg��˧h�L؍-
�"�ni*���{ ��-����ٓL�If?��vAw�k�rȥ�ބ�aN�ShM�,T���Dgp�∕|�YB ��8�DT�p_�aO8F���M��
���� D�}RUhW}�2�����l������x�a_`�<A-Q;vB|C�z�/$�a�=I.\�蚨�l�B��qK��j�M/	,]F���^8���&%T�dE��d�UMR�'��>ɍ�E��X�q☹�v�Έ,�^�C0ǌ���K�3��@ }���-O���+�hR�C��Z&7+:�����|��_@l��B�5��	��qL�v�S� N����bm�[h�|"���]$��9rK�IC�y�:0��l/�P�����n�R�:�! �`Q��y鞁�#��#���-�ר\�y#�ò�S_$[�ϗ[����1�����ʱn�,k�[��MF8�b.������� ���M6#9R�0�ۗ	�q
Cw@�T)��ɿA �� �QYk�� \d�'Q��}A>��E�-f����1O����f"-K�T%ґueB,�) �&�%�5+�F��%.C��AϘ����Y.UҀ�9���[�PP� b�BA�J2rcD�X��l�ɗI�9�f�3g���#��f���bW5�G�ys^�O���v<��o�>_���Oo��/��_��������/w��o�g�B�q/�!��o��im[�W�� �g�#7'������W��54�vv��:�#C���FY7�+M4t��RoI���	��#PQZ�8`2��jwK_|����G
	9$OH�|I�>��<S�4��1�:Y�Y����V5B�&A���+[Ԡh���%����Nl�Z�{�\c֒8�2b��f=A�{�z;Ш˓d���%�'+ /�'�{��b���bxY�Ub�4e����
�l%K�a?m�e�딑u�+��+%+�̮�i�H��rų �
D~v�"�I,��dS�����Y���>itM-�U��J`�P�h?��كm���J�D�P���p��~������
�#
	<h�i@y��5l��颶�_��%+?j`��"��9s�����:����w��&�O4-��+쬶7W�$��0G �ړ��C?�ZFX�{�Ob蔤M|3�#��8^��pL|�t!��g_aNKu}Q;��k�^&�`��.��b�V
��[��@m�Yp}����8W�[�ZsCpCb�ـ�@���I ٥Y;�X�r�4�'�+��K�gXAN�3Oir��G�ˣ���dvN�>�~���!��zG�Z"��xdy�@%�z[�9��=��A�x.��r!D/'(��$J��>ß�YmI��`�-�T8v����f��w$M�o�5�wL*G� �_(�'f=wL���6��1����1�f3:YA�J'D	�.��e�����nA��B�&Ц�\����RȢ�Ze�i���lμ��E�Y-wYNH���e�&�#�&�7���Z�����%��M�;�IO"4t�5�w�A�xj�eϚ/X>ү��Â��Q8�BC�H�9� U�Ͻc��F8<\��;ҿ�<��ս��N�~x�t+L���5Q�V6�3�q���P諠^���o]���&l���4gÅ�A�	�;^��kr9�1���.fg�{��xƚ�2R���'E�̇��WOe�c��*�]����A	�"�rhCy��kb�"M�]N`:�����ט�4h�W^����P<�~4D_�	���ޤ�~fW 8�is��uݮ]�I{�&U�'WD t)6Mn��e1�4�\\���f8i:�����-&���5]�/�J��z���hw`>B2�P@A�-�=$*I#ʌD8Br+u� ю!Z��G��4��c�D�т�L攫5ݕOMƑ=��s�c�h�;hG��8W?���W1�j��D��Eԋ�v�u���j}�n������B��s0�Q
QB�ވ�K()7p�*�^f�~9�1���>���i�g �'���p���<3�[ү���>C�CCon�B�U�m)��������;���K�"����O�o���.���d�?ԡ�d�!�i[�ݎ�%�In�\V��˗lًK$��ߑ��A�]t����a[Z��׉�T�����#	GՄ��+�n�׏u*���k��� M�ӷ��0��h��ty�� �xGy��c����H�?�BV�D�,k�@�6(q��6n���;,�d혗 ��N-_�g����9�%�f3�:";�3�rS�p'_s]��D�M�J�Y�	C��CH���X�{R���W%��=�#�Q�b���f��k�9 �х6$:��M��'P�IW�ķ9�Ĳ���I�������4@<uU2�ɽi^�H؎�q���h=��X���v�u2756_����Ɨ8�n���)?Bv�  �9���h��zB+I�!�{���˷�&kT��cZ��"X6�E�U��q��Q�o��0�-�5ӥ��]"���&��S�����&�恄�g�6ގ��;�%��\���E�	o�4k��^��:�]3Y&�IeeV|�%L8bk�,0��-�']�!|�8��\�z�묉��pp	�D�T�)�.q�@i"ݵ��R���j��{����E�!P�-ٴY�Mz7�[��HKiRoƔ�,(���/��"f�e	�wU����p�t܇�ճG��.�.sv�q���~L�_bjs�))���q���)�uU����#��I��	�,j$'81+2�ĖP�e:����oNT�Z���g��N���s"���3�G]ќ8 ��f���Fi�y�&�[v�l90�a�-	��7B%G:i�E�9��jX�nd�k�%��?�v�bqI"+hD�i�AŃF^�(�ܳ17�%i�&��h��eF��#k�Sf:�	i�J2aa:;V����{CK;h=}���bmEhز�@	|���ĎA@,�<#Z�"ܦ&W�{w���)f��/���A���ՂI_�ף�d7��r���	)60�KV�a�k�,a)�w��*������(瘴��$|�T�C�檶tJxX˞�@�ہ_;%��	�w��=� �f���� GGWpr��#�F|���ʎ�q�1�
	@�e(�+��r�zw�\�    ��&1��;�Sg 'I���{�#���j9ls��T��d`��������@�X��JxE�m6��J�����Ϥu"єe�CL���=�͌0�G��ę	��E�*'�+JY�!6H�:�r�0�QĀ(�E��]�����4��D�c�?�͆�e:��������.����4����q##�$96G�� �@E����-N �' ���}N�<��'J/D9��6�,�=�06�ɓ#�G#�wo��ΌnQ��/+�Óu�B 5����uVJp�������P���tr~9�8����N��_<V�����K:�SgP%Ҝ8�
�{!V�pVN�l;�z#>iZ5��M�i��V��]w�qhc_�K}�_���)���� ��s
�G̹b���'u1gۼOٽ���$�uU����$k#Ɏ���C^������.���j��)R�J��7H�Ά��e�F�W:V��뵋�K��Bs�~R/�ZJ�H�Iz[��&b��®��5�;?)���̩�-S"�_�I����%����̤��G���\�-�D]~�gp�i�A�Цj(����o���A���m�#�\<zW��"9??:�}S����nv�2����ݗ��3\r��Z�8QN�/6��?�k��WҺ������7��&�fj��?���ޗ�NS$
�*#�>-z�*��ʀ�n�vM'�tS��YPg|S}ȈB�Y�\�O�u>*$�&@U�Q3F^��s
b�{u��d�I��� ���/2�N�M����(��H�IX���V���j�t;�z{O4�x��I�YxqVm��@� �B����<�Jv�pBM��4��綨��V��R�N�8���c�9ީֺ��c�� �qPN�R��a���	�ҡ�%+~�xT��\U1�����B�fɉ�ie�WB'�l�ԜQ�&rC�g bzn��AE�����z:4���Y�;9�=d���[6�7lr��Nac�W��D����GPn�d(+��IJ�3݈�AJ�D4�縌���zS� �D-��ސŕ��Rnm[@p��RO�0�ܔ�#�`�4Y�zc$\�;8!����ҭ2��[v��s���*��&.��$GGG���vL�\8?����u[ W:�{Af6��a�kB������hK>��N������(+W�#�bZ
�F.����'�u	txo�ﮓ��H���o�hT�ݱO�gP��St�u���K���L����.������_�)�_ʂ�����s��px�PxL���f��S`�S=��K�i�[/���Q��R+��u��O~>G�s�i���6�X$�d#����C��[rTǎ�̜�<G��2�A���}�.~�A���<����8��m�Y�mq��:?����C/�ɤD�ŝ�ac~��9݋�֔�%��s�p�M��=�D�����k��� C��A$���g�<�dT9lu����S#+�>����m"!�Z��]��f6|���Z�*8���"�[.K�<)�4&�A���3��S���e�D������AO�;}|�Y����]�)�A*N	�twM
�h���_�7�Y�R�d �!a�Apʶ:�A���D�i[�Z!r�o$�☙��H��d�@�5r��g�Z�$)�����vФ��lU�Q�F�V�K����\��.�y�:� �q��ޜ0%�dT	8��7÷/�ځ6��M��Gn.jp؝ș]���H>�]7}��}�L-S�j���\������J�A"{�gvGxqb�s��1�M.��ut]wIb����`���w_~���e�%:��-��q�驡�� BfG��#G���ez�
q��71�5@v�w2��b5���z� �#Ɯu�#+W��\���3����8�A3�(W̃h����$ɡi*@��4��]NNgӋ#���dDJZy�C}�ͫ�g>F�[I)��9�8��L���f�R��8aMR��p�HζCI�V�[�H*50�}�q����F�l4�+��fEkj����6U\ZjL�����9W�8_�-_��?�B���j1Y��J�ŭ��a�=�T4|��3u¸]@�D^6���}�.y_�ȶ��uIm�4��|���t#����a�c}��I9r���I^�"�c5����D�ښ�Hg�+�o2��n�*vE���:�p9*�'#l��i�n#�33N���Z���jq��rΘ�\��X�OF��'F�Z=g��[��;��(O�@
�mR`��EY<�xy��0<G�nr19�\�7C�w���0#Wt40 �V>��u��$�0�'��9O���5!!�����P��e�T�q�y�G	�����Ī@�T���VrA��e��fN=	y6�&3q:��v��B��p@Ru��!�,�k���L��
�l��ٓ�XLcӎp�x��q0z@�(gǽMO#?� �h�x�W���"�$���MGX�)��.dzE�۝�yvl��8ٚ�#���P����\1�� ;U��z���}:�D�ӣ�c�r8���k��t/Mˌ��"OB�]��d�C:����2��<�uh�RMB�����0�_�\�(2���%����C�3+�t�[)��J���]�xB�x^¤M�'��JTU�rp�-�3I��NY��3����Ar�de��/q͐ڳi2yC�m�*�Or��I�q��L�҈|���\�����L�N_����֍�`Ȯ��N˱۸~ϡML,ɳ�G��}��l��{|rqq���s!U����h���%��f;�飷H�br1Z�A��T�M��B�rG��A�UXDsrG�&��ٞm������#�7���%_�)������z�'7G��jV��p������M�+��z	!��p@MC��N�'!���|.V���_�{�*��/�fF��h�?����u�q$���zwʅ�.U����}���fY�I�w��[�Dh9S2q������������!��A銢�CǶU$�ɰ�jH9���h����U�Fr2�s�VQ���ms�����lBn�=���®Z_L��H�צ��?�NϪ��a�$ARoL������2���Q-~ ���zoʬ�&
d�w*'��d�����^��f���O��Iڕ�[!P�"�0��{C�����|d�=�@r���y�l+�S-�`��k��8�9G�%��*��u�c���w?=F��x�쐓�Jp��I�,���H�r�LCDz��u'�`�
W�\ŭ��~)OT�ה ��&Ӑ�X�͢�� 9/@<C�j�B�R�u��`}�Rkg|a��1v�榉�q�6�HC=���vI����&�5��	�)�����֥���ڑ��[#&�&m�V`�s
��.+�M�:�%8VJQ��+��
i,FG~����v�"y�8���_��u�Ĩ�W�n�3��"�{��֗�Ci�J+!6�������s�}�Fjz�볥d=p����  ���Nmƕ�I�}�ɱVد��Nk���2m�)��Vz.����ܓ�b�&Әl�g4Y"��$��51HQ]y�`f�C�)���e��҅(��[ɝ���q��`eUE}0ه}�A|ӡ����� ��4�[R��[�'?��ݱ�2�,Ϝ�b�.��*$�gbXWF��h�%�#��6�Wp�(=a������R'})�
-(c_*S��{
.�N�����};jޭ�}%�eψf�q)	��F/F������Ăg۬�Fȴ��Y0NGxvͤ�Xz�i@���v�?�ą�&ܞrv��Bn��e&`uzl:'0�0�d�f\з�vf�Y���=A����䌷
�"l��F'顑X����Lۂ�DĒ9Q��S���҄�+���hB�5A5�V��Z��_�=�gZ )4�\7	�f�'���ţgHJ�F�F�Ax"����=��-��\|�>��$fVi1aU:3�hG���8���{2$��ڭ�����Bt/���B_R�)��C�מ��֧U�}��f����@0%T�����h���.p;Y�b�T�娵Y��4�}ON���m�I��aڋ�3;ؘ�B[��@'�m��]�f��$Ms9�$�$˞g���\��]���"�� �  /�&֓���B�d6��D��d�ц��F��#M�@��!�{CG�ߤ=����J�p	�"�6�SP���E�z�7,�����YT���iX&�[(`ژE�&����T��M^Mf���7�Ԏ!$oI*<�>h��]:68߉a*q��g��6ĺ�l2;�g}� ;nW���J�{^[�]�<y/Zj�.*,f#<޹�Qہ#l@��^J�o�ݝ�������>���������jI�ߓ�N�IC���P�QUȏ�����2�k*17M���&g�'g��ӓ�����Ɏ2�%�͸�� cLu#:�E��ޢv��+����*LJM�-3�{�qg2u[������h�RzP �3�f��|z_i��h3����Lq����R4OF���u!��i�4��ֺB6���u4#7�DF:
�٥�S��ŵ AE���49�$�W�<Kě�8����́��H��1C�A��{s��Ƈ8�.�U��bg@���Q&�4�m���K��	Jxʥ}���[�}�9~�~���������X���l����Uؐ#�}�KQ�E�D�H���b%5��{�J�[h�PѤ*N7�\XN���w�tR�	�C�m���
�0K����`X��#���]TT0D���p=v�X��2�#Ff
���:���d\�8d��!q$�GaKx)Z�`�?�Ŋ͢��E|� V��ȿ`7[�}\H�Y�դJ(�t�?�y����	�?`���G�Q�}�j�M���F�T7�����)JL��ߖ0p{�ΛX�wW܈b�Ani���d�6�T��#�Ş������A�|b�K�
R �V{��f#)
Iؼ�%�um6|�	2I��\K>2��<���s���@�AD�3�B�Vx;a���k�����/�O#�=��V��AԳ�� ۼ�v�eK}e%ڪ���Z��Վ�ZxB#t=���%���xM}���Zd�EcÍ��HO�\�7�5�g~����(�R���C<�4w\�,�J��)�B23�0 ��*ao����T�]��	H?]D��h>�tƅú���$h�D*�N7 �o2	�=wYQ�W���"0�z�{��,y�5��h��h��U�VDj�� ";����GfQ�W�R���l;a-a�qnYm���\�[&�(�}@Z���]���dA��N��5�وr�c0���[�T溁J��T���)i�4)r�*�~��{��
l4����O
r��j��¥5-T/�OM��[�&���@�s.���棃��Ñ��T�*\�gB��(q=�a�ʝ4*�0�J��?�\1�YQ�A�6]]s�Pdn5�q�[�ߣ����W��Q��k�3��ԥ]D��ѐ�݌2Otoq3�� p V۲>�T�H�m��ZTJ��5��K�c���A�G�.��}�*Η�7	U~qk��(S=����Zk._&}.��.o9r:K6�Ԁ$��vSЩUVR��3aNa��,�:��>Xxd#0Ojp|����c��G�1Ev4ԓ8�ϡהoWh�D��f�����k3��!��/ϱYTx4���w2�\�ǲ��F��Ե��$������ؚ�]�=A8p2f8���8�]6޼?e�P���UDB��������*���ޚĆ�J�������r穅!�� ��R�P��L{�"�m�ћ ��W������5-}�w�#�M�!�}��C�FR�qf������ P�H��*�{]M}���j��m���i�-'{&n=��w�����eD���$�O���R�A|��h|@�Cx.�v#�5�d��T"�����a�f7���7�%��$��/*>j�r���J_���x]��،0I��,IS8�;a�+n�R5�i�ޕb"Ea�濷��nz�쐞�X�s���h�Pq;���-9
^'���x�f�t �Mw1�����a�>��ծ�O�������?��0x��<8)����'��ܒ�8 � ��q�pSXY�v^��q5��s�=$�����ȭe!�j!Y �^���9���kg�rп�P�SⅧ�X��*��A�8a�֖�/�F��N?�|���gZ CR��/�l�D�>���,&��l��-���[��.�wv����!�K��RK��=G^5r�y�bZ���蟍��I��V���w�]~�@/[�p�Ӎ����}��5���;��hx�&����Aг��s�/؞�0)g�u�b�K �S9�LB��$^��FJˠ��L3�9 �:�j��VJ���.t���.��ѿ�:s���䵡����<�됶�i䂷7�KD���ɓ-p6	��_UhZ��
s1u�n�(g4t�������O��06����l�=�z�􏐇�A�P<��V�Ч!�G8���؎��7�8,��=�o�|�Us��z�w>�
��y��}�Fr�:��ڠ�_ZK�`�@�{G�'��ǧ�'g�S��Eh���Q����-��ҡ/�m����uBO.h��o�1H�����hm\g{b��$�!���Z�|������-�hKro�A����B#�ڥ�]����8?)Q�>X��S�c[M҆��/V{��ASW�G������I}���m�1�lQ�e=��`���|��������r�h+
84�J^f�o�����2�o���`i־�m9��V���W߷�i�/w|���wů���'��k������>L^���δg�f+�74�5��e3�饑�����m��t1n.��W��^�N��ϷW_n�/Wo�o��C�sK�`R/�#c)��l�h�-��v�����a��m�۬����N<�$59骔��1���>��k�>8��~�׿���;n���|����wr;+�]h��iL0ɫ���� �ˣ�o���0��lj�T%�:I��+,v?��q׿m�<�;ہ�����z~¬U����ErY�oX�j�jn@�h9u��}|���e;F94�ׇ��#��_n�[���K�w�M�BsKp��K�<��!���k�uj���e#�@����l��B�ӄy$�r�k� xhw���n�cȳ�x� <ۭ�����1�A�~��0k�%�S��|WI���%�	�����B||ZJPx.1B��`�/.�Z6MƐd83����7[qܡ�9����̕�#����^��ي��_�fjN���'y\�<�7�C�N?���x������7aU���a�Z�D*��^�����xrV����[h���
^\</�HܶV��	��Js5��M���>%$�]�<���/w��l�s��XȈ	g�b:�%K#�7tv���zq��@�G#֩�^k��Ť="�c8��]
� (���X��-�Ċ�I��o���nG1m�e���f+����`�ںU�a����)��2M����4+Aq���u����)���\W�۵ڃp�3��T��P�'�ÐqR�BǠ�F�z�˥b�R"u[���N'y��MlӨVM��g�w�����)?@YX2�[򡝾��ev����/�M�y�5{B���a�M���j����53>~���M����ܱ_�O!�v��0}ОZn�
uS����3]�xf�6m{٪���^��w��� .V��O�!|8��f�W?���]�S.�/<������ص���ڞ���7z�쏘Z/�a�%�ڎ]8���o��E��4������G��W�A7�a����=�7-a������J��6�������D���0���1��Ĺ�rq�|�|s�9y��n��yCE��Go�Ɇ�p;)�7�B �vZ��plj[�>Tqv���p+ybX[��jnjǀ��Nq�j�����e"%a�EN$k�`lm��`9�գy5����Z���ھ��3�v����2��@V���Z� �ι��I�����u����O_�����7�J�Y�����mv���oIrUUm�9��7��O]G��+�Y�:�GD�9|����A�L�      4     x�ŕ�n�6���S̭[�����]�c��c����k���$�ɯ�r칯�7�t�u�z�l�$@!9��o4?bdj�}��y�5� ]4[�N�ܦl[0�T%ˊ)㜕�(�+����J�Z��npޣAAX��ʊJ���<��&��K��:gu������]<���^�δpxx�u�� ν����P_X����V��_D�(��J���2�&BJV�E�SQ�����2�S8o_�N&�L\��Z��}���:'/Rʲ�뢼�y*)W9�Ό�p���n��eh��^w�����0�vn��P;ݶ������V;��n������Nw���v�������p��4}�YT"K�TJ�Ex�"��Y�(������Kk���ڀ�}��6���U�C�5\��.���A����>�[�a2.l@��z��(t5T)�X���y;�??�0����7�I~f�;Zݜ��`�!b�����3������8v��+E������W&��84�!=fu�	yR�����t�1tF�X��/��x�1��TJ���[a�u�q&1��(7��q�b����!�r4�ž���15�>_����Lۚa*�u���J�J�	󱎱Rk/��Ҍ�w�g�8�ٝN�%C+ʫL��T�D���d{'G�`��	��MoZTZbtz�]��\������O'�ɏgO�b�T"�r]�U��%�Y�H��/�r�z�p6�>�9;�}��>Bb8�+��T��?��`o�$I��`j      6     x�]�=n1��{�� �=��A�D�-Ҥ��e��h���1R���i�<-���u�p�\*4+�W�A�d(-���z�;�_u^x�����(=��$�#��x]���?��։\RM�H�5i�>�m�3g82��0�[=-O)E��#o[+>��8��|{��H%�$aTc�D?�<����L��g�1/���4Vx:�����:)�d)Pl½����aS/����)�v�^��J�G���
_��<����TL�m���n'>e�u���b�      8   �   x�e�Ao� ���Pi��*��iꤝv��BrBJ���Ѥ\�y��a��G�8@mlo��=��ZΘF�V��g�f_�z>��.�")b�F�{��Gb�=��E7p�S�H9�>a`bF�w$s\��Q��;k�kK��G\R-\�:��40`�y�y�%m�Z튙�GF'7Ϗ��2�v��S�޿�u{b�k �W�pJ0i#���R��^�2ڶ5��G��I)�\��      :   �   x�m��mE1е�Kג��7��<��x@�,0��CeI��Enޱu�b
�ȱ<&���m�^SQ�ڲ�[0e�k�X��q�F�`8�ٓ��E���c,��}�(%8|c�O�O�R��t_83yd���y��CD�9���٘,�7��A�"�w�*��d�M�`�a{�'��/y8��R����ߚU�a!s����1�����n���3��)A�Sr1b��=���J�g�[$�.��I{�/     