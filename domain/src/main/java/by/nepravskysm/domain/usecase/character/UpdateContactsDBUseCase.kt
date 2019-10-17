package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class UpdateContactsDBUseCase(private val dbMailHeadersRepo: DBMailHeadersRepository,
                              private val activeCharacterRepo: ActiveCharacterRepository,
                              private val dbCharacterContactsRepo: DBCharacterContactsRepository) : AsyncUseCase<Boolean>(){

    override suspend fun onBackground(): Boolean {
        val activeCharacter = activeCharacterRepo.getActiveCharacterName()
        val mailHeaderList: List<MailHeader> = dbMailHeadersRepo.getMailsHeaders(activeCharacter)
        val contactList = mutableListOf<Contact>()
        for (header in mailHeaderList){
            if(header.labels.isNotEmpty()){
                contactList.add(
                    Contact(header.fromId,
                        "mail_header_contact",
                        1.0,
                        header.fromName)
                )
            }
        }
        contactList.distinctBy { it.contactName }
        dbCharacterContactsRepo.insertContactList(contactList, activeCharacter)
        return true
    }

}