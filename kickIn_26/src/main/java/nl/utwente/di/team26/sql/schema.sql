drop schema if exists idb_kick_in_team_26;
create schema if not exists idb_kick_in_team_26;
SET search_path = "idb_kick_in_team_26";

CREATE TABLE Events
(
    eventId      bigserial NOT NULL,
    name         text,
    description  text,
    location     text,
    createdBy    text,
    lastEditedBy text,
    PRIMARY KEY (eventId)
);

CREATE TABLE Maps
(
    mapId        bigserial NOT NULL,
    name         text,
    description  text,
    createdBy    text,
    lastEditedBy text,
    PRIMARY KEY (mapId)
);

CREATE TABLE TypeOfResource
(
    resourceId  bigserial NOT NULL,
    name        text,
    description text,
    PRIMARY KEY (resourceId)
);

CREATE TABLE Materials
(
    resourceId bigint NOT NULL,
    image      text,
    PRIMARY KEY (resourceId),
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource (resourceId) ON DELETE CASCADE
);

CREATE TABLE Drawing
(
    resourceId bigint NOT NULL,
    image      text,
    PRIMARY KEY (resourceId),
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource (resourceId) ON DELETE CASCADE
);

CREATE TABLE EventMap
(
    eventId bigint NOT NULL,
    mapId   bigint NOT NULL,
    PRIMARY KEY (eventId, mapId),
    FOREIGN KEY (eventId) REFERENCES Events (eventId) ON DELETE CASCADE,
    FOREIGN KEY (mapId) REFERENCES Maps (mapID) ON DELETE CASCADE
);

CREATE TABLE MapObjects
(
    objectId   bigserial NOT NULL,
    mapId      bigint,
    resourceId bigint,
    latLangs   text,
    PRIMARY KEY (objectId),
    FOREIGN KEY (mapId) REFERENCES Maps (mapId) ON DELETE CASCADE,
    FOREIGN KEY (resourceId) REFERENCES TypeOfResource (resourceId) ON DELETE CASCADE
);

create table users
(
    userid bigserial,
    email text unique not null,
    password text not null,
    clarificationLevel int not null,
    primary key (userid)
);

create table session
(
    tokenId bigserial,
    token text unique not null ,
    userid bigint unique not null ,
    foreign key (userid) references users (userid),
    primary key (tokenId)
);