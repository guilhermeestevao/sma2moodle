--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:05:41 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 807 (class 1259 OID 196683)
-- Dependencies: 7
-- Name: ag_agente_curso; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_agente_curso (
    id integer NOT NULL,
    id_curso bigint,
    id_agente bigint
);


ALTER TABLE public.ag_agente_curso OWNER TO postgres;

--
-- TOC entry 806 (class 1259 OID 196681)
-- Dependencies: 807 7
-- Name: ag_agente_curso_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_agente_curso_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_agente_curso_id_seq OWNER TO postgres;

--
-- TOC entry 3765 (class 0 OID 0)
-- Dependencies: 806
-- Name: ag_agente_curso_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_agente_curso_id_seq OWNED BY ag_agente_curso.id;


--
-- TOC entry 3653 (class 2604 OID 196686)
-- Dependencies: 806 807 807
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_agente_curso ALTER COLUMN id SET DEFAULT nextval('ag_agente_curso_id_seq'::regclass);


--
-- TOC entry 3760 (class 0 OID 196683)
-- Dependencies: 807 3761
-- Data for Name: ag_agente_curso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_agente_curso (id, id_curso, id_agente) FROM stdin;
2024	203	1
2025	203	2
2026	203	4
\.


--
-- TOC entry 3766 (class 0 OID 0)
-- Dependencies: 806
-- Name: ag_agente_curso_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_agente_curso_id_seq', 2026, true);


--
-- TOC entry 3656 (class 2606 OID 196688)
-- Dependencies: 807 807 3762
-- Name: pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_agente_curso
    ADD CONSTRAINT pk PRIMARY KEY (id);


--
-- TOC entry 3654 (class 1259 OID 196696)
-- Dependencies: 807 3762
-- Name: fki_fk2; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_fk2 ON ag_agente_curso USING btree (id_curso);


--
-- TOC entry 3657 (class 2606 OID 196691)
-- Dependencies: 263 807 3762
-- Name: fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_agente_curso
    ADD CONSTRAINT fk2 FOREIGN KEY (id_curso) REFERENCES mdl_course(id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2014-04-24 18:05:42 BRT

--
-- PostgreSQL database dump complete
--

