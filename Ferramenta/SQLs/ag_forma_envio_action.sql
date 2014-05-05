--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:06:46 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 812 (class 1259 OID 222045)
-- Dependencies: 7
-- Name: ag_forma_envio_action; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_forma_envio_action (
    id integer NOT NULL,
    forma integer,
    action integer
);


ALTER TABLE public.ag_forma_envio_action OWNER TO postgres;

--
-- TOC entry 811 (class 1259 OID 222043)
-- Dependencies: 812 7
-- Name: ag_forma_envio_action_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ag_forma_envio_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ag_forma_envio_action_id_seq OWNER TO postgres;

--
-- TOC entry 3766 (class 0 OID 0)
-- Dependencies: 811
-- Name: ag_forma_envio_action_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ag_forma_envio_action_id_seq OWNED BY ag_forma_envio_action.id;


--
-- TOC entry 3653 (class 2604 OID 222048)
-- Dependencies: 811 812 812
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_forma_envio_action ALTER COLUMN id SET DEFAULT nextval('ag_forma_envio_action_id_seq'::regclass);


--
-- TOC entry 3761 (class 0 OID 222045)
-- Dependencies: 812 3762
-- Data for Name: ag_forma_envio_action; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_forma_envio_action (id, forma, action) FROM stdin;
91	2	6
92	3	6
93	1	6
94	2	8
95	3	8
96	1	8
97	2	9
98	3	9
99	1	9
100	2	7
101	3	7
102	1	7
103	2	10
104	3	10
105	1	10
107	3	15
\.


--
-- TOC entry 3767 (class 0 OID 0)
-- Dependencies: 811
-- Name: ag_forma_envio_action_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ag_forma_envio_action_id_seq', 107, true);


--
-- TOC entry 3656 (class 2606 OID 222050)
-- Dependencies: 812 812 3763
-- Name: pri_2; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_forma_envio_action
    ADD CONSTRAINT pri_2 PRIMARY KEY (id);


--
-- TOC entry 3654 (class 1259 OID 222066)
-- Dependencies: 812 3763
-- Name: fki_for_actions; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_for_actions ON ag_forma_envio_action USING btree (action);


--
-- TOC entry 3657 (class 2606 OID 222061)
-- Dependencies: 775 812 3763
-- Name: for_actions; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_forma_envio_action
    ADD CONSTRAINT for_actions FOREIGN KEY (action) REFERENCES ag_actions(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3658 (class 2606 OID 222056)
-- Dependencies: 805 812 3763
-- Name: for_envio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_forma_envio_action
    ADD CONSTRAINT for_envio FOREIGN KEY (forma) REFERENCES ag_formas_de_envio(id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2014-04-24 18:06:46 BRT

--
-- PostgreSQL database dump complete
--

