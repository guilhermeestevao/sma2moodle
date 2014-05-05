--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:08:08 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 785 (class 1259 OID 182637)
-- Dependencies: 7
-- Name: ag_tarefa_material; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_tarefa_material (
    id_tarefa bigint NOT NULL,
    id_material integer NOT NULL
);


ALTER TABLE public.ag_tarefa_material OWNER TO postgres;

--
-- TOC entry 3757 (class 0 OID 182637)
-- Dependencies: 785 3758
-- Data for Name: ag_tarefa_material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_tarefa_material (id_tarefa, id_material) FROM stdin;
18	39
17	40
41	41
18	42
\.


--
-- TOC entry 3653 (class 1259 OID 196644)
-- Dependencies: 785 3759
-- Name: fki_id_tarefa; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_id_tarefa ON ag_tarefa_material USING btree (id_tarefa);


--
-- TOC entry 3655 (class 2606 OID 196634)
-- Dependencies: 779 785 3759
-- Name: fk2ad0b0b2255072f3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_tarefa_material
    ADD CONSTRAINT fk2ad0b0b2255072f3 FOREIGN KEY (id_material) REFERENCES ag_material(id);


--
-- TOC entry 3654 (class 2606 OID 196645)
-- Dependencies: 162 785 3759
-- Name: fktarefa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_tarefa_material
    ADD CONSTRAINT fktarefa FOREIGN KEY (id_tarefa) REFERENCES mdl_assign(id);


-- Completed on 2014-04-24 18:08:08 BRT

--
-- PostgreSQL database dump complete
--

