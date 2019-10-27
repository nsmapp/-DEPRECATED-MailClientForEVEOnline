package by.nepravskysm.mailclientforeveonline.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.UnreadMailsCount
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase
import by.nepravskysm.domain.usecase.character.UpdateContactsDBUseCase
import by.nepravskysm.domain.usecase.character.UpdateContactsRestUseCase
import by.nepravskysm.domain.usecase.mails.GetUnreadMailUseCase
import by.nepravskysm.domain.usecase.mails.SynchroMailsHeaderUseCase
import by.nepravskysm.domain.utils.AUTH_ERROR
import by.nepravskysm.domain.utils.DB_ACTIVE_CHARACTER_INFO_ERROR
import by.nepravskysm.domain.utils.SYNCHRONIZE_CONTACT_ERROR
import by.nepravskysm.domain.utils.SYNCHRONIZE_MAIL_HEADER_ERROR


class MainViewModel(private val authUseCase: AuthUseCase,
                    private val getActiveCharInfo: GetActivCharInfoUseCase,
                    private val synchrMailsHeader: SynchroMailsHeaderUseCase,
                    private val updateContactsRest: UpdateContactsRestUseCase,
                    private val updateContactsDB: UpdateContactsDBUseCase,
                    private val getUnreadMailCount: GetUnreadMailUseCase
): ViewModel(){

    var activeCharacterName: String = ""


    val characterName: MutableLiveData<String> by lazy { MutableLiveData<String>("")}
    val characterId: MutableLiveData<Long> by lazy { MutableLiveData<Long>()}
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val unreadMailsCount: MutableLiveData<UnreadMailsCount>
            by lazy { MutableLiveData<UnreadMailsCount>() }

    fun startAuth(code: String){
        authUseCase.setData(code)
            .execute {
                onComplite {
                    characterName.value = it.characterName
                    activeCharacterName = it.characterName
                    getActiveCharacterInfo()
                    synchoniseContacts(it.characterID)
                    synchronizeMailHeader()
                }
                onError {
                    eventId.value = AUTH_ERROR
                }
            }
    }

    fun getUnreadMails(){
        getUnreadMailCount.execute {
            onComplite { unreadMailsCount.value = it }
            onError {  }
        }
    }

    fun synchoniseContacts(characterId: Long){
        updateContactsRest.setData(characterId).execute {
            onComplite { }
            onError {
                eventId.value = SYNCHRONIZE_CONTACT_ERROR
            }
        }
    }

    fun getActiveCharacterInfo(){
        getActiveCharInfo.execute {
            onComplite {
                activeCharacterName = it.characterName
                characterName.value = it.characterName
                characterId.value = it.characterId
            }
            onError {
                eventId.value = DB_ACTIVE_CHARACTER_INFO_ERROR
            }
        }
    }

    fun synchronizeMailHeader(){
        isVisibilityProgressBar.value = true
        synchrMailsHeader.execute {
            onComplite {
                getUnreadMails()
                isVisibilityProgressBar.value = false
                updateContactsDB.execute { }
            }
            onError {
                getUnreadMails()
                isVisibilityProgressBar.value = false
                eventId.value = SYNCHRONIZE_MAIL_HEADER_ERROR
            }
        }
    }
}