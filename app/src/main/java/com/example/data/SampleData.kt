package com.example.data

import com.example.model.AudiobookItem
import com.example.model.BookCategory
import com.example.model.SentenceItem
import com.example.model.UserHighlight
import com.example.model.VoiceModel

object SampleData {
    const val LOGO_URL = "https://lh3.googleusercontent.com/aida/ALJJyrD3lCJP6cJyynYLzCeJXRTtoZ344fzAwW0ApRYTIFq_VvxqRYJH_DH6pYWasSPNZBWqyaxuaqGro2ZEx3-wtlzKY5by__xfdl4qz3vevbZArRUAJ6hzNJugioQcEc8TYuuaXflUcCDmysFYx229mPi1O86CwVO6wUEms9tSbExXebzelqLuAdYZqZY27z3NQ3wdLW2IR7eNeIp8tqTAcY0TIzGbjvfmx4r68flNzDP1oQ"
    const val AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDSU2Tj_GL5yWT3Z8VNm5JrfhKv7WJr7iGU-0XWD6kGTI0DzBYU5mqH1FXK2ZVVaCvohmVbP-qgu_6nNjOXH0QmYEmImOSYkKkPhJ9uvZy413J6dttzdaeg74Skm0ZnsZYlSQLRtVXl50ItulI7eBUIZk3VRs1wc0c_oQ3H4-58H_H5AeJbYux74HuUZKC8DqYEBHOwF_yLi81LfMQJY0LNiAbL5D52RN1ZHwx5YSQ9"
    const val CHAPTER_VIGNETTE_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuCMQZLUI1Cy8R8mdUI0laFu4wzdporCtKOAa3LqziUvdPKpg8_WFwAGcmPBxCCqU9sDCqPn_LiDNdCYRam6TNp0n1BsuiNskX1vuzw8UqkWGQDRi6Oqjt0IKPex8fby91RT2PaeJQCTtOXKak_MiRuOJD6L8s-8Wzh4TKM7uKDQ0I8uab0eHsxX3JYDU0WHw9kHAPJZlwxhMkJxduCz8CWEbMr9UjZexYQ68KaFs0MI"

    val KlaraAndTheSun = AudiobookItem(
        id = "klara_sun",
        title = "Klara and the Sun",
        author = "Kazuo Ishiguro",
        narrator = "Sura Siu",
        coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuASii12u6q5bpO7QmVlyJIQxeGWkdzcBx1SiFs4wHP4JN8QM6rBtFXM3Aen50_03TD5rCYBx_HIVQHjqck8h17A3z2Uemm5fHwWfLcE4gAWK0eRwZ2fR84aGdu85m4caXCQfQYXok49K3OJYVYY7Lb2-jcQbS6JsMPwvgDUnta9kMSSYq2Do4vwptvxHDTcgUYAhuVXf0xooX3B8ZiWqBVP-aUgAvVmz2GSBcbqp0uK",
        progressPercent = 68,
        currentChapter = "Chapter 4 · The Horizon",
        durationText = "10h 16m",
        remainingText = "14:22 remaining",
        rating = 4.9,
        category = BookCategory.CONTINUE_READING,
        isDownloaded = true,
        isSynchronized = true,
        speed = 1.25f,
        formatTag = "Synchronized Read"
    )

