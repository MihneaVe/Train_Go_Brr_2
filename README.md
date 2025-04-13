# README - Sistem de Management al Gării Feroviare

## Descriere generală

Aplicația reprezintă un sistem de gestionare a unei gări feroviare care permite administrarea trenurilor, rutelor, stațiilor și biletelor. Sistemul facilitează operațiunile comune într-o gară, cum ar fi:

- Cumpărarea biletelor
- Gestionarea rutelor
- Programarea trenurilor
- Administrarea utilizatorilor

---

## Structura aplicației

### Pachetul `pkcg.model`

Acest pachet conține clasele model care reprezintă entitățile de bază ale sistemului:

- **`User` (clasă abstractă)**  
    - Clasa de bază pentru utilizatori cu autentificare  
    - Conține `username` și `parola` cu metode de autentificare  

- **`Admin` (extinde `User`)**  
    - Reprezentare pentru administratorii sistemului  
    - Are drepturi sporite precum modificarea prețurilor sau adăugarea trenurilor  

- **`Customer` (extinde `User`)**  
    - Reprezintă clienții sistemului  
    - Conține date personale (`nume complet`, `email`)  
    - Poate achiziționa bilete și face rezervări  

- **`Station`**  
    - Reprezintă o stație de tren  
    - Include numele stației și platformele disponibile  
    - Gestionează lista de peroane ale stației  

- **`Platform`**  
    - Reprezintă un peron din stație  
    - Are un număr unic de identificare  

- **`Train`**  
    - Reprezintă un tren  
    - Include `număr`, `tip` și `capacitate`  

- **`Route`**  
    - Reprezintă o rută între două stații  
    - Conține `stația de plecare`, `stația de destinație` și `prețul de bază`  

- **`Schedule`**  
    - Reprezintă programarea unui tren pe o anumită rută  
    - Include `trenul`, `ruta`, `ora de plecare`, `ora de sosire` și `numărul peronului`  

- **`Ticket`**  
    - Reprezintă un bilet pentru călătorie  
    - Conține detalii despre `client`, `programare`, `preț` și `clasă` (prima clasă sau nu)  

- **`Reservation`**  
    - Reprezintă o rezervare pentru un loc  
    - Include `clientul`, `programarea`, `numărul locului` și `statusul confirmării`  

---

### Pachetul `pkcg.service`

Acest pachet conține clasele care implementează logica de business:

- **`UserService`**  
    - Gestionează utilizatorii sistemului  
    - Permite înregistrarea administratorilor și clienților  
    - Asigură funcționalitățile de autentificare și autorizare  
    - Menține sesiunea utilizatorului curent  

- **`StationService`**  
    - Administrează stațiile, trenurile, rutele și programările  
    - Permite adăugarea și gestionarea stațiilor  
    - Permite adăugarea și gestionarea trenurilor  
    - Oferă funcționalități de căutare după stație destinație  
    - Permite administrarea rutelor și actualizarea prețurilor  
    - Gestionează rezervările de locuri  

- **`TicketService`**  
    - Gestionează achiziționarea și administrarea biletelor  
    - Permite cumpărarea de bilete pentru o programare  
    - Oferă rapoarte privind biletele vândute  
    - Calculează veniturile totale  

---

### Clasa `pkcg.Main`

Clasa principală care demonstrează funcționalitățile sistemului:

- Inițializează serviciile necesare  
- Creează date de test (`stații`, `trenuri`, `rute`, `programări`, `utilizatori`)  
- Demonstrează operațiunile disponibile pentru administratori și clienți  

---

## Operațiunile disponibile

### Înregistrare și autentificare

- Înregistrare administratori și clienți cu date personale  
- Autentificare în sistem cu `username` și `parolă`  

### Gestionarea stațiilor

- Adăugarea de stații noi cu număr de peroane  
- Obținerea listei de stații disponibile  

### Gestionarea trenurilor

- Adăugarea de trenuri noi cu detalii despre tip și capacitate  
- Vizualizarea trenurilor disponibile  

### Administrarea rutelor

- Crearea de rute între stații cu prețuri de bază  
- Actualizarea prețurilor pentru rute (doar administrator)  
- Vizualizarea rutelor disponibile  

### Gestionarea programărilor

- Adăugarea de programări pentru trenuri pe rute specifice  
- Căutarea programărilor după destinație  
- Vizualizarea tuturor programărilor  

### Administrarea biletelor

- Cumpărarea de bilete pentru o programare  
- Selectarea clasei de călătorie (`standard` sau `prima clasă`)  
- Vizualizarea biletelor pentru un client  

### Rezervări

- Rezervarea locurilor pentru o programare  
- Confirmarea rezervărilor  
- Anularea rezervărilor  

### Rapoarte

- Calcularea veniturilor totale din vânzarea biletelor  

---

## Exemplu de utilizare

Aplicația demonstrează prin clasa `Main`:

### Operațiuni administrative:

1. Autentificare ca administrator  
2. Modificarea prețurilor pentru rute  
3. Vizualizarea rutelor actualizate  

### Operațiuni pentru clienți:

1. Autentificare ca client  
2. Vizualizarea rutelor disponibile  
3. Cumpărarea unui bilet de călătorie  