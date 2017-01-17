--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.10
-- Dumped by pg_dump version 9.4.10
-- Started on 2017-01-17 20:54:35

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 2014 (class 1262 OID 16393)
-- Name: newsbotdb; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE newsbotdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Italian_Italy.1252' LC_CTYPE = 'Italian_Italy.1252';


ALTER DATABASE newsbotdb OWNER TO postgres;

\connect newsbotdb

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2017 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 176 (class 1259 OID 16461)
-- Name: group_telegram; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE group_telegram (
    group_id bigint NOT NULL,
    registered character varying
);


ALTER TABLE group_telegram OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 16458)
-- Name: news_pk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE news_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE news_pk_seq OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 16438)
-- Name: news; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE news (
    id bigint DEFAULT nextval('news_pk_seq'::regclass) NOT NULL,
    link character varying(255),
    title character varying(255),
    pubblication_date date,
    creation_date date,
    sent character varying
);


ALTER TABLE news OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16413)
-- Name: user_telegram; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE user_telegram (
    user_id bigint NOT NULL,
    user_name character varying(150),
    first_name character varying(150),
    last_name character varying,
    registered character varying
);


ALTER TABLE user_telegram OWNER TO postgres;

--
-- TOC entry 1896 (class 2606 OID 16449)
-- Name: NEWS_PK; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY news
    ADD CONSTRAINT "NEWS_PK" PRIMARY KEY (id);


--
-- TOC entry 1900 (class 2606 OID 16465)
-- Name: group_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY group_telegram
    ADD CONSTRAINT group_id_pk PRIMARY KEY (group_id);


--
-- TOC entry 1898 (class 2606 OID 16447)
-- Name: link_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY news
    ADD CONSTRAINT link_unique UNIQUE (link);


--
-- TOC entry 1894 (class 2606 OID 16419)
-- Name: user_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_telegram
    ADD CONSTRAINT user_id_pk PRIMARY KEY (user_id);


--
-- TOC entry 2016 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-01-17 20:54:35

--
-- PostgreSQL database dump complete
--

