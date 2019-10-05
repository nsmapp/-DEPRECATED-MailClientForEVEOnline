package by.nepravskysm.database.repoimpl

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.ActivaCharacterDBE
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository

class ActiveCharacterRepoImpl(private val appDatabase: AppDatabase) : ActiveCharacterRepository {

    override suspend fun insertCharacterName(activeCharacter: String) {
        appDatabase.activeCharacterDao().insertCharacterName(ActivaCharacterDBE(activeCharacter))
    }

    override suspend fun updateActiveCharacterName(characterName: String): Boolean {
        appDatabase.activeCharacterDao().updateActiveCharacterName(characterName)

        return true
    }

    override suspend fun getActiveCharacterName(): String =
        appDatabase.activeCharacterDao()
            .getActiveCharacterName()

}