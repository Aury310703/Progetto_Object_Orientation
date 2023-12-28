PGDMP  1    *                {            progetto    16.0    16.0 I    E           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            F           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            G           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            H           1262    32924    progetto    DATABASE     {   CREATE DATABASE progetto WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE progetto;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            I           0    0    SCHEMA public    COMMENT         COMMENT ON SCHEMA public IS '';
                   postgres    false    6            J           0    0    SCHEMA public    ACL     +   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
                   postgres    false    6                        3079    32926 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            K           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            ]           1247    32937 	   pk_utente    DOMAIN     +   CREATE DOMAIN public.pk_utente AS integer;
    DROP DOMAIN public.pk_utente;
       public          postgres    false    6            �            1255    32938    diventaautore()    FUNCTION     |  CREATE FUNCTION public.diventaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    32939    inserimento_frase()    FUNCTION     w  CREATE FUNCTION public.inserimento_frase() RETURNS trigger
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
       public          postgres    false    6            �            1255    32940 -   inserimento_frase(character varying, integer) 	   PROCEDURE     v  CREATE PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
valore INTEGER;
BEGIN
	SELECT numerazione INTO valore
	FROM frasecorrente
	WHERE pagina = idpagina
	ORDER BY numerazione DESC LIMIT 1;
	INSERT INTO frasecorrente (stringainserita,numerazione,idpagina) VALUES (stringa,valore+1,pagina);
END;
$$;
 Z   DROP PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer);
       public          postgres    false    6            �            1255    32941    modificaautore()    FUNCTION       CREATE FUNCTION public.modificaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    32942    notificam()    FUNCTION     �  CREATE FUNCTION public.notificam() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
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
$$;
 "   DROP FUNCTION public.notificam();
       public          postgres    false    6            �            1255    33081 %   ricostruzione_versione(integer, date) 	   PROCEDURE     �  CREATE PROCEDURE public.ricostruzione_versione(IN pagina integer, IN datainsertita date)
    LANGUAGE plpgsql
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
        ORDER BY dataValutazione DESC, oravalutazione DESC LIMIT 1;
        IF num IS NULL THEN 
            INSERT INTO tempVersione VALUES (k.StringaInserita, k.numerazione);
				RAISE NOTICE 'k.stringainserita: %', k.stringainserita;
        ELSE
            INSERT INTO tempVersione VALUES (sProposta, num);
				RAISE NOTICE 'stringainserita: %', sProposta;
        END IF;
    END LOOP;

    DROP TABLE tempVersione;
END;
$$;
 X   DROP PROCEDURE public.ricostruzione_versione(IN pagina integer, IN datainsertita date);
       public          postgres    false    6            �            1255    32943    riempi_tmp(integer)    FUNCTION     Q  CREATE FUNCTION public.riempi_tmp(codicealbum integer) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
DECLARE
var ALBUM.codA%TYPE;
cu refcursor;
cursore CURSOR FOR
	SELECT codA
	FROM ALBUM A
	WHERE inAlbum = codiceAlbum;
BEGIN
	OPEN cursore;
	FETCH cursore INTO var;
	IF NOT FOUND THEN
		-- RAISE NOTICE '%', 'ciaooo';
		CLOSE cursore;
        RETURN NULL;
	ELSE
	LOOP
		INSERT INTO TMP VALUES (var);
		-- RAISE NOTICE '%', 'uuu';
		cu:= riempi_tmp(var);
		FETCH cursore INTO var;
		INSERT INTO TMP VALUES (var);
		EXIT WHEN NOT FOUND;
		return riempi_tmp(var);
	END LOOP;
	END IF;
