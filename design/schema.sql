CREATE SCHEMA IF NOT EXISTS idb_kick_in_team_26

    CREATE TABLE Map (map_id bigserial PRIMARY KEY,
                        event_id int NOT NULL,
                        name varchar(255) NOT NULL ,
                        description text,
                        last_edited_by varchar(255));

    CREATE TABLE Event (event_id bigserial PRIMARY KEY,
                        name varchar(255) NOT NULL ,
                        location varchar(255) NOT NULL ,
                        description text,
                        created_by varchar(255) NOT NULL ,
                        last_edited_by varchar(255) NOT NULL);

    CREATE TABLE EventMap (map_id int NOT NULL,
                           event_id int NOT NULL,
                           PRIMARY KEY (map_id, event_id),
                           FOREIGN KEY (map_id) REFERENCES Map (map_id),
                           FOREIGN KEY (event_id) REFERENCES Event (event_id));

    CREATE TABLE TypeOfResource (resource_id bigserial PRIMARY KEY,
                                 name varchar(255),
                                 description text);

    CREATE TABLE Resources (object_id bigserial PRIMARY KEY,
                            resource_id int NOT NULL,
                            map_id int NOT NULL,
                            lat_langs text NOT NULL,
                            FOREIGN KEY (map_id) REFERENCES Map (map_id),
                            FOREIGN KEY (resource_id) REFERENCES TypeOfResource (resource_id));

    CREATE TABLE StaticResource (resource_id int PRIMARY KEY,
                                 picture text,
                                 FOREIGN KEY (resource_id) REFERENCES TypeOfResource (resource_id));

    CREATE TABLE FreeDraw (free_draw_id bigserial PRIMARY KEY,
                            resource_id int NOT NULL,
                            drawn_picture text,
                            FOREIGN KEY (resource_id) REFERENCES TypeOfResource (resource_id));