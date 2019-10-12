package by.nepravskysm.mailclientforeveonline.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase
import by.nepravskysm.domain.usecase.character.SynchroniseCharactersContactsUseCase
import by.nepravskysm.domain.usecase.mails.SynchroMailsHeaderUseCase


class MainViewModel(private val authUseCase: AuthUseCase,
                    private val getActivCharInfoUseCase: GetActivCharInfoUseCase,
                    private val synchroMailsHeaderUseCase: SynchroMailsHeaderUseCase,
                    private val synchroniseCharactersContactsUseCase: SynchroniseCharactersContactsUseCase
): ViewModel(){



    val characterName: MutableLiveData<String> by lazy { MutableLiveData<String>("")}
    val characterId: MutableLiveData<Long> by lazy { MutableLiveData<Long>()}
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}

    fun startAuth(code: String){
        authUseCase.setData(code)
        authUseCase.execute {
            onComplite {
                characterName.value = it.characterName
                synchronizeMailHeader()
                getActiveCharacterInfo()
                synchoniseContacts(it.characterID)
            }
            onError { Log.d("logde----->", it.toString()) }
        }
    }

    fun synchoniseContacts(characterId: Long){
        synchroniseCharactersContactsUseCase.setData(characterId).execute {
            onComplite { Log.d("logde----->", "contact sinchro COMPLITE") }
            onError {  Log.d("logde----->", " contract sinchro ERROR ${it.message}") }
        }
    }



    fun getActiveCharacterInfo(){
        getActivCharInfoUseCase.execute {
            onComplite {
                characterName.value = it.characterName
                characterId.value = it.characterId

            }
            onError { Log.d("logde-->",  "getActiveCharacterInfo()" + it.toString()) }
        }
    }

    fun synchronizeMailHeader(){
        isVisibilityProgressBar.value = true
        synchroMailsHeaderUseCase.execute {
            onComplite {

                isVisibilityProgressBar.value = false
                //TODO добавить оповещениие о проведенной синхронизации

                 Log.d("logde----->", "synchronizeMailHeader() Completed")
            }
            onError {
                isVisibilityProgressBar.value = false
                Log.d("logde----->", it.toString()) }
        }
    }
}