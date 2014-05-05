--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-04-24 18:07:38 BRT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 813 (class 1259 OID 230728)
-- Dependencies: 3653 3654 7
-- Name: ag_mensagens; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ag_mensagens (
    id bigint NOT NULL,
    mensagem character varying(10000) DEFAULT NULL::character varying,
    destinatario character varying(255) DEFAULT NULL::character varying,
    f_envio integer,
    agente bigint,
    action bigint,
    tipo text,
    id_professor integer
);


ALTER TABLE public.ag_mensagens OWNER TO postgres;

--
-- TOC entry 3759 (class 0 OID 230728)
-- Dependencies: 813 3760
-- Data for Name: ag_mensagens; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ag_mensagens (id, mensagem, destinatario, f_envio, agente, action, tipo, id_professor) FROM stdin;
22	Prezado <nome do tutor> Na disciplina  <nome da disciplina> existem os seguintes fóruns:  <nome do fórum> Onde o(s) aluno(s): <nome do aluno> não possuem nenhuma participação nos respectivos fóruns ou não receberam suas devidas notas referentes a postagens realizadas. Seria interessante que você incentivasse essas alunos especificos a participarem dos fóruns, mesmo que tais não sejam avaliativos.	alan	\N	1	1	\N	\N
24	Prezado <nome do tutor>. Na disciplina <nonome da disciplina>, existem os seguintes fóruns: <nome do fórum>. Onde sua participação não foi encontrada. É necessário que você participe para incentivar os alunos desse curso \\\\n\\"	alan	\N	1	5	\N	\N
25	Prezado <nome do tutor>. Na disciplina <nome da disciplina>, existem os seguintes fóruns: <nome do fórum> onde acorreram publicações pelos alunos, porém não foi constatado sua participação nos últimos dias Procure analisar as publicações, realizando comentários, sugestões ou criticas.	alan	\N	1	4	\N	\N
26	Prezado  <nome do tutor> em <data> foi criado uma nova disciplina chamada <nome da disciplina>. Procure ler o conteúdo dessa disciplina	alan	\N	1	1	\N	\N
27	Prezado <nome do aluno>. Na disciplina <nome da disciplina>, existem os seguintes fóruns: <nome do fórum> onde  não foram encontradas publicações dos alunos nos últimos dias. Motive os alunos para que continuarem participando desses fóruns, pois são avaliativos! 	alan	\N	1	3	\N	\N
28	Tutor,  \\nAbaixo os alunos que ainda não participaram das atividades que estão encerrando na disciplina <nome da disciplina> \\nAluno: <nomnome do aluno>  \\nAtividades:<nomwnome da atividade>	alan	\N	1	30	\N	\N
29	Prezado <nome do aluno>, Já fazem duas semanas que a atividade <nome da atividade> no curso <nome do curso> teve seu periodo de avaliação encerrado, porém as notas dos alunos não foram postadas. Procure postar as notas dos alunos o quanto antes.	alan	\N	8	19	\N	\N
30	Prezado Aluno,No curso <nome do curso>, o seu rendimento está baixo. Procure o seu grupo para apoio ou, caso ainda não tenha um, busque apoio com seus colegas de curso.	alan	\N	6	16	\N	\N
31	Prezado Aluno, as atividades da disciplina <nome do curso> são: - <nome da atividade>.	alan	\N	7	17	\N	\N
32	Prezado Aluno, Revise os principais conceitos da(s) disciplina(s) <pre-requisito> para que seu aprendizado em  <nome do curso> seja maximizado.	alan	\N	7	18	\N	\N
33	Prezado Aluno, Estude mais a disciplina de <nome da disciplina>, pois seu rendimento está baixo. há uma disciplina fundamental, sendo pre-requisito das seguintes disciplinas: <nome da disciplina>.	alan	\N	7	17	\N	\N
34	Prezado <nome do aluno> em  <data> foi criado uma nova disciplina\\n chamada  <nome da disciplina> . Procure participar mais vezes dos fóruns pois eles são uma importante ferramenta de aprendizagem cooperativa e em alguns casos serão usados como avaliação.	alan	\N	7	32	\N	\N
35	Prezado, em <data> estará disponível um novo questionário chamado <nome da atividade> . Programe-se para essa avaliação.	alan	\N	7	17	\N	\N
41	Prezado <nome do aluno> em <data> foi criado uma nova disciplina chamada<nome da disciplina> Procure ler o conteúdo dessa disciplina. 	alan	\N	4	14	\N	\N
42	Prezado <nome do aluno> com a aproximação de uma atividade avaliativa foi marcado um chat na seguinte data e hora <data do chat> \\\\n Aproveite para tirar possíveis dúvidas com o tutor e/ou seus colegas.	alan	\N	4	12	\N	\N
43	Olá <nome do aluno>, esteja sempre atento ao calendário, pois ele informa todos os eventos e prazos do curso Para saber o que vai acontecer sobre uma determinada data, basta passar o mouse sobre o dia desejado. Você também pode cadastrar eventos de acordo com o ritmo de seus estudos clicando em NOVO EVENTO.	alan	\N	2	6	\N	\N
52	Olá <nome do aluno>, caso esteja com alguma dúvida em algum dos conteúdo,  bastar clicar sobre o nome do curso que está matriculado e procurar algum fórum,  todos os cursos por padrão possuem uma atividade chamada Fórum de noticias.  Você tanto pode criar um tópico ou participar de um já existente.\\nEm um fórum os alunos podem interagir com tutores e professores com o objetivo de compatilhar conhecimentos tirando dúvidas dos conteudos ou postando notícias ligadas ao assunto em discussão.  Procure participar dos fóruns dos cursos em que está matrculado, pois alguns são avaliativos.	alan	\N	2	8	\N	\N
53	Olá <nome do aluno> para saber quem mais estão matriculado no mesmo curso que você, ou mesmos quem seja o responsável pelo andamento do curso como tutores e professares, basta ir no menu de navegação no canto direito da tela clique sobre o nome do curso depois clique em PARTICIPANTES,você encontrará uma lista com todos os integrantes do curso. A partir disso você poderá entrar em contato com qualquer um clicando em ENVIAR MMENSAGEM.	alan	\N	2	9	\N	\N
54	Olá <nome do aluno> você pode fazer alterações de seu perfil clicando no menu CONFIGURAÇÕES. Nesse menu é possível alterar sua senha, informações de contato, informações pessoais, etc. 	alan	\N	2	7	\N	\N
55	Olá <nome do aluno>, para visualizar sua situação em relação a notas  dentro do menu COMNFIGURAÇÕES de um determinado curso e na aba ADMINISTRAÇÃO DO CURSO e por fim clicando em NOTAS é possível ver as notas das atividades já realizadas.	alan	\N	2	10	\N	\N
56	Prezado <nome do aluno>. Na disciplina <nome da disciplina> há algumas atividades pendentes, são elas: <nome da atividade - data encerramento>.	alan	\N	4	15	\N	\N
\.


--
-- TOC entry 3656 (class 2606 OID 241405)
-- Dependencies: 813 813 3761
-- Name: pk_ag_mensagens; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ag_mensagens
    ADD CONSTRAINT pk_ag_mensagens PRIMARY KEY (id);


--
-- TOC entry 3657 (class 2606 OID 240180)
-- Dependencies: 805 813 3761
-- Name: fk1b408eee29fb0db2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ag_mensagens
    ADD CONSTRAINT fk1b408eee29fb0db2 FOREIGN KEY (f_envio) REFERENCES ag_formas_de_envio(id);


-- Completed on 2014-04-24 18:07:38 BRT

--
-- PostgreSQL database dump complete
--

