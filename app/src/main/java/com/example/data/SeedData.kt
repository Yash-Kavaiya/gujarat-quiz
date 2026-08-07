package com.example.data

object SeedData {

    val categories = listOf(
        QuizCategory(
            id = "gpsc_1_2",
            titleEn = "GPSC Class 1 & 2",
            titleGuj = "જીપીએસસી · Class 1 & 2",
            questionCount = 1840,
            difficulty = "Hard",
            coinCost = 100,
            initials = "GP",
            tag = "HOT",
            isRecommended = true
        ),
        QuizCategory(
            id = "gsssb_clerk",
            titleEn = "GSSSB Clerk (CCE)",
            titleGuj = "જીએસએસએસબી · 1,420 Qs",
            questionCount = 1420,
            difficulty = "Medium",
            coinCost = 100,
            initials = "GS",
            tag = "HOT",
            isRecommended = true
        ),
        QuizCategory(
            id = "psi_written",
            titleEn = "Police Sub Inspector",
            titleGuj = "પોલીસ એસ.આઈ. · 980 Qs",
            questionCount = 980,
            difficulty = "Hard",
            coinCost = 80,
            initials = "PS",
            tag = null,
            isRecommended = false
        ),
        QuizCategory(
            id = "talati_mantri",
            titleEn = "Talati cum Mantri",
            titleGuj = "તલાટી · 1,160 Qs",
            questionCount = 1160,
            difficulty = "Medium",
            coinCost = 80,
            initials = "TA",
            tag = "TRENDING",
            isRecommended = true
        ),
        QuizCategory(
            id = "tet_tat",
            titleEn = "Teacher Eligibility TET/TAT",
            titleGuj = "શિક્ષક પાત્રતા · 760 Qs",
            questionCount = 760,
            difficulty = "Medium",
            coinCost = 60,
            initials = "TE"
        ),
        QuizCategory(
            id = "gset_exam",
            titleEn = "Gujarat Eligibility Test GSET",
            titleGuj = "જી-સેટ · 540 Qs",
            questionCount = 540,
            difficulty = "Hard",
            coinCost = 80,
            initials = "GS"
        ),
        QuizCategory(
            id = "constable_lrd",
            titleEn = "Police Constable / LRD",
            titleGuj = "કોન્સ્ટેબલ · 890 Qs",
            questionCount = 890,
            difficulty = "Easy",
            coinCost = 60,
            initials = "LR"
        ),
        QuizCategory(
            id = "bin_sachivalay",
            titleEn = "Bin Sachivalay Clerk",
            titleGuj = "બિન સચિવાલય · 670 Qs",
            questionCount = 670,
            difficulty = "Medium",
            coinCost = 60,
            initials = "BI"
        ),
        QuizCategory(
            id = "gujarat_history",
            titleEn = "Gujarat History",
            titleGuj = "ગુજરાતનો ઇતિહાસ · 420 Qs",
            questionCount = 420,
            difficulty = "Easy",
            coinCost = 40,
            initials = "GH"
        ),
        QuizCategory(
            id = "geography_economy",
            titleEn = "Geography & Economy",
            titleGuj = "ભૂગોળ અને અર્થતંત્ર · 380 Qs",
            questionCount = 380,
            difficulty = "Medium",
            coinCost = 40,
            initials = "GE"
        ),
        QuizCategory(
            id = "polity_constitution",
            titleEn = "Polity & Constitution",
            titleGuj = "બંધારણ અને રાજવ્યવસ્થા · 460 Qs",
            questionCount = 460,
            difficulty = "Medium",
            coinCost = 40,
            initials = "PC"
        ),
        QuizCategory(
            id = "current_affairs",
            titleEn = "Current Affairs",
            titleGuj = "વર્તમાન પ્રવાહો · 320 Qs",
            questionCount = 320,
            difficulty = "Easy",
            coinCost = 40,
            initials = "CA",
            tag = "WEEKLY"
        ),
        QuizCategory(
            id = "science_tech",
            titleEn = "Science & Tech",
            titleGuj = "વિજ્ઞાન અને ટેકનોલોજી · 510 Qs",
            questionCount = 510,
            difficulty = "Medium",
            coinCost = 40,
            initials = "ST"
        ),
        QuizCategory(
            id = "quantitative_aptitude",
            titleEn = "Quantitative Aptitude",
            titleGuj = "ગણિત અને અંકશાસ્ત્ર · 680 Qs",
            questionCount = 680,
            difficulty = "Hard",
            coinCost = 40,
            initials = "QA"
        ),
        QuizCategory(
            id = "logical_reasoning",
            titleEn = "Logical Reasoning",
            titleGuj = "તાર્કિક કસોટી · 590 Qs",
            questionCount = 590,
            difficulty = "Medium",
            coinCost = 40,
            initials = "LR"
        ),
        QuizCategory(
            id = "english_language",
            titleEn = "English Language",
            titleGuj = "અંગ્રેજી વ્યાકરણ · 440 Qs",
            questionCount = 440,
            difficulty = "Easy",
            coinCost = 40,
            initials = "EN"
        ),
        QuizCategory(
            id = "gujarati_grammar",
            titleEn = "Gujarati Grammar",
            titleGuj = "ગુજરાતી વ્યાકરણ · 520 Qs",
            questionCount = 520,
            difficulty = "Easy",
            coinCost = 40,
            initials = "GG"
        ),
        QuizCategory(
            id = "head_clerk",
            titleEn = "GSSSB Head / Senior Clerk",
            titleGuj = "મુખ્ય ક્લાર્ક · 640 Qs",
            questionCount = 640,
            difficulty = "Medium",
            coinCost = 80,
            initials = "HC"
        ),
        QuizCategory(
            id = "nayab_mamlatdar",
            titleEn = "Nayab Mamlatdar / DySO",
            titleGuj = "નાયબ મામલતદાર / DySO · 480 Qs",
            questionCount = 480,
            difficulty = "Hard",
            coinCost = 80,
            initials = "DY"
        ),
        QuizCategory(
            id = "forest_guard",
            titleEn = "Forest Guard / RFO",
            titleGuj = "વનરક્ષક · 410 Qs",
            questionCount = 410,
            difficulty = "Medium",
            coinCost = 60,
            initials = "FG"
        ),
        QuizCategory(
            id = "gujarat_budget",
            titleEn = "Gujarat Budget & Economics",
            titleGuj = "ગુજરાત બજેટ · 290 Qs",
            questionCount = 290,
            difficulty = "Medium",
            coinCost = 40,
            initials = "GB"
        ),
        QuizCategory(
            id = "environment_ecology",
            titleEn = "Environment & Ecology",
            titleGuj = "પર્યાવરણ · 260 Qs",
            questionCount = 260,
            difficulty = "Easy",
            coinCost = 40,
            initials = "EE"
        ),
        QuizCategory(
            id = "computer_knowledge",
            titleEn = "Computer Knowledge",
            titleGuj = "કમ્પ્યુટર જ્ઞાન · 340 Qs",
            questionCount = 340,
            difficulty = "Easy",
            coinCost = 40,
            initials = "CK"
        ),
        QuizCategory(
            id = "revenue_talati_gk",
            titleEn = "Revenue / Talati GK",
            titleGuj = "મહેસૂલી જ્ઞાન · 380 Qs",
            questionCount = 380,
            difficulty = "Medium",
            coinCost = 60,
            initials = "RT"
        ),
        QuizCategory(
            id = "full_mock_test",
            titleEn = "Full-Syllabus Mock Tests",
            titleGuj = "સંપૂર્ણ મોક ટેસ્ટ · 120 Qs",
            questionCount = 120,
            difficulty = "Hard",
            coinCost = 100,
            initials = "MT",
            tag = "WIN 5,000"
        )
    )

