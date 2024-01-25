PGDMP       1                 |            progetto    16.0    16.0 C    >           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            ?           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            @           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            A           1262    17130    progetto    DATABASE     {   CREATE DATABASE progetto WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE progetto;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            B           0    0    SCHEMA public    COMMENT         COMMENT ON SCHEMA public IS '';
                   postgres    false    6            C           0    0    SCHEMA public    ACL     +   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
                   postgres    false    6                        3079    17131 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            D           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            Y           1247    17142 	   pk_utente    DOMAIN     +   CREATE DOMAIN public.pk_utente AS integer;
    DROP DOMAIN public.pk_utente;
       public          postgres    false    6            �            1255    17143    diventaautore()    FUNCTION     |  CREATE FUNCTION public.diventaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    17144    inserimento_frase()    FUNCTION     w  CREATE FUNCTION public.inserimento_frase() RETURNS trigger
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
       public          postgres    false    6            �            1255    17145 -   inserimento_frase(character varying, integer) 	   PROCEDURE     v  CREATE PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer)
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
       public          postgres    false    6            �            1255    17146    modificaautore()    FUNCTION       CREATE FUNCTION public.modificaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    17147    notificam()    FUNCTION       CREATE FUNCTION public.notificam() RETURNS trigger
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
$$;
 "   DROP FUNCTION public.notificam();
       public          postgres    false    6            �            1255    17148 %   ricostruzione_versione(integer, date) 	   PROCEDURE     �  CREATE PROCEDURE public.ricostruzione_versione(IN pagina integer, IN datainsertita date)
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
       public          postgres    false    6            �            1255    17149    settaggiodataoravalutazione()    FUNCTION     9  CREATE FUNCTION public.settaggiodataoravalutazione() RETURNS trigger
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
       public          postgres    false    6            �            1259    17150    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    idpagina public.pk_utente NOT NULL,
    paginacollegata public.pk_utente NOT NULL
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false    6    857    857            �            1259    17155    frasecorrente    TABLE       CREATE TABLE public.frasecorrente (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    datainserimento date DEFAULT CURRENT_DATE,
    orainserimento time without time zone DEFAULT CURRENT_TIME,
    idpagina integer NOT NULL
);
 !   DROP TABLE public.frasecorrente;
       public         heap    postgres    false    6            �            1259    17162    modificaproposta    TABLE     �  CREATE TABLE public.modificaproposta (
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
       public         heap    postgres    false    6            �            1259    17170    modificaproposta_idmodifica_seq    SEQUENCE     �   CREATE SEQUENCE public.modificaproposta_idmodifica_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.modificaproposta_idmodifica_seq;
       public          postgres    false    6    218            E           0    0    modificaproposta_idmodifica_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.modificaproposta_idmodifica_seq OWNED BY public.modificaproposta.idmodifica;
          public          postgres    false    219            �            1259    17293    notifica    TABLE     �   CREATE TABLE public.notifica (
    idautore public.pk_utente NOT NULL,
    idmodifica public.pk_utente NOT NULL,
    data date,
    ora time without time zone,
    titolo character varying(80)
);
    DROP TABLE public.notifica;
       public         heap    postgres    false    857    6    857            �            1259    17174    pagina    TABLE     �   CREATE TABLE public.pagina (
    idpagina integer NOT NULL,
    titolo character varying(80),
    dataoracreazione timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idautore integer NOT NULL
);
    DROP TABLE public.pagina;
       public         heap    postgres    false    6            �            1259    17178    pagina_idpagina_seq    SEQUENCE     �   CREATE SEQUENCE public.pagina_idpagina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.pagina_idpagina_seq;
       public          postgres    false    220    6            F           0    0    pagina_idpagina_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.pagina_idpagina_seq OWNED BY public.pagina.idpagina;
          public          postgres    false    221            �            1259    17179    utente    TABLE     z  CREATE TABLE public.utente (
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
       public         heap    postgres    false    6            �            1259    17184    utente_idutente_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_idutente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.utente_idutente_seq;
       public          postgres    false    6    222            G           0    0    utente_idutente_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.utente_idutente_seq OWNED BY public.utente.idutente;
          public          postgres    false    223            �            1259    17278    visiona    TABLE     �   CREATE TABLE public.visiona (
    idutente integer,
    idpagina integer,
    datavisone date DEFAULT CURRENT_DATE,
    oravisione time without time zone DEFAULT CURRENT_TIME
);
    DROP TABLE public.visiona;
       public         heap    postgres    false    6            w           2604    17190    modificaproposta idmodifica    DEFAULT     �   ALTER TABLE ONLY public.modificaproposta ALTER COLUMN idmodifica SET DEFAULT nextval('public.modificaproposta_idmodifica_seq'::regclass);
 J   ALTER TABLE public.modificaproposta ALTER COLUMN idmodifica DROP DEFAULT;
       public          postgres    false    219    218            {           2604    17191    pagina idpagina    DEFAULT     r   ALTER TABLE ONLY public.pagina ALTER COLUMN idpagina SET DEFAULT nextval('public.pagina_idpagina_seq'::regclass);
 >   ALTER TABLE public.pagina ALTER COLUMN idpagina DROP DEFAULT;
       public          postgres    false    221    220            }           2604    17192    utente idutente    DEFAULT     r   ALTER TABLE ONLY public.utente ALTER COLUMN idutente SET DEFAULT nextval('public.utente_idutente_seq'::regclass);
 >   ALTER TABLE public.utente ALTER COLUMN idutente DROP DEFAULT;
       public          postgres    false    223    222            2          0    17150    collegamento 
   TABLE DATA           _   COPY public.collegamento (stringainserita, numerazione, idpagina, paginacollegata) FROM stdin;
    public          postgres    false    216   �`       3          0    17155    frasecorrente 
   TABLE DATA           p   COPY public.frasecorrente (stringainserita, numerazione, datainserimento, orainserimento, idpagina) FROM stdin;
    public          postgres    false    217   �`       4          0    17162    modificaproposta 
   TABLE DATA           �   COPY public.modificaproposta (idmodifica, stringaproposta, stato, dataproposta, oraproposta, datavalutazione, oravalutazione, utentep, autorev, stringainserita, numerazione, idpagina) FROM stdin;
    public          postgres    false    218   �e       ;          0    17293    notifica 
   TABLE DATA           K   COPY public.notifica (idautore, idmodifica, data, ora, titolo) FROM stdin;
    public          postgres    false    225   n       6          0    17174    pagina 
   TABLE DATA           N   COPY public.pagina (idpagina, titolo, dataoracreazione, idautore) FROM stdin;
    public          postgres    false    220   cn       8          0    17179    utente 
   TABLE DATA           e   COPY public.utente (idutente, login, password, nome, cognome, datanascita, email, ruolo) FROM stdin;
    public          postgres    false    222   =o       :          0    17278    visiona 
   TABLE DATA           M   COPY public.visiona (idutente, idpagina, datavisone, oravisione) FROM stdin;
    public          postgres    false    224   p       H           0    0    modificaproposta_idmodifica_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.modificaproposta_idmodifica_seq', 134, true);
          public          postgres    false    219            I           0    0    pagina_idpagina_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.pagina_idpagina_seq', 8, true);
          public          postgres    false    221            J           0    0    utente_idutente_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.utente_idutente_seq', 16, true);
          public          postgres    false    223            �           2606    17194    collegamento collegamento_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pkey PRIMARY KEY (stringainserita, numerazione, idpagina, paginacollegata);
 H   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pkey;
       public            postgres    false    216    216    216    216            �           2606    17196    frasecorrente pk_frase 
   CONSTRAINT     x   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT pk_frase PRIMARY KEY (stringainserita, numerazione, idpagina);
 @   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT pk_frase;
       public            postgres    false    217    217    217            �           2606    17198    modificaproposta pk_modifica 
   CONSTRAINT     b   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT pk_modifica PRIMARY KEY (idmodifica);
 F   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT pk_modifica;
       public            postgres    false    218            �           2606    17200    pagina pk_pagina 
   CONSTRAINT     T   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pk_pagina PRIMARY KEY (idpagina);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pk_pagina;
       public            postgres    false    220            �           2606    17202    utente pk_utente 
   CONSTRAINT     T   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT pk_utente PRIMARY KEY (idutente);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT pk_utente;
       public            postgres    false    222            �           2606    17206     frasecorrente unique_numerazione 
   CONSTRAINT     l   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT unique_numerazione UNIQUE (numerazione, idpagina);
 J   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT unique_numerazione;
       public            postgres    false    217    217            �           2606    17208    frasecorrente uniquefrase 
   CONSTRAINT     v   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT uniquefrase UNIQUE (stringainserita, numerazione, idpagina);
 C   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT uniquefrase;
       public            postgres    false    217    217    217            �           2606    17210    utente utente_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_email_key;
       public            postgres    false    222            �           2606    17212    utente utente_login_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_login_key UNIQUE (login);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_login_key;
       public            postgres    false    222            �           2620    17213    pagina diventaautore    TRIGGER     q   CREATE TRIGGER diventaautore AFTER INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.diventaautore();
 -   DROP TRIGGER diventaautore ON public.pagina;
       public          postgres    false    226    220            �           2620    17214    frasecorrente inserimento_frase    TRIGGER     �   CREATE TRIGGER inserimento_frase BEFORE INSERT ON public.frasecorrente FOR EACH ROW EXECUTE FUNCTION public.inserimento_frase();
 8   DROP TRIGGER inserimento_frase ON public.frasecorrente;
       public          postgres    false    217    227            �           2620    17215    modificaproposta modificaautore    TRIGGER     }   CREATE TRIGGER modificaautore AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.modificaautore();
 8   DROP TRIGGER modificaautore ON public.modificaproposta;
       public          postgres    false    218    229            �           2620    17216 "   modificaproposta notifica_modifica    TRIGGER     {   CREATE TRIGGER notifica_modifica AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.notificam();
 ;   DROP TRIGGER notifica_modifica ON public.modificaproposta;
       public          postgres    false    243    218            �           2620    17217 ,   modificaproposta settaggiodataoravalutazione    TRIGGER     �   CREATE TRIGGER settaggiodataoravalutazione AFTER UPDATE ON public.modificaproposta FOR EACH ROW WHEN ((old.stato = 0)) EXECUTE FUNCTION public.settaggiodataoravalutazione();
 E   DROP TRIGGER settaggiodataoravalutazione ON public.modificaproposta;
       public          postgres    false    242    218    218            �           2606    17218    modificaproposta fk_autorev    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_autorev FOREIGN KEY (autorev) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_autorev;
       public          postgres    false    218    4751    222            �           2606    17228    frasecorrente fk_pagina    FK CONSTRAINT     ~   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 A   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT fk_pagina;
       public          postgres    false    220    4749    217            �           2606    17288    visiona fk_pagina    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_pagina;
       public          postgres    false    4749    224    220            �           2606    17233 #   modificaproposta fk_stringainserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_stringainserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_stringainserita;
       public          postgres    false    218    217    218    217    217    218    4741            �           2606    17238    pagina fk_utente    FK CONSTRAINT     w   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT fk_utente FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT fk_utente;
       public          postgres    false    222    220    4751            �           2606    17283    visiona fk_utente    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_utente FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_utente;
       public          postgres    false    4751    224    222            �           2606    17248    modificaproposta fk_utentep    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_utentep FOREIGN KEY (utentep) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_utentep;
       public          postgres    false    222    218    4751            �           2606    17301    notifica notifica_idautore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idautore_fkey FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idautore_fkey;
       public          postgres    false    222    4751    225            �           2606    17296 !   notifica notifica_idmodifica_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idmodifica_fkey FOREIGN KEY (idmodifica) REFERENCES public.modificaproposta(idmodifica);
 K   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idmodifica_fkey;
       public          postgres    false    225    4747    218            �           2606    17268    collegamento pk_fraseinserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT pk_fraseinserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina);
 G   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT pk_fraseinserita;
       public          postgres    false    217    217    217    216    216    216    4741            2      x������ � �      3   �  x��U=s�6��_�Қ�q�}�u���hFN�qu�"��΀ �W�פt�t�����%E�I,��}x����6���?�]o�HG)�R7d�ﲷ�Bѩ�Ƨ�����y������	��}�H�4JM��#�*a�����#[Z@"�4�q�)q
