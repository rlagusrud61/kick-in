drop schema if exists idb_kick_in_team_26;
create schema if not exists idb_kick_in_team_26;
SET search_path = "idb_kick_in_team_26";

CREATE TABLE Events
(
    eventId      bigint NOT NULL,
    name         varchar(255),
    description  varchar(255),
    location     varchar(255),
    createdBy    varchar(255),
    lastEditedBy varchar(255),
    PRIMARY KEY (eventId)
);

CREATE TABLE Maps
(
    mapId        bigint NOT NULL,
    name         varchar(255),
    description  varchar(255),
    createdBy    varchar(255),
    lastEditedBy varchar(255),
    PRIMARY KEY (mapId)
);

CREATE TABLE TypeOfResource
(
    resourceId  bigint NOT NULL,
    name        varchar(255),
    description varchar(255),
    PRIMARY KEY (resourceId)
);

CREATE TABLE Materials
(
    resourceId bigint NOT NULL,
    image      varchar(255),
    PRIMARY KEY (resourceId),
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource(resourceId)
);

CREATE TABLE Drawing
(
    resourceId bigint NOT NULL,
    image      varchar(255),
    PRIMARY KEY (resourceId),
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource (resourceId)
);

CREATE TABLE EventMap
(
    eventId bigint NOT NULL,
    mapId   bigint NOT NULL,
    PRIMARY KEY (eventId, mapId),
    FOREIGN KEY (eventId) REFERENCES Events (eventId),
    FOREIGN KEY (mapId) REFERENCES Maps (mapID)
);

CREATE TABLE MapObjects
(
    objectId   bigint NOT NULL,
    mapId      bigint,
    resourceId bigint,
    latLangs   varchar(255),
    PRIMARY KEY (objectId),
    FOREIGN KEY (mapId) REFERENCES Maps(mapId),
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource(resourceId)
);