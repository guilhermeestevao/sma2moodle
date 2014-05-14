--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:05:07 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 775 (class 1259 OID 165043)
-- Dependencies: 7
-- Name: ag_actions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_actions (
    id integer NOT NULL,
    nome character varying,
    agente bigint,
    descricao text
);


ALTER TABLE public.ag_actions OWNER TO postgres;

--
-- TOC entry 3758 (class 0 OID 165043)
-- Dependencies: 775 3759
-- Data for Name: ag_actions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_actions (id, nome, agente, descricao) FROM stdin;
1	alunosParticipantes	1	Informar ao(s) tutor(es) de um curso quais alunos ainda não participaram de algum fórum 
2	informarNovaDisciplina	1	Informar ao(s) tutor(es) quando uma nova disciplina é criada  
3	mantemForumAtivo	1	Notificarao(s) tutor(es) quando um determinado fórum atinge um periodo de tempo muito longo sem publicações de alunos  
4	mantemTutorAtivo	1	Notificar o(s) tutor(es) quando ele passar mutio tempo sem fazer postagems em algum fórum 
5	tutoresParticipantes	1	Notifica o(s) tutor(es ) quando ele ele não tiver feito nenhuma publicação em umdeterminado  fórum 
12	criaChat	4	Ao se aproxima da data de uma avaliação um chat é criado automaticamente para que alunos possam tirar suas dúvidas junto a professores e tutores
13	informarAndamento	4	Informar o andamento dos alunos de uma disciplina mostrando atividades já realizadas e suas respectivas notas e também as atividades em aberto  
14	mostraNovaDisciplina	4	Informar aos alunos quando uma nova disciplina for criada no ambiente
15	pesquisarData	4	Informar os prazos das atividades do disciplina
16	formarGrupos	6	Informar ao aluno quando automaticamente  ele foi inserido em algum grupo 
17	informarAtividadesDisciplina	7	Informa as atividades de uma disciplina
18	informarPreRequisitos	7	Informa os pré-requisitos de uma dicsiplina e assuntos que o aluno deve estudar (na dicsiplina atual) para atingir um bom desempenho em disciplinas futuras 
19	informarNotasAtrasadas	8	Notificar o professor quando ele demorar a postar as notas de alguma atividade/avaiçaão
6	exibirDicasCalendario	2	Dar dicas de como o aluno deve utilizar e tirar melhor proveito do calendário disponível no Moodle  
7	exibirDicasConfiguracoes	2	Dar dica de como o aluno pode acessar e configurar seu perfil no moodle
8	exibirDicasForuns	2	Dar dicas de como o aluno pode participar de fóruns 
9	exibirDicasParticipantes	2	Dar dicas de como o aluno pode encontrar virtualmente outrar pessoas da disciplina em que ele participa
10	exibirDicasRelatorioDeNotas	2	Sem descrição
11	 	2	Sem descrição
21	InformarDataModificada	4	\N
30	InformaAtividadesEncerrando	1	\N
31	OrientarAlunoNotaBaixaDisciplina	7	\N
32	IncentivaParticipação	7	\N
33	ComunicaAvaliação	7	\N
\.


--
-- TOC entry 3655 (class 2606 OID 213464)
-- Dependencies: 775 775 3760
-- Name: primary; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_actions
    ADD CONSTRAINT "primary" PRIMARY KEY (id);


--
-- TOC entry 3653 (class 1259 OID 213476)
-- Dependencies: 775 3760
-- Name: fki_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_key ON ag_actions USING btree (agente);


--
-- TOC entry 3656 (class 2606 OID 213471)
-- Dependencies: 774 775 3760
-- Name: key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_actions
    ADD CONSTRAINT key FOREIGN KEY (agente) REFERENCES ag_agentes(id);


-- Completed on 2014-04-24 18:05:08 BRT

--
-- PostgreSQL database dump complete
--