    val TrendingBooks = listOf(
        AudiobookItem(
            id = "tomorrow_tomorrow",
            title = "Tomorrow, & Tomorrow",
            author = "Gabrielle Zevin",
            narrator = "Jennifer Pickens",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDSLf-Q8rz2pB7G-qyFbso_qPhbU7UyVqOVkXQR7gq2fzufSbsLfrgpX06SVXY-MPiJxkPL3rD2fSTDDZ8wuu2UlLwJE36-CNSrQARpcQrFYXqkHFaB0qUvX7DfvVynJvUwZWfWJODImCXSFOiu0lmjsdbftV-qBJzULaQyBVvzqpxG7AG2ObaBonid2-3fLsTaFvQieoAGwFbmoXqXoaSAFRR4dirO1nYsT6TR6oZl",
            progressPercent = 0,
            currentChapter = "Chapter 1",
            durationText = "13h 52m",
            remainingText = "13h 52m",
            rating = 4.9,
            category = BookCategory.TRENDING,
            formatTag = "Listen & Read"
        ),
        AudiobookItem(
            id = "sea_of_tranquility",
            title = "Sea of Tranquility",
            author = "Emily St. John Mandel",
            narrator = "John Lee, Cast",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCKuB3NLpt8LAvO1jzZj0lBYHuBpOiADvaL1IiENh0zhJT21MOI15HMZleOIcb6eN9O0QsZshcmfMi8JE6Us8EHi9UIzb2uKSAOyn8EgVNS-q98_SpVWHbpY-ou6RLX6aVyZsbCqJQ1Pg6Qh6zUJWEWf_p1ZplbiyvFv0luN04-3t3JgDeMRV5YNVJPo5uI5qhMvNshjdN8v42tVQ3CbuiT1McIFhMq8NtR3rp3cWRi",
            progressPercent = 0,
            currentChapter = "Chapter 1",
            durationText = "5h 48m",
            remainingText = "5h 48m",
            rating = 4.8,
            category = BookCategory.TRENDING,
            formatTag = "Listen & Read"
        ),
        AudiobookItem(
            id = "demon_copperhead",
            title = "Demon Copperhead",
            author = "Barbara Kingsolver",
            narrator = "Charlie Thurston",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCIPLHKXMzPgqEWinmQQEJzhgK34z8dS2gcb4mBmUtsFo46V2R_PqSS8kBEPT5W5emWr5ZmDnWON5kGQCNMDk0eV9ehZoPol9aOeTUH5QA5XAu4-A9f-0PjDZgvTgX-us6TxPJD_pPbWg4UD4gCYiYj2o8vVDAyjq_z3HeMv_5zErn06Nzdb-9bdPIxJWqOUfQ0P-EN6h1FumSb8mIrOKJU73BUc_m3c4673wfzuq26",
            progressPercent = 0,
            currentChapter = "Chapter 1",
            durationText = "21h 03m",
            remainingText = "21h 03m",
            rating = 4.9,
            category = BookCategory.TRENDING,
            formatTag = "Listen & Read"
        )
    )

    val ShortListens = listOf(
        AudiobookItem(
            id = "letters_poet",
            title = "Letters to a Young Poet",
            author = "Rainer Maria Rilke",
            narrator = "Classic Voice",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD7lp9MO2tBZH3KUkyVF8C44fgw7UCdANl6PWpYBrSy9CzPqRqyzee286c9p9ykVX636KkL-s_eMuELLJ4HXFuAJKmHLPMwCrOh-Pa8G0cYArA6sToxIkkeOdLC14omxfCdZMF4DYArjvwhkJLp8HQOeP2Km-jIJoKk3DiUqT4oIp74MraYXcyh7XXG6HOVidD--Y0KMrxfpobvSwYqyc3gW8yaI1GRQ5Vf5yQ3wTzP",
            progressPercent = 0,
            durationText = "1h 24m",
            remainingText = "1h 24m",
            rating = 4.8,
            category = BookCategory.SHORT_LISTENS,
            formatTag = "Complete Essay"
        ),
        AudiobookItem(
            id = "room_ones_own",
            title = "A Room of One's Own",
            author = "Virginia Woolf",
            narrator = "Literary Cast",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB7npITrOBzhGA2WxkovYOdqmWjzVuClXy3vRwxo8CztQQlf9GIwiNfVQgco91fP2w98fpbfwjghOHhvtMRwncSh4rS9f0JPdGlTNnzTFdAxndm6Jt_By5eGhzOqVff8LPqFiwjIA6TXGHkigv2nxAvJzMes_CyV7NWsrt-pKl5nqF6g_QAc1vkGAnIe4VWE6U1FLR2uPEkcSicvajiMTslxJGTFjD_xc8v_Io-ND-9",
            progressPercent = 0,
            durationText = "2h 10m",
            remainingText = "2h 10m",
            rating = 4.9,
            category = BookCategory.SHORT_LISTENS,
            formatTag = "Full Monograph"
        ),
        AudiobookItem(
            id = "shortness_of_life",
            title = "On the Shortness of Life",
            author = "Seneca",
            narrator = "Philosophy Cast",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCsgmE2Ztabuo0gJLvXigzioE-hYFfoPXtf9PSFxpblef18in5MQJcbDzCUpaPMokSZVBnaRHn52Sgalh6q-F3iEanYaz6ICeJbO6SHg9k69_bnWSjTRwW89403IzJcIPeMik2C7G-Hq8uQ55N57QN00VEBIvMPx3Numh66dzdxqIpo1R-dVwEtXSqpa4QNVABZlRXyKc5HfIiiru6XfzODl4ENT9dIJ48nMsaIzPg2",
            progressPercent = 0,
            durationText = "1h 48m",
            remainingText = "1h 48m",
            rating = 4.7,
            category = BookCategory.SHORT_LISTENS,
            formatTag = "Stoic Dialogues"
        )
    )

