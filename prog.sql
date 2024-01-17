PGDMP      	                 |            progetto    16.1    16.1 E    A           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            B           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            C           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            D           1262    24921    progetto    DATABASE     {   CREATE DATABASE progetto WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE progetto;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            E           0    0    SCHEMA public    COMMENT         COMMENT ON SCHEMA public IS '';
                   postgres    false    6            F           0    0    SCHEMA public    ACL     +   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
                   postgres    false    6                        3079    24922 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            G           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            Y           1247    24933 	   pk_utente    DOMAIN     +   CREATE DOMAIN public.pk_utente AS integer;
    DROP DOMAIN public.pk_utente;
       public          postgres    false    6            �            1255    24934    diventaautore()    FUNCTION     |  CREATE FUNCTION public.diventaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    24935    inserimento_frase()    FUNCTION     w  CREATE FUNCTION public.inserimento_frase() RETURNS trigger
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
       public          postgres    false    6            �            1255    24936 -   inserimento_frase(character varying, integer) 	   PROCEDURE     v  CREATE PROCEDURE public.inserimento_frase(IN stringa character varying, IN pagina integer)
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
       public          postgres    false    6            �            1255    24937    modificaautore()    FUNCTION       CREATE FUNCTION public.modificaautore() RETURNS trigger
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
       public          postgres    false    6            �            1255    24938    notificam()    FUNCTION     �  CREATE FUNCTION public.notificam() RETURNS trigger
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
       public          postgres    false    6            �            1255    24939 %   ricostruzione_versione(integer, date) 	   PROCEDURE     �  CREATE PROCEDURE public.ricostruzione_versione(IN pagina integer, IN datainsertita date)
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
       public          postgres    false    6            �            1255    24941    settaggiodataoravalutazione()    FUNCTION     9  CREATE FUNCTION public.settaggiodataoravalutazione() RETURNS trigger
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
       public          postgres    false    6            �            1259    24945    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    idpagina public.pk_utente NOT NULL,
    paginacollegata public.pk_utente NOT NULL
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false    857    857    6            �            1259    24950    frasecorrente    TABLE       CREATE TABLE public.frasecorrente (
    stringainserita character varying(1000) NOT NULL,
    numerazione integer NOT NULL,
    datainserimento date DEFAULT CURRENT_DATE,
    orainserimento time without time zone DEFAULT CURRENT_TIME,
    idpagina integer NOT NULL
);
 !   DROP TABLE public.frasecorrente;
       public         heap    postgres    false    6            �            1259    24957    modificaproposta    TABLE     �  CREATE TABLE public.modificaproposta (
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
       public         heap    postgres    false    6            �            1259    24965    modificaproposta_idmodifica_seq    SEQUENCE     �   CREATE SEQUENCE public.modificaproposta_idmodifica_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.modificaproposta_idmodifica_seq;
       public          postgres    false    6    218            H           0    0    modificaproposta_idmodifica_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.modificaproposta_idmodifica_seq OWNED BY public.modificaproposta.idmodifica;
          public          postgres    false    219            �            1259    24966    notifica    TABLE     �   CREATE TABLE public.notifica (
    idpagina public.pk_utente NOT NULL,
    idutente public.pk_utente,
    data date,
    ora time without time zone,
    idautore public.pk_utente NOT NULL,
    titolo character varying(80)
);
    DROP TABLE public.notifica;
       public         heap    postgres    false    857    857    857    6            �            1259    24969    pagina    TABLE     �   CREATE TABLE public.pagina (
    idpagina integer NOT NULL,
    titolo character varying(80),
    dataoracreazione timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idautore integer NOT NULL
);
    DROP TABLE public.pagina;
       public         heap    postgres    false    6            �            1259    24973    pagina_idpagina_seq    SEQUENCE     �   CREATE SEQUENCE public.pagina_idpagina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.pagina_idpagina_seq;
       public          postgres    false    6    221            I           0    0    pagina_idpagina_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.pagina_idpagina_seq OWNED BY public.pagina.idpagina;
          public          postgres    false    222            �            1259    24974    utente    TABLE     z  CREATE TABLE public.utente (
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
       public         heap    postgres    false    6            �            1259    24979    utente_idutente_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_idutente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.utente_idutente_seq;
       public          postgres    false    6    223            J           0    0    utente_idutente_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.utente_idutente_seq OWNED BY public.utente.idutente;
          public          postgres    false    224            �            1259    24980    visiona    TABLE     �   CREATE TABLE public.visiona (
    idutente integer NOT NULL,
    idpagina integer NOT NULL,
    datavisone date DEFAULT CURRENT_DATE,
    oravisione time without time zone DEFAULT CURRENT_TIME
);
    DROP TABLE public.visiona;
       public         heap    postgres    false    6            w           2604    24985    modificaproposta idmodifica    DEFAULT     �   ALTER TABLE ONLY public.modificaproposta ALTER COLUMN idmodifica SET DEFAULT nextval('public.modificaproposta_idmodifica_seq'::regclass);
 J   ALTER TABLE public.modificaproposta ALTER COLUMN idmodifica DROP DEFAULT;
       public          postgres    false    219    218            {           2604    24986    pagina idpagina    DEFAULT     r   ALTER TABLE ONLY public.pagina ALTER COLUMN idpagina SET DEFAULT nextval('public.pagina_idpagina_seq'::regclass);
 >   ALTER TABLE public.pagina ALTER COLUMN idpagina DROP DEFAULT;
       public          postgres    false    222    221            }           2604    24987    utente idutente    DEFAULT     r   ALTER TABLE ONLY public.utente ALTER COLUMN idutente SET DEFAULT nextval('public.utente_idutente_seq'::regclass);
 >   ALTER TABLE public.utente ALTER COLUMN idutente DROP DEFAULT;
       public          postgres    false    224    223            5          0    24945    collegamento 
   TABLE DATA           _   COPY public.collegamento (stringainserita, numerazione, idpagina, paginacollegata) FROM stdin;
    public          postgres    false    216   �c       6          0    24950    frasecorrente 
   TABLE DATA           p   COPY public.frasecorrente (stringainserita, numerazione, datainserimento, orainserimento, idpagina) FROM stdin;
    public          postgres    false    217   �c       7          0    24957    modificaproposta 
   TABLE DATA           �   COPY public.modificaproposta (idmodifica, stringaproposta, stato, dataproposta, oraproposta, datavalutazione, oravalutazione, utentep, autorev, stringainserita, numerazione, idpagina) FROM stdin;
    public          postgres    false    218   h       9          0    24966    notifica 
   TABLE DATA           S   COPY public.notifica (idpagina, idutente, data, ora, idautore, titolo) FROM stdin;
    public          postgres    false    220   Vi       :          0    24969    pagina 
   TABLE DATA           N   COPY public.pagina (idpagina, titolo, dataoracreazione, idautore) FROM stdin;
    public          postgres    false    221   �i       <          0    24974    utente 
   TABLE DATA           e   COPY public.utente (idutente, login, password, nome, cognome, datanascita, email, ruolo) FROM stdin;
    public          postgres    false    223   �j       >          0    24980    visiona 
   TABLE DATA           M   COPY public.visiona (idutente, idpagina, datavisone, oravisione) FROM stdin;
    public          postgres    false    225   �j       K           0    0    modificaproposta_idmodifica_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.modificaproposta_idmodifica_seq', 26, true);
          public          postgres    false    219            L           0    0    pagina_idpagina_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.pagina_idpagina_seq', 4, true);
          public          postgres    false    222            M           0    0    utente_idutente_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.utente_idutente_seq', 6, true);
          public          postgres    false    224            �           2606    24989    collegamento collegamento_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pkey PRIMARY KEY (stringainserita, numerazione, idpagina, paginacollegata);
 H   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pkey;
       public            postgres    false    216    216    216    216            �           2606    24991    frasecorrente pk_frase 
   CONSTRAINT     x   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT pk_frase PRIMARY KEY (stringainserita, numerazione, idpagina);
 @   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT pk_frase;
       public            postgres    false    217    217    217            �           2606    24993    modificaproposta pk_modifica 
   CONSTRAINT     b   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT pk_modifica PRIMARY KEY (idmodifica);
 F   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT pk_modifica;
       public            postgres    false    218            �           2606    24995    pagina pk_pagina 
   CONSTRAINT     T   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pk_pagina PRIMARY KEY (idpagina);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pk_pagina;
       public            postgres    false    221            �           2606    24997    utente pk_utente 
   CONSTRAINT     T   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT pk_utente PRIMARY KEY (idutente);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT pk_utente;
       public            postgres    false    223            �           2606    24999    visiona pk_visona 
   CONSTRAINT     _   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT pk_visona PRIMARY KEY (idutente, idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT pk_visona;
       public            postgres    false    225    225            �           2606    25001     frasecorrente unique_numerazione 
   CONSTRAINT     l   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT unique_numerazione UNIQUE (numerazione, idpagina);
 J   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT unique_numerazione;
       public            postgres    false    217    217            �           2606    25003    frasecorrente uniquefrase 
   CONSTRAINT     v   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT uniquefrase UNIQUE (stringainserita, numerazione, idpagina);
 C   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT uniquefrase;
       public            postgres    false    217    217    217            �           2606    25005    utente utente_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_email_key;
       public            postgres    false    223            �           2606    25007    utente utente_login_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_login_key UNIQUE (login);
 A   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_login_key;
       public            postgres    false    223            �           2620    25008    pagina diventaautore    TRIGGER     q   CREATE TRIGGER diventaautore AFTER INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.diventaautore();
 -   DROP TRIGGER diventaautore ON public.pagina;
       public          postgres    false    221    226            �           2620    25009    frasecorrente inserimento_frase    TRIGGER     �   CREATE TRIGGER inserimento_frase BEFORE INSERT ON public.frasecorrente FOR EACH ROW EXECUTE FUNCTION public.inserimento_frase();
 8   DROP TRIGGER inserimento_frase ON public.frasecorrente;
       public          postgres    false    227    217            �           2620    25010    modificaproposta modificaautore    TRIGGER     }   CREATE TRIGGER modificaautore AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.modificaautore();
 8   DROP TRIGGER modificaautore ON public.modificaproposta;
       public          postgres    false    229    218            �           2620    25011 "   modificaproposta notifica_modifica    TRIGGER     {   CREATE TRIGGER notifica_modifica AFTER INSERT ON public.modificaproposta FOR EACH ROW EXECUTE FUNCTION public.notificam();
 ;   DROP TRIGGER notifica_modifica ON public.modificaproposta;
       public          postgres    false    241    218            �           2620    25012 ,   modificaproposta settaggiodataoravalutazione    TRIGGER     �   CREATE TRIGGER settaggiodataoravalutazione AFTER UPDATE ON public.modificaproposta FOR EACH ROW WHEN ((old.stato = 0)) EXECUTE FUNCTION public.settaggiodataoravalutazione();
 E   DROP TRIGGER settaggiodataoravalutazione ON public.modificaproposta;
       public          postgres    false    218    218    243            �           2606    25013    modificaproposta fk_autorev    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_autorev FOREIGN KEY (autorev) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_autorev;
       public          postgres    false    4751    218    223            �           2606    25018    visiona fk_pagina    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_pagina;
       public          postgres    false    4749    221    225            �           2606    25023    frasecorrente fk_pagina    FK CONSTRAINT     ~   ALTER TABLE ONLY public.frasecorrente
    ADD CONSTRAINT fk_pagina FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 A   ALTER TABLE ONLY public.frasecorrente DROP CONSTRAINT fk_pagina;
       public          postgres    false    4749    217    221            �           2606    25028 #   modificaproposta fk_stringainserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_stringainserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_stringainserita;
       public          postgres    false    218    218    218    217    217    217    4741            �           2606    25033    pagina fk_utente    FK CONSTRAINT     w   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT fk_utente FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT fk_utente;
       public          postgres    false    221    223    4751            �           2606    25038    visiona fk_utente    FK CONSTRAINT     x   ALTER TABLE ONLY public.visiona
    ADD CONSTRAINT fk_utente FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 ;   ALTER TABLE ONLY public.visiona DROP CONSTRAINT fk_utente;
       public          postgres    false    225    223    4751            �           2606    25043    modificaproposta fk_utentep    FK CONSTRAINT     �   ALTER TABLE ONLY public.modificaproposta
    ADD CONSTRAINT fk_utentep FOREIGN KEY (utentep) REFERENCES public.utente(idutente);
 E   ALTER TABLE ONLY public.modificaproposta DROP CONSTRAINT fk_utentep;
       public          postgres    false    218    4751    223            �           2606    25048    notifica notifica_idautore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idautore_fkey FOREIGN KEY (idautore) REFERENCES public.utente(idutente);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idautore_fkey;
       public          postgres    false    223    220    4751            �           2606    25053    notifica notifica_idpagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idpagina_fkey FOREIGN KEY (idpagina) REFERENCES public.pagina(idpagina);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idpagina_fkey;
       public          postgres    false    221    4749    220            �           2606    25058    notifica notifica_idutente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(idutente);
 I   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_idutente_fkey;
       public          postgres    false    4751    223    220            �           2606    25063    collegamento pk_fraseinserita    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT pk_fraseinserita FOREIGN KEY (stringainserita, numerazione, idpagina) REFERENCES public.frasecorrente(stringainserita, numerazione, idpagina);
 G   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT pk_fraseinserita;
       public          postgres    false    216    217    217    217    4741    216    216            5      x������ � �      6   B  x��U���8��_1e�K�eٝ���^c\�s��hg �T(��~͕�����c������4E�̼y|~R�s%�����;>+�*S���U�GT���u�����vp�F�U�^J��v\*�NVV.Xju��jc�ɝDV<�����Ėvڳ�MD��U�r�Wi�agU��.�$�t�f�$�ΒI���|��2_$��q��/t��j::[�usT%(��U�����ئ�^�+���,��q��T�����T�cS�G���َR��s���2��Rɣ��Ε�S����~~�������Ad'�Z���Rao[M�c��]>��n;V<^��#�\��6�km2���$:��摄5^����^�������s���#�� �R{*�Ƞɳ�g�
M8��%4{������M@@�k+����|
���p�xW��䦜��&�x��Z(��@ &!Z6����މ�m��A�%j�[�.�/�<�P�gU��zE��X�`_����}~������Q ��rE�B0�S	��O�6��?+"�ͩ���8��*�d?om�PN�L���[fuc�ʗ�ڲ�t�1}HVE>��2x��r5È� �J���gں�����\�D�\Z.�\���*�%	�'iXf���d%�3�V��]�h_��Ek+H�����HvE|z؄�ɲ�	�Q?Ĉ�!Yӄ���-�Ǔ>�Ї�>��^ȸ���� r��M��^U>��3$V�5|�w���᳖Y�^5]�|݈����r�;
�-� ]r"8��ڇ���]Y�3>��c<��R��;RV��[y� ���gR��	8�1@�
��{�||������,���aV������Z��FZG��A 7O���%[���in<���X�!����Q#�`�uYĆ�<؞<Q��0mر6ᯪ��X솯.77��=hX����wB{���ǇM<Y�������~��"�"Df�d'�*�@������'�w��l�&q:�/�+�џ�2�I�&6[���<�����������u���l��4ބB?�?���n,�e$��a�n����i���z���,�e�t1G�www����      7   B  x�}�Mn1���> Dq�����*$� X��E
1
3]p�ޣk�vQ!�����dk#���9�/��V��P�t-�5�5Xʪ*l]_H�4��:g
Q�R�s:D:p�"[&ڎ�\S6�HmU��R��daM]���
�����CV���/c_��� �Q(k��(ZO�_�9Z�@Ӡn�����m�ϰX�6F:����E�����CB_��tR�)��g-���r���Gy�*6���|��i�v0Dh�B`����%�Y��<yn	r��R��g���Ǟ����w;�y�S�2<S�-G���a7�,�] t����׬�d2��Z�
      9   �   x���9�@D�x�dD��7z9�O@B@�EB��<�2�ߗ��̅�4��$L�-Z�[Ń�b`!*��x_��ko�s���$PLl�:�F���dnꮿ��{)E��U�G��@����������#������R      :   �   x���;� E�V1 1���l#m+B1f�
nҦ���j;|���2vE��A2I\�-�TФZ�s��U�r]�WS̂,�-��\��Sg�`ɧD�6����9�B�]���u�`��%	�b
�O����.d      <   V   x�3�L,�L�O�LI,�LI�,J�K,����4200�50�54�J�d�s3s���s9CKR�JR�L�*H3����Ғ��T�=... ��-w      >   ,   x�3�4�4202�54�50�44�24�21г43233����� sC*     