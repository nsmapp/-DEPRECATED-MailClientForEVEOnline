package by.nepravskysm.mailclientforeveonline.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.UnreadMailsCount
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase
import by.nepravskysm.domain.usecase.character.UpdateContactsDBUseCase
import by.nepravskysm.domain.usecase.character.UpdateContactsRestUseCase
import by.nepravskysm.domain.usecase.mails.GetUnreadMailUseCase
import by.nepravskysm.domain.usecase.mails.SynchroMailsHeaderUseCase
import by.nepravskysm.domain.utils.DB_ACTIVE_CHARACTER_INFO_ERROR
import by.nepravskysm.domain.utils.AUTH_ERROR
import by.nepravskysm.domain.utils.SYNCHRONIZE_CONTACT_ERROR
import by.nepravskysm.domain.utils.SYNCHRONIZE_MAIL_HEADER_ERROR


class MainViewModel(private val authUseCase: AuthUseCase,
                    private val getActivCharInfoUseCase: GetActivCharInfoUseCase,
                    private val synchroMailsHeaderUseCase: SynchroMailsHeaderUseCase,
                    private val updateContactsRestUseCase: UpdateContactsRestUseCase,
                    private val updateContactsDBUseCase: UpdateContactsDBUseCase,
                    private val getUnreadMailCount: GetUnreadMailUseCase
): ViewModel(){

    var activeCharacterName: String = ""


    val characterName: MutableLiveData<String> by lazy { MutableLiveData<String>("")}
    val characterId: MutableLiveData<Long> by lazy { MutableLiveData<Long>()}
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}
    val errorId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val unreadMailsCount: MutableLiveData<UnreadMailsCount>
            by lazy { MutableLiveData<UnreadMailsCount>(UnreadMailsCount()) }

    fun startAuth(code: String){
        authUseCase.setData(code)
        authUseCase.execute {
            onComplite {
                characterName.value = it.characterName
                activeCharacterName = it.characterName
                getActiveCharacterInfo()
                synchoniseContacts(it.characterID)
                synchronizeMailHeader()
            }
            onError {
                errorId.value = AUTH_ERROR
                Log.d("logde----->", it.toString()) }
        }
    }

    fun getUnreadMails(){
        getUnreadMailCount.execute {
            onComplite { unreadMailsCount.value = it }
            onError {  }
        }
    }

    fun synchoniseContacts(characterId: Long){
        updateContactsRestUseCase.setData(characterId).execute {
            onComplite { Log.d("logde----->", "contact sinchro COMPLITE") }
            onError {
                errorId.value = SYNCHRONIZE_CONTACT_ERROR
                Log.d("logde----->", " contract sinchro ERROR ${it.message}") }
        }
    }

    fun getActiveCharacterInfo(){
        getActivCharInfoUseCase.execute {
            onComplite {
                activeCharacterName = it.characterName
                characterName.value = it.characterName
                characterId.value = it.characterId
            }
            onError {
                errorId.value = DB_ACTIVE_CHARACTER_INFO_ERROR
                Log.d("logde-->",  "getActiveCharacterInfo()" + it.toString()) }
        }
    }

    fun synchronizeMailHeader(){
        isVisibilityProgressBar.value = true
        synchroMailsHeaderUseCase.execute {
            onComplite {
                getUnreadMails()
                isVisibilityProgressBar.value = false
                updateContactsDBUseCase.execute {  }
                //TODO добавить оповещениие о проведенной синхронизации
                 Log.d("logde----->", "synchronizeMailHeader() Completed")
            }
            onError {
                getUnreadMails()
                isVisibilityProgressBar.value = false
                errorId.value = SYNCHRONIZE_MAIL_HEADER_ERROR
                Log.d("logde----->", it.toString()) }
        }
    }
}