    val sampleQuestions = listOf(
        Question(
            id = "q1",
            categoryId = "gujarat_history",
            topicTag = "GUJARAT HISTORY",
            questionEn = "Who was the first Chief Minister of Gujarat after its formation in 1960?",
            questionGuj = "૧૯૬૦ માં નિર્માણ બાદ ગુજરાતના પ્રથમ મુખ્યમંત્રી કોણ હતા?",
            options = listOf("Morarji Desai", "Jivraj Narayan Mehta", "Balwantrai Mehta", "Hitendra Desai"),
            correctIndex = 1,
            explanation = "Dr. Jivraj Narayan Mehta served as the first Chief Minister of Gujarat from May 1, 1960 until March 1963."
        ),
        Question(
            id = "q2",
            categoryId = "polity_constitution",
            topicTag = "CONSTITUTION",
            questionEn = "Which Article of the Indian Constitution enshrines the Right to Education (6-14 years)?",
            questionGuj = "ભારતીય બંધારણની કઈ કલમ ૬ થી ૧૪ વર્ષ માટે શિક્ષણનો અધિકાર આપે છે?",
            options = listOf("Article 19", "Article 21A", "Article 32", "Article 45"),
            correctIndex = 1,
            explanation = "Article 21A was inserted by the 86th Constitutional Amendment Act, 2002 to make free and compulsory education a Fundamental Right."
        ),
        Question(
            id = "q3",
            categoryId = "geography_economy",
            topicTag = "GEOGRAPHY",
            questionEn = "The Sabarmati river originates from which mountain range?",
            questionGuj = "સાબરમતી નદી કઈ પર્વતમાળામાંથી નીકળે છે?",
            options = listOf("Aravalli Range", "Vindhya Range", "Satpura Range", "Western Ghats"),
            correctIndex = 0,
            explanation = "The Sabarmati river originates in the Aravalli Hills of Udaipur District, Rajasthan and flows through Gujarat."
        ),
        Question(
            id = "q4",
            categoryId = "gpsc_1_2",
            topicTag = "GPSC MAINS/PRELIMS",
            questionEn = "Which Solanki ruler built the Sun Temple at Modhera in Gujarat?",
            questionGuj = "મોઢેરાનું સૂર્ય મંદિર કયા સોલંકી શાસકે બંધાવ્યું હતું?",
            options = listOf("Siddharaj Jaisinh", "Bhimdev I", "Kumarpal", "Mularaja I"),
            correctIndex = 1,
            explanation = "The Sun Temple at Modhera was built during the reign of Bhimdev I of the Solanki dynasty in 1026-27 AD."
        ),
        Question(
            id = "q5",
            categoryId = "current_affairs",
            topicTag = "CURRENT AFFAIRS",
            questionEn = "GIFT City (Gujarat International Finance Tec-City) is situated in which district?",
            questionGuj = "GIFT સિટી (ગુજરાત ઇન્ટરનેશનલ ફાઇનાન્સ ટેક-સિટી) કયા જિલ્લામાં આવેલું છે?",
            options = listOf("Ahmedabad", "Gandhinagar", "Surat", "Vadodara"),
            correctIndex = 1,
            explanation = "GIFT City is located between Ahmedabad and Gandhinagar on the banks of the Sabarmati River in Gandhinagar district."
        )
    )

