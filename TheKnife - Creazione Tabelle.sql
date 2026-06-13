CREATE TABLE Utenti(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	nome VARCHAR(30) NOT NULL CHECK(TRIM(nome) <> ''),
	cognome VARCHAR(30) NOT NULL CHECK(TRIM(cognome) <> ''),
	username VARCHAR(20) UNIQUE NOT NULL CHECK(TRIM(username) <> ''),
	email VARCHAR(30) UNIQUE NOT NULL CHECK (email LIKE('%@%')),
	password_hash VARCHAR(255) NOT NULL,
	data_nascita DATE,
	citta VARCHAR(50) NOT NULL CHECK(TRIM(citta) <> ''),
	nazione VARCHAR(50) NOT NULL CHECK(TRIM(nazione) <> ''),
	ruolo VARCHAR(11) NOT NULL CHECK(ruolo IN ('cliente', 'ristoratore'))
);

CREATE TABLE Ristoranti(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	proprietario INT REFERENCES Utente(id) ON DELETE CASCADE ON UPDATE CASCADE,
	nome VARCHAR(50) NOT NULL CHECK(TRIM(nome) <> ''),
	indirizzo VARCHAR(100) NOT NULL CHECK(TRIM(indirizzo) <> ''),
	citta VARCHAR(20) NOT NULL CHECK(TRIM(citta) <> ''),
	nazione VARCHAR(20) NOT NULL CHECK(TRIM(nazione) <> ''),
	fascia_prezzo VARCHAR(6) NOT NULL CHECK(TRIM(fascia_prezzo) <> ''),
	latitudine DECIMAL(9,7) NOT NULL,
	longitudine DECIMAL(10, 7) NOT NULL,
	recapito_telefonico VARCHAR(20) UNIQUE NOT NULL CHECK(TRIM(recapito_telefonico) <> ''),
	pagina_michelin VARCHAR(100) UNIQUE NOT NULL CHECK(pagina_michelin LIKE('https://guide.michelin.com/%')),
	pagina_web VARCHAR(100) UNIQUE NOT NULL CHECK(pagina_web LIKE('https://%')),
	riconoscimento VARCHAR(30) NOT NULL CHECK(riconoscimento IN ('1 Star', '2 Stars', '3 Stars', 'Selected Restaurants', 'Bib Gourmand')),
	stella_green BOOLEAN NOT NULL,
	descrizione TEXT NOT NULL CHECK(TRIM(descrizione) <> ''),
	prenotazione_online BOOLEAN NOT NULL,
	delivery BOOLEAN NOT NULL
);

CREATE TABLE ProposteCulinarie(
	ristorante INT REFERENCES Ristorante(id) ON DELETE CASCADE ON UPDATE CASCADE,
	tipo_cucina VARCHAR(40) NOT NULL CHECK(TRIM(tipo_cucina) <> ''),
	PRIMARY KEY(ristorante, tipo_cucina)
);

CREATE TABLE ServiziOfferti(
	ristorante INT REFERENCES Ristorante(id) ON DELETE CASCADE ON UPDATE CASCADE,
	servizio VARCHAR(40) NOT NULL CHECK(TRIM(servizio) <> ''),
	PRIMARY KEY(servizio, ristorante)
);

CREATE TABLE Preferiti(
	cliente INT REFERENCES Utente(id) ON DELETE CASCADE ON UPDATE CASCADE,
	ristorante INT REFERENCES Ristorante(id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY(cliente, ristorante)
);

CREATE TABLE Recensioni(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	valutazione INT NOT NULL CHECK(valutazione BETWEEN 1 AND 5),
	testo VARCHAR(200) NOT NULL CHECK(TRIM(testo) <> ''),
	ultima_modifica DATE NOT NULL,
	autore INT REFERENCES Utente(id) ON DELETE CASCADE ON UPDATE CASCADE,
	ristorante INT REFERENCES Ristorante(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Risposte(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	recensione INT REFERENCES Recensione(id) ON DELETE CASCADE ON UPDATE CASCADE,
	autore INT REFERENCES Utente(id) ON DELETE CASCADE ON UPDATE CASCADE,
	testo VARCHAR(200) NOT NULL CHECK(TRIM(testo) <> ''),
	ultima_modifica DATE NOT NULL
);