    val StaffPicks = listOf(
        AudiobookItem(
            id = "great_gatsby",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            narrator = "F. Vance",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVhEfAJ-BWksYBW1IXGm_h6JxMG3bhpGH0HAABSoeLK5t_zBeBTVBtSo9jx9Bfznk5PZXu8q_BGo2qVjuYas-8szq5s6lJ8U8zaboixxpIry1yjUNnu0KV1-6D0PqgggSlzBgPlcfqlSDCf2cDceVJye-BFJppt6FABZ6eAGGOr4VX2IFI6jVBLz2PP5b6BOVG6VbcTjf8Bkj73gyUB7fSrgcYrv7ibHBPmBlS7Tmg",
            rating = 4.8,
            category = BookCategory.STAFF_PICKS,
            formatTag = "Public Domain"
        ),
        AudiobookItem(
            id = "pride_prejudice",
            title = "Pride and Prejudice",
            author = "Jane Austen",
            narrator = "Rosamund Pike",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC6OZ2Od-9lEnL5i3n5RcEOa2J7hxzOZx0ayfU3aGb5aGXRkSNsy6YM8fgb55GEEQBzWZZbAEdXhSPxR0_omVVxVk6o1oNeCD0WMIpf-8Okffi5MbE4e7uBEgslASaqky9RyhSqE1zNjjDTt7NMhvzNi8O2U0hcvQ_DbA45sG4TSIXcEA8Oy7-Pl-ACrmlsmLxwZPrXPDSUpjnYO9D10Wi4ONDIIFYirHz-ii4W8U0H",
            rating = 4.9,
            category = BookCategory.STAFF_PICKS,
            formatTag = "Public Domain"
        )
    )

    val LibraryShelf = listOf(
        AudiobookItem(
            id = "project_hail_mary",
            title = "Project Hail Mary",
            author = "Andy Weir",
            narrator = "Ray Porter",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbtVEoLSaI5zdGFXvpRmyRviuMwnVZySZhlfnTWJdbQATdmoJiHxe3Mmv8Ltj-NHA4pUtBS8EQKXlj16OQ8hOdBGbYuYoECTXDry11gzjwaqrgSw4JFyn7J0ouRVhpyVbKVY2Fe0xstD4SZmNtTxQGSdTIN0zM-WrpK4ekh0u9PsLbjulu2IbMtfu7tKZJxAYnz74JSnFpmwN3045Vg358OwwIyRIa7LSKBSA3z4sY",
            progressPercent = 42,
            currentChapter = "Chapter 14",
            durationText = "16h 10m",
            remainingText = "04:12:30 left",
            rating = 4.9,
            isDownloaded = true,
            isSynchronized = true,
            speed = 1.0f,
            formatTag = "Studio Audio"
        ),
        AudiobookItem(
            id = "deep_work_pdf",
            title = "Deep_Work_Summary.pdf",
            author = "Cal Newport",
            narrator = "Synthesized Audio Note",
            coverUrl = "", // renders as PDF placeholder
            progressPercent = 57,
            currentChapter = "Page 8 of 14",
            durationText = "48m",
            remainingText = "Synced 12m ago",
            rating = 4.8,
            category = BookCategory.MY_DOCUMENTS,
            isDownloaded = true,
            isSynchronized = true,
            formatTag = "PDF Doc"
        ),
        AudiobookItem(
            id = "piranesi",
            title = "Piranesi",
            author = "Susanna Clarke",
            narrator = "Chiwetel Ejiofor",
            coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCskyUZ1K-zvrsmRC6bTwN0_POXdPSvpWDncD6nIjDTO_IT3yiEOLDQAV96mK2aitIv6Agxs01bFNUQ7AMKqzL5OJbfGdMRKQUxWctcT_r67skJFB6m2td3tm7eTO2DSXbXmcAbHWZZ3dFgAVqO3mPWtlkKzE-9Ak4WDHjMcpPvvPH9c8Ql-kZMxsW3_q5B0NxbebeXIgPrpla0AmP0eio8X58iLGnPQMVQI5cjZLIx",
            progressPercent = 88,
            currentChapter = "Part III • The Tides",
            durationText = "6h 58m",
            remainingText = "Part III • 88%",
            rating = 4.9,
            isDownloaded = true,
            isSynchronized = true,
            speed = 1.0f,
            formatTag = "Audiobook"
        )
    )