    val sampleCapsules = listOf(
        Capsule(
            id = "cap_art21a",
            topic = "Polity",
            readTime = "3-min read",
            titleEn = "Article 21A — Right to Education",
            titleGuj = "શિક્ષણનો અધિકાર",
            tldr = "Free & compulsory education for kids aged 6–14 years as a Fundamental Right.",
            keyFacts = listOf(
                "Inserted by the 86th Constitutional Amendment, 2002.",
                "Operationalised through the Right to Education Act, 2009.",
                "Makes elementary education a fundamental right — earlier it was a Directive Principle under Art. 45.",
                "States must ensure no child is denied admission for want of documents or fee."
            ),
            relatedCategoryId = "polity_constitution",
            questionCount = 12
        ),
        Capsule(
            id = "cap_solanki_dynasty",
            topic = "Gujarat History",
            readTime = "4-min read",
            titleEn = "Solanki Era — Golden Age of Gujarat",
            titleGuj = "સોલંકી યુગ · ગુજરાતનો સુવર્ણ યુગ",
            tldr = "Cultural, architectural, and economic peak of medieval Gujarat under Solanki kings.",
            keyFacts = listOf(
                "Founded by Mularaja I in 942 AD with capital at Anahilapataka (Patan).",
                "Rani ki Vav built by Queen Udayamati in memory of Bhimdev I (UNESCO World Heritage Site).",
                "Siddharaj Jaisinh built Sahasralinga Talao and conquered Junagadh & Malwa.",
                "Hemachandracharya authored 'Siddha-Hema-Shabdanushasana' during Siddharaj's rule."
            ),
            relatedCategoryId = "gujarat_history",
            questionCount = 15
        ),
        Capsule(
            id = "cap_rivers_gujarat",
            topic = "Geography",
            readTime = "3-min read",
            titleEn = "Major Rivers & Dams of Gujarat",
            titleGuj = "ગુજરાતની મુખ્ય નદીઓ અને બંધો",
            tldr = "Essential facts about Narmada, Tapti, Sabarmati, and Mahi rivers for GPSC & Talati.",
            keyFacts = listOf(
                "Sardar Sarovar Dam built across the Narmada River at Kevadia (Ekta Nagar).",
                "Ukai Dam is built across the Tapti River in Tapi district.",
                "Dharoi Dam is located across the Sabarmati River in Mehsana.",
                "Mahi is the only river crossing the Tropic of Cancer twice in India."
            ),
            relatedCategoryId = "geography_economy",
            questionCount = 10
        )
    )