END;
$$;
 6   DROP FUNCTION public.riempi_tmp(codicealbum integer);
       public          postgres    false    6            �            1255    33239    settaggiodataoravalutazione()    FUNCTION     9  CREATE FUNCTION public.settaggiodataoravalutazione() RETURNS trigger
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
       public          postgres    false    6            �            1255    32944    somma_max(integer)    FUNCTION     ~  CREATE FUNCTION public.somma_max(albero_input integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    max INT := 0;
    nodo_corrente nodo.codN%type;
    somma_corrente INT;
    scansiona_nodi CURSOR FOR
        SELECT codN
        FROM nodo n
        WHERE n.codA = albero_input;
    BEGIN
        OPEN scansiona_nodi;
        LOOP
        FETCH scansiona_nodi INTO nodo_corrente;
		EXIT WHEN NOT FOUND;
        somma_corrente := somma_nodi(albero_input, nodo_corrente);
        IF somma_corrente > max THEN 
            max := somma_corrente;
        END IF;
    END LOOP;
    CLOSE scansiona_nodi;
    RETURN max;
END;
$$;
 6   DROP FUNCTION public.somma_max(albero_input integer);
       public          postgres    false    6            �            1255    32945    somma_nodi(integer, integer)    FUNCTION     �  CREATE FUNCTION public.somma_nodi(albero_input integer, nodo_input integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    n_nodi INT := 0;
    Nroot albero.root%type;
BEGIN
    -- Esegui la query per ottenere il valore di root
    SELECT root INTO Nroot FROM ALBERO WHERE codA = albero_input;

    -- Esegui il loop finché Nroot è diverso da s_nodo
    WHILE Nroot != nodo_input LOOP
        n_nodi := n_nodi + 1;
        -- Esegui la query per ottenere il valore di padre
        SELECT padre INTO nodo_input FROM arco WHERE figlio = nodo_input;
    END LOOP;
	
    -- Restituisci il numero di nodi
    RETURN n_nodi;
END;
$$;
 K   DROP FUNCTION public.somma_nodi(albero_input integer, nodo_input integer);
       public          postgres    false    6            �            1255    32947    uri_foto(text)    FUNCTION       CREATE FUNCTION public.uri_foto(stag text) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE 
	sql_stmt varchar(1000) := '';
    pos_prec INTEGER := 1;
    pos INTEGER := 1;
    tag TEXT;
    str TEXT;
    Sout TEXT := '';
	cursore REFCURSOR;

BEGIN
	sql_stmt = 'SELECT DISTINCT uri FROM Foto F WHERE F.codF NOT IN (SELECT T.codF FROM TAGFOTO T WHERE T.parola <> ';
	LOOP
		EXIT WHEN pos >= LENGTH(Stag);
		IF SUBSTRING(Stag FROM pos FOR 1) = '@' THEN
		tag := SUBSTR (Stag, pos_prec, pos - pos_prec);
		pos_prec = pos + 1;
		sql_stmt := sql_stmt || quote_literal(tag) || ' OR T.parola <> ';
		END IF;
		pos := pos + 1;
	END LOOP;
	
	tag := SUBSTR (Stag, pos_prec, LENGTH(sTag));
	sql_stmt = sql_stmt || quote_literal(tag) || ')';
	RAISE NOTICE '%', sql_stmt;
	
	OPEN cursore FOR EXECUTE sql_stmt;
	LOOP	
		FETCH cursore INTO str;
		RAISE NOTICE '%', str;
		RAISE NOTICE '%', Sout;
		EXIT WHEN NOT FOUND; 
		Sout := Sout || '@' || str;
	END LOOP;
	-- Sout = RTRIM (Sout, '@');
	RAISE NOTICE '%', Sout;
	RETURN Sout;
END;
$$;
 *   DROP FUNCTION public.uri_foto(stag text);
       public          postgres    false    6            �            1259    33058    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    idpagina public.pk_utente NOT NULL,
    paginacollegata public.pk_utente NOT NULL
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false    861    6    861            �            1259    32948    frasecorrente    TABLE       CREATE TABLE public.frasecorrente (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    datainserimento date DEFAULT CURRENT_DATE,
    orainserimento time without time zone DEFAULT CURRENT_TIME,
    idpagina integer NOT NULL
);
 !   DROP TABLE public.frasecorrente;
       public         heap    postgres    false    6            �            1259    32955    modificaproposta    TABLE     �  CREATE TABLE public.modificaproposta (
    idmodifica integer NOT NULL,
    stringaproposta character varying(1000),
    stato integer DEFAULT 0,
    dataproposta date DEFAULT CURRENT_DATE,
    oraproposta time without time zone DEFAULT CURRENT_TIME,
    datavalutazione date,
    oravalutazione time without time zone,
    utentep integer NOT NULL,
    autorev integer NOT NULL,
    stringainserita character varying(1000),
    numerazione integer,
    idpagina integer
);
 $   DROP TABLE public.modificaproposta;
       public         heap    postgres    false    6            �            1259    32963    modificaproposta_idmodifica_seq    SEQUENCE     �   CREATE SEQUENCE public.modificaproposta_idmodifica_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.modificaproposta_idmodifica_seq;
       public          postgres    false    217    6            L           0    0    modificaproposta_idmodifica_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.modificaproposta_idmodifica_seq OWNED BY public.modificaproposta.idmodifica;
          public          postgres    false    218            �            1259    32964    notifica    TABLE     �   CREATE TABLE public.notifica (
    idpagina public.pk_utente NOT NULL,
    idutente public.pk_utente,
    data date,
    ora time without time zone,
    idautore public.pk_utente NOT NULL,
    titolo character varying(80)
);
    DROP TABLE public.notifica;
       public         heap    postgres    false    861    861    861    6            �            1259    32967    pagina    TABLE     �   CREATE TABLE public.pagina (
    idpagina integer NOT NULL,
    titolo character varying(80),
    dataoracreazione timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idautore integer NOT NULL
);
    DROP TABLE public.pagina;
       public         heap    postgres    false    6            �            1259    32971    pagina_idpagina_seq    SEQUENCE     �   CREATE SEQUENCE public.pagina_idpagina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.pagina_idpagina_seq;
       public          postgres    false    220    6            M           0    0    pagina_idpagina_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.pagina_idpagina_seq OWNED BY public.pagina.idpagina;
          public          postgres    false    221            �            1259    32972    utente    TABLE     z  CREATE TABLE public.utente (
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
       public         heap    postgres    false    6            �            1259    32977    utente_idutente_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_idutente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.utente_idutente_seq;
       public          postgres    false    222    6            N           0    0    utente_idutente_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.utente_idutente_seq OWNED BY public.utente.idutente;
          public          postgres    false    223            �            1259    32978    visiona    TABLE     �   CREATE TABLE public.visiona (
    idutente integer NOT NULL,
    idpagina integer NOT NULL,
    datavisone date DEFAULT CURRENT_DATE,
    oravisione time without time zone DEFAULT CURRENT_TIME
);
    DROP TABLE public.visiona;
       public         heap    postgres    false    6            {           2604    32983    modificaproposta idmodifica    DEFAULT     �   ALTER TABLE ONLY public.modificaproposta ALTER COLUMN idmodifica SET DEFAULT nextval('public.modificaproposta_idmodifica_seq'::regclass);
 J   ALTER TABLE public.modificaproposta ALTER COLUMN idmodifica DROP DEFAULT;
       public          postgres    false    218    217                       2604    32984    pagina idpagina    DEFAULT     r   ALTER TABLE ONLY public.pagina ALTER COLUMN idpagina SET DEFAULT nextval('public.pagina_idpagina_seq'::regclass);
 >   ALTER TABLE public.pagina ALTER COLUMN idpagina DROP DEFAULT;
       public          postgres    false    221    220            �           2604    32985    utente idutente    DEFAULT     r   ALTER TABLE ONLY public.utente ALTER COLUMN idutente SET DEFAULT nextval('public.utente_idutente_seq'::regclass);
 >   ALTER TABLE public.utente ALTER COLUMN idutente DROP DEFAULT;
       public          postgres    false    223    222            B          0    33058    collegamento 
   TABLE DATA           _   COPY public.collegamento (stringainserita, numerazione, idpagina, paginacollegata) FROM stdin;
    public          postgres    false    225   &r       9          0    32948    frasecorrente 
   TABLE DATA           p   COPY public.frasecorrente (stringainserita, numerazione, datainserimento, orainserimento, idpagina) FROM stdin;
    public          postgres    false    216   Cr       :          0    32955    modificaproposta 
   TABLE DATA           �   COPY public.modificaproposta (idmodifica, stringaproposta, stato, dataproposta, oraproposta, datavalutazione, oravalutazione, utentep, autorev, stringainserita, numerazione, idpagina) FROM stdin;
    public          postgres    false    217   |v       <          0    32964    notifica 
   TABLE DATA           S   COPY public.notifica (idpagina, idutente, data, ora, idautore, titolo) FROM stdin;
    public          postgres    false    219   �w       =          0    32967    pagina 
   TABLE DATA           N   COPY public.pagina (idpagina, titolo, dataoracreazione, idautore) FROM stdin;
    public          postgres    false    220   bx       ?          0    32972    utente 
   TABLE DATA           e   COPY public.utente (idutente, login, password, nome, cognome, datanascita, email, ruolo) FROM stdin;
    public          postgres    false    222   �x       A          0    32978    visiona 
   TABLE DATA           M   COPY public.visiona (idutente, idpagina, datavisone, oravisione) FROM stdin;
    public          postgres    false    224   @y       O           0    0    modificaproposta_idmodifica_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.modificaproposta_idmodifica_seq', 26, true);
          public          postgres    false    218            P           0    0    pagina_idpagina_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.pagina_idpagina_seq', 3, true);
          public          postgres    false    221            Q           0    0    utente_idutente_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.utente_idutente_seq', 6, true);
          public          postgres    false    223            �           2606    33064    collegamento collegamento_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pkey PRIMARY KEY (stringainserita, numerazione, idpagina, paginacollegata);
 H   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pkey;
       public            postgres    false    225    225    225    225            �           2606    32987    frasecorrente pk_frase 
   CONSTRAINT     x   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT pk_frase PRIMARY KEY (stringainserita, numerazione, idpagina);
 @   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT pk_frase;
       public            postgres    false    216    216    216            �           2606    32989    modificaproposta pk_modifica 
   CONSTRAINT     b   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT pk_modifica PRIMARY KEY (idmodifica);
 F   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT pk_modifica;
       public            postgres    false    217            �           2606    32991    pagina pk_pagina 
   CONSTRAINT     T   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pk_pagina PRIMARY KEY (idpagina);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pk_pagina;
       public            postgres    false    220            �           2606    32993    utente pk_utente 
   CONSTRAINT     T   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT pk_utente PRIMARY KEY (idutente);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT pk_utente;
       public            postgres    false    222            �           2606    32995    visiona pk_visona 
   CONSTRAINT     _   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT pk_visona PRIMARY KEY (idutente, idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT pk_visona;
       public            postgres    false    224    224            �           2606    32997     frasecorrente unique_numerazione 
   CONSTRAINT     l   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT unique_numerazione UNIQUE (numerazione, idpagina);
 J   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT unique_numerazione;
       public            postgres    false    216    216            �           2606    32999    frasecorrente uniquefrase 
   CONSTRAINT     v   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT uniquefrase UNIQUE (stringainserita, numerazione, idpagina);
 C   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT uniquefrase;
       public            postgres    false    216    216    216            �           2606    33001    utente utente_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_email_key;
       public            postgres    false    222            �           2606    33003    utente utente_login_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_login_key UNIQUE (login);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_login_key;
       public            postgres    false    222            �           2620    33004    pagina diventaautore    TRIGGER     q   CREATE TRIGGER diventaautore AFTER INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.diventaautore();
 -   DROP TRIGGER diventaautore ON public.pagina;
       public          postgres    false    226    220            �           2620    33005    frasecorrente inserimento_frase    TRIGGER     �   CREATE TRIGGER inserimento_frase BEFORE INSERT ON public.frasecorrente FOR EACH ROW EXECUTE FUNCTION public.inserimento_frase();
 8   DROP TRIGGER inserimento_frase ON public.frasecorrente;
       public          postgres    false    216    227            �           2620    33006    modificaproposta modificaautore    TRIGGER     }   CREATE TRIGGER modificaautore AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.modificaautore();
 8   DROP TRIGGER modificaautore ON public.modificaproposta;
       public          postgres    false    229    217            �           2620    33007 "   modificaproposta notifica_modifica    TRIGGER     {   CREATE TRIGGER notifica_modifica AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.notificam();
 ;   DROP TRIGGER notifica_modifica ON public.modificaproposta;
       public          postgres    false    241    217            �           2620    33240 ,   modificaproposta settaggiodataoravalutazione    TRIGGER     �   CREATE TRIGGER settaggiodataoravalutazione AFTER UPDATE ON public.modificaproposta FOR EACH ROW WHEN ((old.stato = 0)) EXECUTE FUNCTION public.settaggiodataoravalutazione();
 E   DROP TRIGGER settaggiodataoravalutazione ON public.modificaproposta;
       public          postgres    false    217    246    217            �           2606    33008    modificaproposta fk_autorev    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_autorev FOREIGN KEY (autorev) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_autorev;
       public          postgres    false    217    222    4753            �           2606    33013    visiona fk_pagina    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_pagina;
       public          postgres    false    4751    220    224            �           2606    33018    frasecorrente fk_pagina    FK CONSTRAINT     ~   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 A   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT fk_pagina;
       public          postgres    false    216    220    4751            �           2606    33023 #   modificaproposta fk_stringainserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_stringainserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_stringainserita;
       public          postgres    false    4743    216    216    216    217    217    217            �           2606    33028    pagina fk_utente    FK CONSTRAINT     w   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT fk_utente FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT fk_utente;
       public          postgres    false    222    220    4753            �           2606    33033    visiona fk_utente    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_utente FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_utente;
       public          postgres    false    224    222    4753            �           2606    33038    modificaproposta fk_utentep    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_utentep FOREIGN KEY (utentep) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_utentep;
       public          postgres    false    4753    217    222            �           2606    33043    notifica notifica_idautore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idautore_fkey FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idautore_fkey;
       public          postgres    false    219    222    4753            �           2606    33048    notifica notifica_idpagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idpagina_fkey FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idpagina_fkey;
       public          postgres    false    220    4751    219            �           2606    33053    notifica notifica_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idutente_fkey;
       public          postgres    false    219    4753    222            �           2606    33065    collegamento pk_fraseinserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT pk_fraseinserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina);
 G   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT pk_fraseinserita;
       public          postgres    false    225    216    216    225    225    216    4743            B      x������ � �      9   )  x��U=��8���b�, �l�����^c\�s��hg �TH����+s�uW���Jv�{@�9|3����I�Ε���_�k�o��h�LɎ~W�3�P�O�ֵ�JW����uVQ��r/�\*�VVV.X
��d�1��V"+@Rzf�`bK;�Y�&!��*���*m ^7�j�ܥ�l�O��8�Ǔl���XO�题gS|�=��^謭�tt���(�J,Pe���燾�M�^�+���,��q��T����#���������l)���v�QC[��Ѷ�����1����a?;$�v;Egg"� ��t����T�٠�q,4��Ǵ�m���KŌtB�k@�A��p�mdA�q��D��,������nAI�L�7MO
k�2�H���`H��S�EM��>�Ph�G�ז�t�C�!&��}����a�V������=��v��M9�M�(��0P�ב LB�l��k���g�K�4��.���<�P�gUǪ:E��X�`_����}q������Q ��rE�b0�S	��O�6���?+"�ͩ��	�0��*M����F�Z�˷guc�ʗ�B�U�m�>d�e1��"�r��j�!A>�,�	DϴuM��1>;�Td�,r���c�(��+T>�2\���rQ���N�Zu�v]�}���|� �ݓ��"����a^J�0�NЌ�!F4ɚ&�Vo�<��I�>��ў��b�Eq���� �8m
_;U�KϐX�����^�JL��Zfy{�t�u#�w�����$�w�E1蒛��~y��]|�]�_ԕ�7�c���1���/u��#eE
��� ]�x&�[��#���h����ǇOۍ��O����fUoߡw����:	�>�� n�J�K����>�7|��BCC�,�F��vpYĆ��۞<Q�}?mر6���X솯.77��=hXl
��;�=�����&�N�K��XI?*n��2FN��<͖�\�**��Z-ޅΦ�<K��l�X!���x����ob��I��i��/��}��}�b���|:�aoB�q��@o7��e,�H|�gzww�Ȯ�O      :   6  x�}�KN�0�s�9@ke��kWB�
��bHBe��TN¢'�\�� �Z���ߟeE���J�����(PW�*Dir�*#6O�̄K5{�-t<z���w�t<1��m���쩫�������8��M�� 5����i#�,�������n���©���8�;��qO�ax�H�Mְ&B�K[J�&J����j����K�B`Y���L�5Eqi]�B�X�ڎW�G�@�Nm&�OWg��R��@s�.�5����/];���%�׶�|�%�]C�*E�X�fld�$���d      <   �   x���9�@D�x�dD��7z9�O@B@�EB��<�2�ߗ��̅�4��$L�-Z�[Ń�b`!*��x_��ko�s���$PLl�:�F���dnꮿ��{)E��U�G��@����������#������R      =   h   x���1�0�99�/���v�೰T�C�R���X��?=I�B��x�1��N�����Y����R�H[�1���q�qi4��b�V��[���?���m��^r��!�      ?   V   x�3�L,�L�O�LI,�LI�,J�K,����4200�50�54�J�d�s3s���s9CKR�JR�L�*H3����Ғ��T�=... ��-w      A   ,   x�3�4�4202�54�50�44�24�21г43233����� sC*     