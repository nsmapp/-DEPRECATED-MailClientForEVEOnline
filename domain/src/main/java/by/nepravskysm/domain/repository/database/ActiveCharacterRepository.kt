package by.nepravskysm.domain.repository.database

interface ActiveCharacterRepository {

    suspend fun insertCharacterName(activeCharacter: String)

    suspend fun updateActiveCharacterName(characterName: String):Boolean

    suspend fun getActiveCharacterName() : String
}