# Gujarat Quiz

A bilingual (English + ગુજરાતી) exam-prep quiz app for GPSC, GSSSB, Talati, PSI, TET/TAT and other
Gujarat government exams. Built with Jetpack Compose, Room and DataStore.

## Features

| Area | What it does |
| --- | --- |
| **Practice quizzes** | 24 exam & subject categories. Each quiz draws 10 random questions from a bilingual bank of 95 questions; exam categories (GPSC, GSSSB, Talati…) mix the subjects in that exam's syllabus. 30-second timer per question, 50-50 hints, explanations after every answer. |
| **Daily Challenge** | One mixed 10-question set per day, the same for everyone that day. One attempt per day; +500 bonus for a perfect score. |
| **Mock test** | Full-length paper built from the whole bank, grouped into General Studies / Aptitude / Language / Computer sections. 2-hour countdown with auto-submit, question palette, mark-for-review, +2 / −0.66 marking. |
| **1v1 Duels** | 5-round duels against a simulated opponent with a 20-second round timer. Winner gets 200 coins; a draw refunds the 100-coin entry. |
| **Streaks** | A real daily streak calculated from completed quizzes. Streak freezes cover missed days automatically. The calendar shows the current month, and the 3/7/14/30/100-day milestones can each be claimed once. |
| **Coin economy** | Wallet with a full transaction history. Hint packs and streak freezes from the shop go into your inventory. |
| **Leaderboard** | You earn 25 rank points per correct answer, and your rank is calculated live against the leaderboard. |
| **Revision capsules** | Short micro-notes with key facts. Bookmarks are saved, you can share a capsule, and "Practice" opens a quiz on the related category. |
| **Profile** | Edit your name and city, switch between three themes (Saffron / Indigo / Midnight), and invite friends with your referral code (copy, WhatsApp or the share sheet). |

> Coin packs in the shop are credited instantly. Google Play Billing is not integrated yet.

## Project layout

```
app/src/main/java/com/example/
├── MainActivity.kt            # Navigation host + bottom bar
├── viewmodel/MainViewModel.kt # All app state: quizzes, timers, streaks, coins, duels
├── data/
│   ├── QuestionBank.kt        # Bilingual question bank + exam syllabus mapping
│   ├── StreakCalculator.kt    # Pure streak / milestone / mock-scoring logic
│   ├── SeedData.kt            # Categories, capsules, shop items, duel friends
│   ├── Entities.kt, AppDao.kt, AppDatabase.kt, AppRepository.kt   # Room (schema v3)
└── ui/                        # Compose screens, components and themes
```

## Building

```bash
gradle :app:assembleDebug        # build (or open in Android Studio)
gradle :app:testDebugUnitTest    # unit + Robolectric tests
```

Requires Android SDK 36 and JDK 17+. The database migrates from v2 to v3 without losing data.
