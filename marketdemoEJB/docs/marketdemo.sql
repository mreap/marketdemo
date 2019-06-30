-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.1
-- PostgreSQL version: 10.0
-- Project Site: pgmodeler.io
-- Model Author: ---


-- Database creation must be done outside a multicommand file.
-- These commands were put in this file only as a convenience.
-- -- object: marketdemo | type: DATABASE --
-- -- DROP DATABASE IF EXISTS marketdemo;
-- CREATE DATABASE marketdemo
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'es_EC.UTF-8'
-- 	LC_CTYPE = 'es_EC.UTF-8'
-- 	TABLESPACE = pg_default
-- 	OWNER = postgres;
-- -- ddl-end --
-- 

-- object: public.cliente | type: TABLE --
-- DROP TABLE IF EXISTS public.cliente CASCADE;
CREATE TABLE public.cliente(
	cedula_cliente character varying(10) NOT NULL,
	nombres character varying(100) NOT NULL,
	apellidos character varying(100) NOT NULL,
	direccion character varying(100),
	clave character varying(20),
	CONSTRAINT cliente_pk PRIMARY KEY (cedula_cliente)

);
-- ddl-end --
ALTER TABLE public.cliente OWNER TO postgres;
-- ddl-end --

-- object: public.estado_pedido | type: TABLE --
-- DROP TABLE IF EXISTS public.estado_pedido CASCADE;
CREATE TABLE public.estado_pedido(
	id_estado_pedido character varying(2) NOT NULL,
	descripcion_estado character varying(50) NOT NULL,
	CONSTRAINT pk_estados_pedido PRIMARY KEY (id_estado_pedido)

);
-- ddl-end --
COMMENT ON TABLE public.estado_pedido IS 'estados que puede tener un pedido de compra de un cliente';
-- ddl-end --
ALTER TABLE public.estado_pedido OWNER TO postgres;
-- ddl-end --

-- object: public.bitacora | type: TABLE --
-- DROP TABLE IF EXISTS public.bitacora CASCADE;
CREATE TABLE public.bitacora(
	codigo_evento integer NOT NULL,
	fecha_evento date NOT NULL,
	codigo_usuario character varying(20) NOT NULL,
	metodo character varying(100) NOT NULL,
	descripcion character varying(200),
	direccion_ip character varying(20),
	CONSTRAINT bitacora_pk PRIMARY KEY (codigo_evento)

);
-- ddl-end --
COMMENT ON COLUMN public.bitacora.metodo IS 'Metodo que generó el evento';
-- ddl-end --
COMMENT ON COLUMN public.bitacora.descripcion IS 'informacion detalle del evento';
-- ddl-end --
COMMENT ON COLUMN public.bitacora.direccion_ip IS 'direccion IP del usuario';
-- ddl-end --
ALTER TABLE public.bitacora OWNER TO postgres;
-- ddl-end --

-- object: public.factura_cab | type: TABLE --
-- DROP TABLE IF EXISTS public.factura_cab CASCADE;
CREATE TABLE public.factura_cab(
	numero_factura character varying(17) NOT NULL,
	codigo_usuario character varying(20) NOT NULL,
	cedula_cliente character varying(10) NOT NULL,
	fecha_emision date NOT NULL,
	subtotal numeric(12,2) NOT NULL,
	base_cero numeric(12,2) NOT NULL,
	valor_iva numeric(12,2) NOT NULL,
	total numeric(12,2) NOT NULL,
	CONSTRAINT facturas_cab_pk PRIMARY KEY (numero_factura)

);
-- ddl-end --
COMMENT ON COLUMN public.factura_cab.numero_factura IS 'Numero de factura en formato eee-ppp-nnnnnnnnn (establecimiento, punto de emision, numero secuencia)';
-- ddl-end --
COMMENT ON COLUMN public.factura_cab.codigo_usuario IS 'codigo del usuario que crea la factura';
-- ddl-end --
ALTER TABLE public.factura_cab OWNER TO postgres;
-- ddl-end --

-- object: public.factura_det | type: TABLE --
-- DROP TABLE IF EXISTS public.factura_det CASCADE;
CREATE TABLE public.factura_det(
	numero_factura_det integer NOT NULL,
	numero_factura character varying(17) NOT NULL,
	codigo_producto integer NOT NULL,
	cantidad integer NOT NULL,
	precio_unitario_venta numeric(12,2) NOT NULL,
	CONSTRAINT factura_det_pk PRIMARY KEY (numero_factura_det)

);
-- ddl-end --
ALTER TABLE public.factura_det OWNER TO postgres;
-- ddl-end --

