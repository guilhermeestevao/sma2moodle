--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-15 22:28:52 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 805 (class 1259 OID 196560)
-- Dependencies: 3654 7
-- Name: ag_formas_de_envio; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_formas_de_envio (
    id integer NOT NULL,
    f_envio character varying(255) DEFAULT NULL::character varying
);


ALTER TABLE public.ag_formas_de_envio OWNER TO postgres;

--
-- TOC entry 804 (class 1259 OID 196558)
-- Dependencies: 805 7
-- Name: ag_formas_de_envio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_formas_de_envio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_formas_de_envio_id_seq OWNER TO postgres;

--
-- TOC entry 3764 (class 0 OID 0)
-- Dependencies: 804
-- Name: ag_formas_de_envio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_formas_de_envio_id_seq OWNED BY ag_formas_de_envio.id;


--
-- TOC entry 3653 (class 2604 OID 196563)
-- Dependencies: 804 805 805
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_formas_de_envio ALTER COLUMN id SET DEFAULT nextval('ag_formas_de_envio_id_seq'::regclass);


--
-- TOC entry 3759 (class 0 OID 196560)
-- Dependencies: 805 3760
-- Data for Name: ag_formas_de_envio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_formas_de_envio (id, f_envio) FROM stdin;
2	E-Mail
3	Moodle
1	SMS
4	Twitter
\.


--
-- TOC entry 3765 (class 0 OID 0)
-- Dependencies: 804
-- Name: ag_formas_de_envio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_formas_de_envio_id_seq', 1, false);


--
-- TOC entry 3656 (class 2606 OID 196566)
-- Dependencies: 805 805 3761
-- Name: ag_formas_de_envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_formas_de_envio
    ADD CONSTRAINT ag_formas_de_envio_pkey PRIMARY KEY (id);


-- Completed on 2014-04-15 22:28:52 BRT

--
-- PostgreSQL database dump complete
--

