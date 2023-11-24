# Proiect GlobalWaves  - Etapa 1

## Detalii de implementare (pachetul `utils`)

  + In implementarea proiectului s-a folosit o clasa numita `CommandParser` care are rolul de a parcurge lista de comenzi si
sa execute comenzile necesare.
  + Parser-ul de comenzi va tine o biblioteca cu toate melodiile si podcast-urile, o lista cu utilizatorii (de clasa `User`), o lista cu
playlist-urile create si o lista in care se contorizeaza like-urile melodiilor.
  + Clasa `User` reprezinta contul unui utilizator in care se afla lista de melodii apreciate, lista cu playlist-uri create si
player-ul audio. Aceasta clasa este mai mult un wrapper al comenzilor catre playlist-uri si player pentru a oferi un API
pentru parser-ul de comenzi.
  + Clasa `Player` este, dupa cum spune si numele, clasa player-elor audio. Au fost creati mai multi constructori pentru fiecare
tip de player (de melodie, de podcast, de playlist). Player-ul are toate comenzile ce pot se gasesc intr-un player normal:
`play`, `pause`, `stop`, `next`, `prev`, `forward`, `backward`.
  + Clasa `Playlist` este reprezentarea unui playlist si contorizeaza numarul de followeri ai acestuia pentru Top 5.
  + Clasa `Searcher` creaaza un obiect folosit de parser pentru a cauta o melodie/un podcast/un playlist in baza de date.
  + Clasa `PodcastSave` este folosita de User pentru a memora locul din podcast in care a fost oprit Player-ul, pentru a putea
reveni in acelasi loc la o reluare a podcast-ului.
  + Clasa `Song` reprezinta doar o asociere intre numele unei melodii si numarul de like-uri (preferand sa se foloseasca SongInput pentru alte operatii).
  + Clasa `Stats` este folosita pentru a formata statusul Player-ului la un anumit moment de timp pentru comanda `status`.

## Alte clase
  + Clasele din `fileio` sunt folosite pentru formatarea JSON, libraria Jackson avand nevoie de ele pentru a citi si pentru a scrie fisiere JSON.
  + Clasele din `main` sunt clasele principale din care porneste tot codul. Ele folosesc clasele din `utils` descrise mai sus.