    val Chapter4Sentences = listOf(
        SentenceItem(
            id = 0,
            text = "It began not with thunder or the shifting of ancient plates, but with the subtle turning of the tide against the breakwater. Old men leaned over the timber railings, counting the rhythm of each pulse before speaking a single word of warning.",
            startTimeSeconds = 0f,
            endTimeSeconds = 8.5f,
            isRead = true
        ),
        SentenceItem(
            id = 1,
            text = "For three days, the harbor masters maintained that the currents were merely seasonal disturbances.",
            startTimeSeconds = 8.5f,
            endTimeSeconds = 14.2f,
            isRead = true
        ),
        SentenceItem(
            id = 2,
            text = "When the Sun reached the break between the roofs, the light fell straight across the desk, illuminating every particle of dust in quiet suspension.",
            startTimeSeconds = 14.2f,
            endTimeSeconds = 24.8f,
            isRead = false,
            userNote = "Parallels with the motif of photosynthetic empathy in Chapter 2. Audio timestamp: 01:24:18."
        ),
        SentenceItem(
            id = 3,
            text = "She held the ledger close to her chest. If the numbers aligned with yesterday's ledger, the cargo wasn't grain or ambergris at all, but rather the preserved instruments from the observatory on the outer shoals.",
            startTimeSeconds = 24.8f,
            endTimeSeconds = 38.0f,
            isRead = false
        ),
        SentenceItem(
            id = 4,
            text = "Outside, a tern cried twice, its shadow cutting across the bleached limestone flags. The clocks of the guild hall struck four in cascading unison, resonant brass notes settling over the damp stones of the quarter.",
            startTimeSeconds = 38.0f,
            endTimeSeconds = 52.0f,
            isRead = false
        )
    )

    val VoiceOptions = listOf(
        VoiceModel(
            id = "Nova",
            name = "Nova",
            badge = "Neural HD",
            description = "Warm • Expressive • Subtle Breath cadence",
            sampleText = "The afternoon sun cut through the tall library arches with quiet clarity."
        ),
        VoiceModel(
            id = "Alloy",
            name = "Alloy",
            badge = "Offline Ready",
            description = "Crisp • Balanced • High Nonfiction clarity",
            sampleText = "Deep work is the ability to focus without distraction on a cognitively demanding task."
        ),
        VoiceModel(
            id = "Onyx",
            name = "Onyx",
            badge = "Neural HD",
            description = "Deep • Authoritative • Classic Epic Baritone",
            sampleText = "The sky above the port was the color of television, tuned to a dead channel."
        )
    )

    val FeaturedHighlight = UserHighlight(
        id = "hl_1",
        bookTitle = "Klara and the Sun",
        chapter = "Ch. 4",
        quote = "The Sun always has ways to reach us, no matter how harsh the patterns look from the inside.",
        marginaliaNote = "Parallels with the motif of photosynthetic empathy in Chapter 2. Audio timestamp: 01:24:18.",
        timestamp = "01:24:18",
        dateText = "Today, 08:45 AM"
    )
}
