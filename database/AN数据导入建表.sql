-- Table: facility_an

-- DROP TABLE facility_an;

CREATE TABLE facility_an
(
  gid serial NOT NULL,
  fa numeric,
  fa_id numeric,
  name_chn character varying(160),
  mesh character varying(10),
  poi_id numeric,
  fa_type character varying(4),
  disp_class integer,
  fa_flag integer,
  area_flag integer,
  poi_guid character varying(32),
  "precision" character varying(1),
  sources integer,
  updatetime character varying(8),
  proname character varying(64),
  geom geometry(MultiPolygon),
  data_version character varying(10),
  CONSTRAINT facility_an_pkey PRIMARY KEY (gid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE facility_an
  OWNER TO postgres;
