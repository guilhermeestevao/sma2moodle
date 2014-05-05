--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:06:15 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 777 (class 1259 OID 182504)
-- Dependencies: 7
-- Name: ag_assunto; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_assunto (
    nome character varying,
    id integer NOT NULL
);


ALTER TABLE public.ag_assunto OWNER TO postgres;

--
-- TOC entry 778 (class 1259 OID 182513)
-- Dependencies: 777 7
-- Name: ag_assunto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_assunto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_assunto_id_seq OWNER TO postgres;

--
-- TOC entry 3763 (class 0 OID 0)
-- Dependencies: 778
-- Name: ag_assunto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_assunto_id_seq OWNED BY ag_assunto.id;


--
-- TOC entry 3653 (class 2604 OID 182515)
-- Dependencies: 778 777
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_assunto ALTER COLUMN id SET DEFAULT nextval('ag_assunto_id_seq'::regclass);


--
-- TOC entry 3757 (class 0 OID 182504)
-- Dependencies: 777 3759
-- Data for Name: ag_assunto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_assunto (nome, id) FROM stdin;
POO	11
assembly	13
\.


--
-- TOC entry 3764 (class 0 OID 0)
-- Dependencies: 778
-- Name: ag_assunto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_assunto_id_seq', 13, true);


--
-- TOC entry 3655 (class 2606 OID 182553)
-- Dependencies: 777 777 3760
-- Name: ag_assunto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_assunto
    ADD CONSTRAINT ag_assunto_pkey PRIMARY KEY (id);


-- Completed on 2014-04-24 18:06:15 BRT

--
-- PostgreSQL database dump complete
--