    val leaderboard = listOf(
        LeaderboardUser(rank = 1, name = "Priyank V.", initials = "PV", city = "Surat", score = 4820, delta = "+1"),
        LeaderboardUser(rank = 2, name = "Kaushal M.", initials = "KM", city = "Ahmedabad", score = 4790, delta = "-1"),
        LeaderboardUser(rank = 3, name = "Dhruvi P.", initials = "DP", city = "Rajkot", score = 4715, delta = "+3"),
        LeaderboardUser(rank = 4, name = "Harsh S.", initials = "HS", city = "Vadodara", score = 4602, delta = "+2"),
        LeaderboardUser(rank = 5, name = "Mira J.", initials = "MJ", city = "Bhavnagar", score = 4588, delta = "0"),
        LeaderboardUser(rank = 6, name = "Yash T.", initials = "YT", city = "Gandhinagar", score = 4570, delta = "+4"),
        LeaderboardUser(rank = 142, name = "Aakash P. (You)", initials = "AP", city = "Ahmedabad", score = 4012, delta = "↑ 38", isCurrentUser = true)
    )

    val coinPacks = listOf(
        CoinPack(id = "pack_500", coins = 500, bonusTag = null, priceInr = 49, approxUsd = "≈ 0.59 USD"),
        CoinPack(id = "pack_1200", coins = 1200, bonusTag = "+20% bonus", priceInr = 99, approxUsd = "≈ 1.19 USD", isPopular = true),
        CoinPack(id = "pack_3000", coins = 3000, bonusTag = "+50% bonus", priceInr = 199, approxUsd = "≈ 2.39 USD"),
        CoinPack(id = "pack_8000", coins = 8000, bonusTag = "+78% bonus", priceInr = 449, approxUsd = "≈ 5.39 USD", isBestValue = true),
        CoinPack(id = "pack_20000", coins = 20000, bonusTag = "x2 bonus", priceInr = 999, approxUsd = "≈ 11.99 USD")
    )

    val powerUps = listOf(
        PowerUp(id = "pu_hints_5", title = "5 Hints Pack", description = "Eliminates 2 wrong options in practice quizzes", coinPrice = 150, iconName = "lightbulb"),
        PowerUp(id = "pu_hints_15", title = "15 Hints Bundle", description = "Bulk discount for serious aspirants", coinPrice = 350, iconName = "lightbulb_bulk"),
        PowerUp(id = "pu_streak_freeze", title = "Streak Freeze", description = "Protects your daily streak if you miss a day", coinPrice = 200, iconName = "snowflake")
    )

