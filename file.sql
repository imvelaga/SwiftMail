-- Drop table

-- DROP TABLE public.mailcontent;

CREATE TABLE public.mailcontent (
	subject varchar NULL,
	message varchar NULL,
	email varchar NULL,
	"serial" serial NULL,
	isactive int4 NULL,
	crts varchar NULL
);