�ur����*�'k�hO�6
� ��l���8	�t������e�d����6=݋�2F�kZގbd��$�:-w�~n�j�^��,�q�������c�8��VۑY��kk��G���r%7fTn^�Z� �հ���|
�;Iq��"GN7Щ�4�
'3(�[b���#qw�]*^6��P�hm�4�֚:e<<���8:��<�ީ��=���)��oJ�J���h�T>��,� �F9�<�#��X��ڊa���0	!H0	�y���Mk��ۑ��{g;;������D鯅����`�e-��&�,+ތX�uؠ�e} c�]����9���j2P�݀e�^9-ڕ{�,���\.+@	�L.��W���Dɚ��ab�e�E��[ۂ ��K�I��}�3��1S�s_C3�jI|H�\�^�x�c�	�g�!z[��J��cǛ��eʛ�^ץ��J�$��$����5�5j;)q7��5z9ъ��
��R(�/�]�����|�g���s�f��4$�{�ZY�F��U����s�����e�[�Y�������m����$�z�����,k�
���gy>��4�ӏ�i���B/�*�bP�>��]��& =:�?�+���G��+c����:�ё4,�������	9[�#�+�(o�jr��ǇO�+������%��پ�ٱ~X� ���`�����@�$tI�9�K���A��[x�aB�d5"~�!��Ͷ�G�(7Ov���������n��Bps! /΃�Uр����,��m����*
��|?a2���92�|d�I�(���**��F
�oB�l�&Q��u���'�+.�^�f����e����,���ocכ���,Oa�B�v��yG�E(�*�x�����;�y�M�l�,��"-�1i�}�&K��Ä�P�<�b�Ӿ8N�s��$�h��w�q#�|YB��� �e���K�gѺ77��uP]�]\\���ו      4   o  x��[͎�>s�B���,V�O��c�6�=���%=R��>M�y�X���YId3>�=X`���W,��Q|��Ǐ��~��� ������k/ �:�`�s��_@�-z� 4�+�����0�;���iJ��rѶ�R��@Kȵ�!�.I5���n//�U��o?���[Z��d��tq��&�V  [�-�$GJ��&��=X6�(�RA�8֏�f3��9<�~����f�|��û�iן���)�6��|�O]?��������㱋��]s�M|�������ϻc<�W�{�C7lb�����?�w����������=͇�S�C�S<����wI���d5�n�@0A
L�:q8�񅄗�2e���C��Bɦ����C�����}!��Tn�-U��I��jP��16�������4�/�96����W��8Bb�a���sdM<�� ����Q-��M�w:wLbw��.��Cߜv�]"�u�����mw"���=?��5�v�n�|xw��{b��w[&t�3��^������[�Zv.I��5����e0�NaA�W� �Ef�3�ڴ�G(Ԡ�T+bs���]&�� v]4�5�M� ���b�Bk�D/a�R�������T�I������5^|�0Ч����&EȠ��X�16���h�� ��֠��H_e�YG�*j�K0��$A,�cqJܝ͛��A�)�x|\̇�������d>�S�Sn[P�j�5ՠ��Řt����e�����q����b��*ݒ��}�R*:6��Ͱ�䓤�i�<��ﳼKK-����j���8�d-D(��x��\5{7[p��P�%����|s�R��xoS��®9��^�%�Yp�Ak!��a(�3�郳����e��A��"��[��8o|Wf��3��QlR��0�1��`��d-:�l�=��F"�RP�7~�����ӛM[
�P8oj�Z��bX�xv ��tP`���7xħ�q܃�� xy�c�<F�s۝A+���.������\����Ks�Ak�*��I��USh-<�*������l�N��-����q��oM�rR�d�P�J��U�咙s�.��b���l��?�˴�֡
5��,��X���`<��b#�Ak!9�&b�����{�d�*�B�I�(�tN]x�湐�ٍQ*���B�����!��:��Ԡ������'��5��}��/��l�;�Hנ��k1s5bH�Ё
ˠ:�JÛ���ZI��$j���AkQ!��ݝNy�&l��`5�D2�Jp��`V@�io��4�; �S���q�3-��,��e>�"%̋�+��ٕ��N<� ����ſc�����dˣ�2+x�
_b:^0�ӋL�\3(�*HɼP���k+�%zP�0Ԡ�`�a�K��t�m2�Sh%���j��E&m�		��AbA� 2% '�#D]�J�2�][�0���d��f�Da���Z�@*�}�5h%$�b���6��v�ejP�ƜȌ��s�Io�=�r�7�J�p6�!���[�9�*9h�v����S5�)ckP�]m�p�˷��C�j�J,*qᖫ��"и�KbgP�X%��rmz�e��T�넣L"�VkMX�Z��r�T�_�RK`�Ǡ��;����+Z�?@�ߥ,�*�-�T��/ӗNw
�uc]�1�"�i��4Ѧj�%��T��Hߢ���`��:��+'����<�H��*���ۢ<r�t���[TY�B�a�����d�l'P��F�YVCQ&Y{��eD�A���w0��/���)��"�)�ſ6]��L�UHe��K��A�!6��?9�R�Z�!l
����qL�w��|��#�" ,��L`Ȃ��Ѥ[�E���֢0�š;_}0]1��(Ԡ"�(��9I�SKNc*�$/���8g!;�V�63RQ��l��
��+��w%�I�^���+�\��8���~�\���{���'��n_@��&z���R]D��|Z�A��M��t��`:$@vr� �0��x_*��Fͪs��8�X�]*�C��ߤ~�d�~eb8]i_����x?kSk2�gD����?gH�yx��M:���S��(!��"�H�K^��'�/�+�U��)�&��������      ;   <   x�3�446�4202�50�52�4��20�25�334030��IT��/*IU�K-J����� s      6   �   x���Aj�0еs
_ F�%��5��&tL	��	s����R����/t���qV��cqg��#d��8kr2�۪����^��V����l�9 h�:�����)��f�n~oo��z�}��l#�}ʌ�$�� �����>���j�AYA�+}(O���
	��Yr��^�K��b2���?/�?��b����t��i��sN�      8   �   x����
� ����x9v�bwAH�*Q*_G�U�.�G���?�@OP�=���=�΂▉<C�^m7Ż�m�k��k4�hXN��<P\��h�awp4:��s������X}����u����C�f��H�Ew��,��뉣�oA���$�����JA{�C�,!�(,9c�B�,      :   �  x�u�˱[1C�v/���Q-鿎@�d_���$ *^�RV���K�%z$�uYE��OlBiS���먑���x�;���Վ:��Z�[����j��~D)��n��b���]�c+r��^��^��ζ���^L�){u`p��,�qb�*[4�92X;�c��'�2�m��텘{l�ؘ�yX&�v��ݫ�h��!cѶ�N��4a��Z꺝���x� 	��f�k�vjf��0$O)��\�'��Z�hi��y��q#��m;�!oY���Q4�)�ݬ��'�����(��k�&����6�?��q#��3_f�Ñ'9W��A	��G��
²c�Td�؟��R�\߆��T��ᤂ(���Ƈ�͖��샯*��ZqD�@~������     