    val duelFriends = listOf(
        DuelFriend(id = "f1", name = "Darshan R.", initials = "DR", city = "Surat", status = "Online now", isOnline = true),
        DuelFriend(id = "f2", name = "Mira J.", initials = "MR", city = "Bhavnagar", status = "Online now", isOnline = true),
        DuelFriend(id = "f3", name = "Priyank V.", initials = "PR", city = "Surat", status = "Last seen 2h ago", isOnline = false),
        DuelFriend(id = "f4", name = "Yash T.", initials = "YT", city = "Gandhinagar", status = "Online now", isOnline = true)
    )

    fun generateMock100Questions(): List<MockQuestion> {
        val list = mutableListOf<MockQuestion>()
        for (i in 1..100) {
            val section = when {
                i <= 50 -> "General Studies"
                i <= 80 -> "Aptitude"
                else -> "Gujarati"
            }
            val topic = when {
                i <= 20 -> "GEOGRAPHY"
                i <= 35 -> "POLITY"
                i <= 50 -> "HISTORY"
                i <= 65 -> "MATHS"
                i <= 80 -> "REASONING"
                else -> "GRAMMAR"
            }
            val qEn = when (i % 5) {
                1 -> "Q$i: The Sabarmati river originates from which mountain range?"
                2 -> "Q$i: Which Article of the Indian Constitution enshrines Right to Education?"
                3 -> "Q$i: Find the next term in series: 2, 6, 12, 20, 30, ...?"
                4 -> "Q$i: 'અલંકાર ઓળખાવો': મુખ ચંદ્ર જેવું સુંદર છે."
                else -> "Q$i: Who was the first Chief Minister of Gujarat State in 1960?"
            }
            val qGuj = when (i % 5) {
                1 -> "સાબરમતી નદી કઈ પર્વતમાળામાંથી નીકળે છે?"
                2 -> "ભારતીય બંધારણની કઈ કલમ શિક્ષણનો અધિકાર આપે છે?"
                3 -> "શ્રેણીનો આગળનો પદ શોધો: ૨, ૬, ૧૨, ૨૦, ૩૦, ...?"
                4 -> "અલંકાર ઓળખાવો: મુખ ચંદ્ર જેવું સુંદર છે."
                else -> "૧૯૬૦માં ગુજરાત રાજ્યના પ્રથમ મુખ્યમંત્રી કોણ હતા?"
            }
            val options = when (i % 5) {
                1 -> listOf("Aravalli", "Vindhya", "Satpura", "Western Ghats")
                2 -> listOf("Article 19", "Article 21A", "Article 32", "Article 45")
                3 -> listOf("36", "40", "42", "48")
                4 -> listOf("ઉપમા", "રૂપક", "ઉત્પ્રેક્ષા", "અતિશયોક્તિ")
                else -> listOf("Morarji Desai", "Jivraj Narayan Mehta", "Balwantrai Mehta", "Hitendra Desai")
            }
            val correctIdx = when (i % 5) {
                1 -> 0
                2 -> 1
                3 -> 2
                4 -> 0
                else -> 1
            }
            list.add(
                MockQuestion(
                    id = i,
                    section = section,
                    topic = topic,
                    questionEn = qEn,
                    questionGuj = qGuj,
                    options = options,
                    correctIndex = correctIdx
                )
            )
        }
        return list
    }

    val sampleLeaderboard: List<LeaderboardUser> get() = leaderboard
    val sampleDuelFriends: List<DuelFriend> get() = duelFriends
    val marchStreakDays: List<StreakCalendarDay> = (1..31).map { day ->
        StreakCalendarDay(
            dayNumber = day,
            isCompleted = day <= 20,
            isFrozen = day == 14
        )
    }
}

