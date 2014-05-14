--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:07:07 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 783 (class 1259 OID 182631)
-- Dependencies: 7
-- Name: ag_licao_material; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_licao_material (
    id_licao numeric(19,2) NOT NULL,
    id_material integer NOT NULL
);


ALTER TABLE public.ag_licao_material OWNER TO postgres;

--
-- TOC entry 3755 (class 0 OID 182631)
-- Dependencies: 783 3756
-- Data for Name: ag_licao_material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_licao_material (id_licao, id_material) FROM stdin;
\.


--
-- TOC entry 3653 (class 2606 OID 182640)
-- Dependencies: 779 783 3757
-- Name: fk2f33dd0b255072f3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_licao_material
    ADD CONSTRAINT fk2f33dd0b255072f3 FOREIGN KEY (id_material) REFERENCES ag_material(id);


-- Completed on 2014-04-24 18:07:08 BRT

--
-- PostgreSQL database dump complete
--
