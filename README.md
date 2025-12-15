[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/_uV8Mn8f)
# 📘 Projektarbete: JPA + Hibernate med GitHub-flöde

Projektet genomförs som antingen en Java CLI-applikation eller med hjälp av JavaFX om ni vill ha ett grafiskt gränssnitt.
Arbetet utförs i grupper om fyra deltagare. Ni bildar grupperna genom att antingen skapa en ny grupp eller
ansluta till en befintlig grupp via GitHub Classrooms.

Projektet ska använda en relationsdatabas, där MySQL eller PostgreSQL rekommenderas.
Kommunikation med databasen ska ske med JPA och Hibernate, enligt code first-metoden.

## 🗓️ Veckoplanering med Checklista
### ✅ Vecka 1 – Grundläggning och struktur
- [ ] Klona GitHub-repo
- [ ] Konfigurera persistence.xml eller använd PersistenceConfiguration i kod
- [ ] Skapa entiteter och verifiera tabellgenerering
- [ ] Lägg till relationer (One-to-Many, Many-to-Many)
- [ ] Arbeta på feature-branches och använd pull requests för kodgranskning

### ✅ Vecka 2 – Funktionalitet och relationer
- [ ] Dela upp funktioner mellan gruppmedlemmar
- [ ] Implementera funktionalitet för huvudentiteter
- [ ] Testa queries med EntityManager
- [ ] Dokumentera större designbeslut i PR-beskrivningar
- [ ] Säkerställ att alla merges sker via kodgranskning

### ✅ Vecka 3 – Finslipning och presentation
- [ ] Lägg till validering, felhantering och loggning
- [ ] Skriv enhetstester för centrala funktioner
- [ ] Förbered demo (~10 min):
- [ ] Visa applikationen (CLI-kommandon eller GUI)
- [ ] Gå igenom datamodellen och relationerna

## 🎯 Projektförslag
### Spelturnering / E-sportplattform 🎮

One-to-Many: En turnering → flera matcher

Many-to-Many: Spelare ↔ Lag

### Film- och serietjänst (à la Netflix) 🎬

One-to-Many: En regissör → flera filmer/serier

Many-to-Many: Användare ↔ Favoritlistor

### Musikspelare 🎵

One-to-Many: En artist → flera album

Many-to-Many: Album ↔ Spellistor

### Projekt- och uppgiftshantering 📋

One-to-Many: Ett projekt → flera uppgifter

Many-to-Many: Uppgifter ↔ Användare

### Restaurangbokning 🍽️

One-to-Many: En restaurang → flera bord

Many-to-Many: Gäster ↔ Bokningar
