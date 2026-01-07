CREATE TABLE virus (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    transmission_rate DOUBLE PRECISION NOT NULL CHECK (transmission_rate >= 0.0 AND transmission_rate <= 1.0),
    incubation_hours INT NOT NULL,
    lethality DOUBLE PRECISION NOT NULL CHECK (lethality >= 0.0 AND lethality <= 1.0)
);

CREATE TABLE human (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_infected BOOLEAN NOT NULL DEFAULT FALSE,
    is_immune BOOLEAN NOT NULL DEFAULT FALSE,
    infection_date TIMESTAMP
);

CREATE TABLE zombie (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    strength INT NOT NULL CHECK (strength >= 1 AND strength <= 10),
    speed INT NOT NULL CHECK (speed >= 1 AND speed <= 10),
    transformation_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_human_infected ON human(is_infected);
CREATE INDEX idx_human_immune ON human(is_immune);
CREATE INDEX idx_zombie_transformation_date ON zombie(transformation_date);
