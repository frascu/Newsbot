--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.10
-- Dumped by pg_dump version 9.4.10
-- Started on 2016-11-26 18:32:29

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE newsbotdb;
--
-- TOC entry 1995 (class 1262 OID 16393)
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
-- TOC entry 1998 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

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
-- TOC entry 1881 (class 2606 OID 16419)
-- Name: user_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_telegram
    ADD CONSTRAINT user_id_pk PRIMARY KEY (user_id);


--
-- TOC entry 1997 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-11-26 18:32:29

--
-- PostgreSQL database dump complete
--

