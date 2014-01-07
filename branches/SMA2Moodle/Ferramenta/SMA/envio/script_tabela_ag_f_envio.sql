--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.9
-- Dumped by pg_dump version 9.1.9
-- Started on 2013-11-30 00:54:14

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 163 (class 3079 OID 11639)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1860 (class 0 OID 0)
-- Dependencies: 163
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 161 (class 1259 OID 16527)
-- Dependencies: 5
-- Name: id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.id OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 162 (class 1259 OID 16529)
-- Dependencies: 1848 5
-- Name: ag_f_envio; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_f_envio (
    id integer DEFAULT nextval('id'::regclass) NOT NULL,
    forma character varying(80)
);


ALTER TABLE public.ag_f_envio OWNER TO postgres;

--
-- TOC entry 1852 (class 0 OID 16529)
-- Dependencies: 162 1853
-- Data for Name: ag_f_envio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_f_envio (id, forma) FROM stdin;
\.


--
-- TOC entry 1861 (class 0 OID 0)
-- Dependencies: 161
-- Name: id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('id', 1, false);


--
-- TOC entry 1850 (class 2606 OID 16534)
-- Dependencies: 162 162 1854
-- Name: ag_f_envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_f_envio
    ADD CONSTRAINT ag_f_envio_pkey PRIMARY KEY (id);


--
-- TOC entry 1859 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-11-30 00:54:15

--
-- PostgreSQL database dump complete
--

