--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:07:23 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 779 (class 1259 OID 182537)
-- Dependencies: 7
-- Name: ag_material; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_material (
    caminho character varying(255),
    link character varying(255),
    nome character varying(255),
    tipo character varying(255),
    id_assunto integer,
    id integer NOT NULL
);


ALTER TABLE public.ag_material OWNER TO postgres;

--
-- TOC entry 780 (class 1259 OID 182543)
-- Dependencies: 779 7
-- Name: ag_material_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_material_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_material_id_seq OWNER TO postgres;

--
-- TOC entry 3765 (class 0 OID 0)
-- Dependencies: 780
-- Name: ag_material_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_material_id_seq OWNED BY ag_material.id;


--
-- TOC entry 3653 (class 2604 OID 182545)
-- Dependencies: 780 779
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_material ALTER COLUMN id SET DEFAULT nextval('ag_material_id_seq'::regclass);


--
-- TOC entry 3759 (class 0 OID 182537)
-- Dependencies: 779 3761
-- Data for Name: ag_material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_material (caminho, link, nome, tipo, id_assunto, id) FROM stdin;
/var/www/moodle/SMA/files/1385406895/CMAISMAIS.pdf		CMAISMAIS.pdf	application/pdf	11	39
/var/www/moodle_uece/SMA/files/1386622143/26131200776574001551550010008652661687130300.xml		26131200776574001551550010008652661687130300.xml	text/xml	13	40
/var/www/moodle_uece/SMA/files/1393010155/resumo.odt		resumo.odt	application/vnd.oasis.opendocument.text	11	41
/var/www/moodle_uece/SMA/files/1393533758/ler.odt		ler.odt	application/vnd.oasis.opendocument.text	11	42
\.


--
-- TOC entry 3766 (class 0 OID 0)
-- Dependencies: 780
-- Name: ag_material_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_material_id_seq', 42, true);


--
-- TOC entry 3655 (class 2606 OID 182561)
-- Dependencies: 779 779 3762
-- Name: ag_material_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_material
    ADD CONSTRAINT ag_material_key PRIMARY KEY (id);


--
-- TOC entry 3656 (class 1259 OID 182559)
-- Dependencies: 779 3762
-- Name: fki_fkcb6cd520e5e68865; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_fkcb6cd520e5e68865 ON ag_material USING btree (id_assunto);


--
-- TOC entry 3657 (class 2606 OID 182554)
-- Dependencies: 777 779 3762
-- Name: fkcb6cd520e5e68865; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_material
    ADD CONSTRAINT fkcb6cd520e5e68865 FOREIGN KEY (id_assunto) REFERENCES ag_assunto(id);


-- Completed on 2014-04-24 18:07:24 BRT

--
-- PostgreSQL database dump complete
--

