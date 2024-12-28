package com.happy.friendogly

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.happy.friendogly.local.dao.RecentPetsDao
import com.happy.friendogly.local.model.GenderEntity
import com.happy.friendogly.local.model.RecentPetEntity
import com.happy.friendogly.local.model.SizeTypeEntity
import com.happy.friendogly.local.room.RecentPetsDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class RecentPetsDatabaseTest {
    private lateinit var dao: RecentPetsDao

    private lateinit var db: RecentPetsDatabase

    @Before
    fun setUp() {
        db =
            Room
                .inMemoryDatabaseBuilder(
                    context = ApplicationProvider.getApplicationContext<Context>(),
                    klass = RecentPetsDatabase::class.java,
                ).build()

        dao = db.recentPetDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `최근_반려동물_정보를_저장하고_조회할_수_있다`() {
        // when
        runBlocking {
            dao.insertRecentPet(DUMMY_RECENT_PET)
        }

        // then
        runBlocking {
            val actual: RecentPetEntity? = dao.getRecentPetById(DUMMY_RECENT_PET.id)
            assertThat(actual?.memberId).isEqualTo(DUMMY_RECENT_PET.memberId)
            assertThat(actual?.name).isEqualTo(DUMMY_RECENT_PET.name)
            assertThat(actual?.imgUrl).isEqualTo(DUMMY_RECENT_PET.imgUrl)
        }
    }

    @Test
    fun `여러_반려동물_정보를_추가하고_조회할_수_있다`() {
        // when
        runBlocking {
            DUMMY_RECENT_PETS.forEach { recentPetEntity ->
                dao.insertRecentPet(recentPetEntity)
            }
        }

        runBlocking {
            assertThat(dao.getAllRecentPet().size).isEqualTo(DUMMY_RECENT_PETS.size)
        }
    }

    companion object {
        private val DUMMY_RECENT_PET =
            RecentPetEntity(
                memberId = 0L,
                petId = 0L,
                imgUrl = "imageUrl",
                name = "땡이",
                birthday = LocalDate(2024, 10, 2),
                gender = GenderEntity.FEMALE,
                sizeType = SizeTypeEntity.SMALL,
                createdAt = LocalDateTime.now(),
            )

        private val DUMMY_RECENT_PETS =
            List(5) { DUMMY_RECENT_PET.copy(memberId = it.toLong()) }
    }
}
