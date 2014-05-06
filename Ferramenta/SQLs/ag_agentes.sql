--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:06:03 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 774 (class 1259 OID 165037)
-- Dependencies: 7
-- Name: ag_agentes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_agentes (
    id integer NOT NULL,
    nome character varying
);


ALTER TABLE public.ag_agentes OWNER TO postgres;

--
-- TOC entry 3756 (class 0 OID 165037)
-- Dependencies: 774 3757
-- Data for Name: ag_agentes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_agentes (id, nome) FROM stdin;
1	acompanhanteTutorAgente
2	ajudanteAgente
3	buscadorAgente
4	companheiroAgente
5	coordenadorAgente
6	formadorAgente
7	pedagogicoAgente
8	acompanhanteDeProfessores
\.


--
-- TOC entry 3654 (class 2606 OID 196690)
-- Dependencies: 774 774 3758
-- Name: pk2; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_agentes
    ADD CONSTRAINT pk2 PRIMARY KEY (id);


-- Completed on 2014-04-24 18:06:03 BRT

--
-- PostgreSQL database dump complete
--
