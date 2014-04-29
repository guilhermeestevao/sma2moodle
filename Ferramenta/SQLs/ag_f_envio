--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:06:35 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 809 (class 1259 OID 205234)
-- Dependencies: 3653 7
-- Name: ag_f_envio; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_f_envio (
    id integer DEFAULT nextval('id'::regclass) NOT NULL,
    forma character varying(80)
);


ALTER TABLE public.ag_f_envio OWNER TO postgres;

--
-- TOC entry 3757 (class 0 OID 205234)
-- Dependencies: 809 3758
-- Data for Name: ag_f_envio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_f_envio (id, forma) FROM stdin;
1	sms
2	email
3	moodle
\.


--
-- TOC entry 3655 (class 2606 OID 205239)
-- Dependencies: 809 809 3759
-- Name: ag_f_envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_f_envio
    ADD CONSTRAINT ag_f_envio_pkey PRIMARY KEY (id);


-- Completed on 2014-04-24 18:06:36 BRT

--
-- PostgreSQL database dump complete
--

