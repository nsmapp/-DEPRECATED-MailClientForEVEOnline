package by.nepravskysm.database.repoimpl

import android.content.Context
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.MailHeaderDBE
import by.nepravskysm.database.entity.subentity.RecipientDBE
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class DBMailHeaderRepoImplTest {

    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var repoImpl: DBMailHeaderRepoImpl
    private lateinit var header: MailHeaderDBE
    private val headerDBEList = mutableListOf<MailHeader>()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getContext()
        appDatabase = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()


        repoImpl = DBMailHeaderRepoImpl(appDatabase)
        header = MailHeaderDBE(
            1111, 12, "name1", true,
            listOf<Int>(2), listOf(RecipientDBE(0, "type")), "subject1",
            "timestamp1", "ownerName1", "mailType1"
        )
        for (i in 1L..10L) {
            headerDBEList.add(
                MailHeader(
                    100 + i, 12 + i, "name$i", true,
                    listOf(i.toInt()), listOf(Recipient(i, "type")), "subject$i",
                    "timestamp$i", "ownerName$i"
                )
            )
        }
    }

    @After
    fun closeDB() {
        appDatabase.close()
    }

    @Test
    fun insert_and_read_from_database() {
        var count: Int = 0
        runBlocking {
            repoImpl.insert(headerDBEList, "testName")
            count = repoImpl.getAllWithoutSent("testName").size
        }
        assertEquals(9, count)

    }

    @Test
    fun last_mail_id() {
        var lastMailId: Long = 0
        runBlocking {
            repoImpl.insert(headerDBEList, "testName")
            lastMailId = repoImpl.getLastMailId("testName")
        }
        assertEquals(110, lastMailId)
    }


    @Test
    fun map_database_header_To_domain_header() {
        val result = repoImpl.mapToDomainHeaders(listOf(header))

        assertEquals(header.fromId, result[0].fromId)
        assertEquals(header.fromId, result[0].fromId)
        assertEquals(header.mailId, result[0].mailId)
        assertEquals(header.mailType, result[0].mailType)
        assertEquals(header.fromName, result[0].fromName)
        assertEquals(header.isRead, result[0].isRead)
        assertEquals(header.recipients.size, result[0].recipients.size)
        assertEquals(header.subject, result[0].subject)
        assertEquals(header.timestamp, result[0].timestamp)
    }
}