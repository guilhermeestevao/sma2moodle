--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:06:24 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 782 (class 1259 OID 182625)
-- Dependencies: 7
-- Name: ag_coordenador; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_coordenador (
    id integer NOT NULL,
    curso numeric(19,2),
    email character varying(255)
);


ALTER TABLE public.ag_coordenador OWNER TO postgres;

--
-- TOC entry 781 (class 1259 OID 182623)
-- Dependencies: 782 7
-- Name: ag_coordenador_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_coordenador_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_coordenador_id_seq OWNER TO postgres;

--
-- TOC entry 3763 (class 0 OID 0)
-- Dependencies: 781
-- Name: ag_coordenador_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_coordenador_id_seq OWNED BY ag_coordenador.id;


--
-- TOC entry 3653 (class 2604 OID 182628)
-- Dependencies: 781 782 782
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_coordenador ALTER COLUMN id SET DEFAULT nextval('ag_coordenador_id_seq'::regclass);


--
-- TOC entry 3758 (class 0 OID 182625)
-- Dependencies: 782 3759
-- Data for Name: ag_coordenador; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_coordenador (id, curso, email) FROM stdin;
\.


--
-- TOC entry 3764 (class 0 OID 0)
-- Dependencies: 781
-- Name: ag_coordenador_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_coordenador_id_seq', 4, true);


--
-- TOC entry 3655 (class 2606 OID 182630)
-- Dependencies: 782 782 3760
-- Name: ag_coordenador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_coordenador
    ADD CONSTRAINT ag_coordenador_pkey PRIMARY KEY (id);


-- Completed on 2014-04-24 18:06:25 BRT

--
-- PostgreSQL database dump complete
--