-- object: public.parametro | type: TABLE --
-- DROP TABLE IF EXISTS public.parametro CASCADE;
CREATE TABLE public.parametro(
	nombre_parametro character varying(50) NOT NULL,
	valor_parametro character varying(250) NOT NULL,
	CONSTRAINT parametro_pk PRIMARY KEY (nombre_parametro)

);
-- ddl-end --
COMMENT ON TABLE public.parametro IS 'tabla con valores de parametros generales del sistema';
-- ddl-end --
ALTER TABLE public.parametro OWNER TO postgres;
-- ddl-end --

-- object: public.pedido_cab | type: TABLE --
-- DROP TABLE IF EXISTS public.pedido_cab CASCADE;
CREATE TABLE public.pedido_cab(
	numero_pedido integer NOT NULL,
	fecha_pedido date NOT NULL,
	cedula_cliente character varying(10) NOT NULL,
	id_estado_pedido character varying(2) NOT NULL,
	subtotal numeric(12,2) NOT NULL,
	CONSTRAINT pk_pedido_cab PRIMARY KEY (numero_pedido)

);
-- ddl-end --
COMMENT ON TABLE public.pedido_cab IS 'Tabla maestra de pedidos de compra de clientes.';
-- ddl-end --
COMMENT ON COLUMN public.pedido_cab.subtotal IS 'valor subtotal del pedido de compra sin impuestos.';
-- ddl-end --
ALTER TABLE public.pedido_cab OWNER TO postgres;
-- ddl-end --

-- object: public.pedido_det | type: TABLE --
-- DROP TABLE IF EXISTS public.pedido_det CASCADE;
CREATE TABLE public.pedido_det(
	numero_pedido_det integer NOT NULL,
	numero_pedido integer NOT NULL,
	codigo_producto integer NOT NULL,
	cantidad integer NOT NULL,
	precio_unitario_venta numeric(12,2) NOT NULL,
	CONSTRAINT pedido_det_pk PRIMARY KEY (numero_pedido_det)

);
-- ddl-end --
ALTER TABLE public.pedido_det OWNER TO postgres;
-- ddl-end --

-- object: public.producto | type: TABLE --
-- DROP TABLE IF EXISTS public.producto CASCADE;
CREATE TABLE public.producto(
	codigo_producto integer NOT NULL,
	nombre character varying(50) NOT NULL,
	descripcion character varying(100),
	precio_unitario numeric(12,2) NOT NULL,
	existencia integer NOT NULL,
	tiene_impuesto character varying(1) NOT NULL,
	CONSTRAINT producto_impuesto_chk CHECK (((tiene_impuesto)::text = ANY (ARRAY[('S'::character varying)::text, ('N'::character varying)::text]))),
	CONSTRAINT producto_pk PRIMARY KEY (codigo_producto)

);
-- ddl-end --
COMMENT ON COLUMN public.producto.existencia IS 'cantidad en existencia';
-- ddl-end --
COMMENT ON COLUMN public.producto.tiene_impuesto IS 'si tiene impuesto o no (S/N)';
-- ddl-end --
ALTER TABLE public.producto OWNER TO postgres;
-- ddl-end --

-- object: public.seq_bitacora | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_bitacora CASCADE;
CREATE SEQUENCE public.seq_bitacora
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_bitacora OWNER TO postgres;
-- ddl-end --

-- object: public.seq_pedido_cab | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_pedido_cab CASCADE;
CREATE SEQUENCE public.seq_pedido_cab
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_pedido_cab OWNER TO postgres;
-- ddl-end --

-- object: public.seq_pedido_det | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_pedido_det CASCADE;
CREATE SEQUENCE public.seq_pedido_det
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_pedido_det OWNER TO postgres;
-- ddl-end --

