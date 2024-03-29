package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class ChangeActiveCharacter(private val activeCharacterRepo: ActiveCharacterRepository) :
    AsyncUseCase<Boolean>() {

    private var characterName = ""
    fun setData(characterName: String) : ChangeActiveCharacter{
        this.characterName = characterName
        return this
    }

    override suspend fun onBackground(): Boolean =
        activeCharacterRepo.updateActiveCharacterName(characterName)

}