-- object: public.usuario | type: TABLE --
-- DROP TABLE IF EXISTS public.usuario CASCADE;
CREATE TABLE public.usuario(
	codigo_usuario character varying(20) NOT NULL,
	nombres_usuario character varying(100) NOT NULL,
	tipo_usuario character varying(2) NOT NULL,
	clave character varying(10),
	CONSTRAINT usuarios_pk PRIMARY KEY (codigo_usuario)

);
-- ddl-end --
COMMENT ON TABLE public.usuario IS 'usuarios del sistema';
-- ddl-end --
COMMENT ON COLUMN public.usuario.codigo_usuario IS 'codigo unico del usuario';
-- ddl-end --
COMMENT ON COLUMN public.usuario.nombres_usuario IS 'nombres completos del usuario';
-- ddl-end --
COMMENT ON COLUMN public.usuario.tipo_usuario IS 'tipo de usuario';
-- ddl-end --
COMMENT ON COLUMN public.usuario.clave IS 'contraseña del usuario';
-- ddl-end --
ALTER TABLE public.usuario OWNER TO postgres;
-- ddl-end --

-- object: public.seq_factura_det | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_factura_det CASCADE;
CREATE SEQUENCE public.seq_factura_det
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_factura_det OWNER TO postgres;
-- ddl-end --

-- object: usuario_bitacora_fk | type: CONSTRAINT --
-- ALTER TABLE public.bitacora DROP CONSTRAINT IF EXISTS usuario_bitacora_fk CASCADE;
ALTER TABLE public.bitacora ADD CONSTRAINT usuario_bitacora_fk FOREIGN KEY (codigo_usuario)
REFERENCES public.usuario (codigo_usuario) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: factcab_cliente_fk | type: CONSTRAINT --
-- ALTER TABLE public.factura_cab DROP CONSTRAINT IF EXISTS factcab_cliente_fk CASCADE;
ALTER TABLE public.factura_cab ADD CONSTRAINT factcab_cliente_fk FOREIGN KEY (cedula_cliente)
REFERENCES public.cliente (cedula_cliente) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: usuario_facturacab_fk | type: CONSTRAINT --
-- ALTER TABLE public.factura_cab DROP CONSTRAINT IF EXISTS usuario_facturacab_fk CASCADE;
ALTER TABLE public.factura_cab ADD CONSTRAINT usuario_facturacab_fk FOREIGN KEY (codigo_usuario)
REFERENCES public.usuario (codigo_usuario) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: factdet_factcab_fk | type: CONSTRAINT --
-- ALTER TABLE public.factura_det DROP CONSTRAINT IF EXISTS factdet_factcab_fk CASCADE;
ALTER TABLE public.factura_det ADD CONSTRAINT factdet_factcab_fk FOREIGN KEY (numero_factura)
REFERENCES public.factura_cab (numero_factura) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: factdet_producto_fk | type: CONSTRAINT --
-- ALTER TABLE public.factura_det DROP CONSTRAINT IF EXISTS factdet_producto_fk CASCADE;
ALTER TABLE public.factura_det ADD CONSTRAINT factdet_producto_fk FOREIGN KEY (codigo_producto)
REFERENCES public.producto (codigo_producto) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_pedidocab_cliente | type: CONSTRAINT --
-- ALTER TABLE public.pedido_cab DROP CONSTRAINT IF EXISTS fk_pedidocab_cliente CASCADE;
ALTER TABLE public.pedido_cab ADD CONSTRAINT fk_pedidocab_cliente FOREIGN KEY (cedula_cliente)
REFERENCES public.cliente (cedula_cliente) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_pedidocab_estado | type: CONSTRAINT --
-- ALTER TABLE public.pedido_cab DROP CONSTRAINT IF EXISTS fk_pedidocab_estado CASCADE;
ALTER TABLE public.pedido_cab ADD CONSTRAINT fk_pedidocab_estado FOREIGN KEY (id_estado_pedido)
REFERENCES public.estado_pedido (id_estado_pedido) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: pedidodet_pedidocab_fk | type: CONSTRAINT --
-- ALTER TABLE public.pedido_det DROP CONSTRAINT IF EXISTS pedidodet_pedidocab_fk CASCADE;
ALTER TABLE public.pedido_det ADD CONSTRAINT pedidodet_pedidocab_fk FOREIGN KEY (numero_pedido)
REFERENCES public.pedido_cab (numero_pedido) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: pedidodet_producto_fk | type: CONSTRAINT --
-- ALTER TABLE public.pedido_det DROP CONSTRAINT IF EXISTS pedidodet_producto_fk CASCADE;
ALTER TABLE public.pedido_det ADD CONSTRAINT pedidodet_producto_fk FOREIGN KEY (codigo_producto)
REFERENCES public.producto (codigo_producto) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


