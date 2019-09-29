package by.nepravskysm.mailclientforeveonline.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase
import by.nepravskysm.domain.usecase.mails.SynchroMailsHeaderUseCase


class MainViewModel(private val authUseCase: AuthUseCase,
                    private val getActivCharInfoUseCase: GetActivCharInfoUseCase,
                    private val synchroMailsHeaderUseCase: SynchroMailsHeaderUseCase
): ViewModel(){



    val characterName: MutableLiveData<String> by lazy { MutableLiveData<String>("")}
    val characterId: MutableLiveData<Long> by lazy { MutableLiveData<Long>()}

    init {
        getActiveCharacterInfo()
    }

    fun startAuth(code: String){
        authUseCase.setFitstAuthToken(code)
        authUseCase.execute {
            onComplite {
                characterName.value = it.characterName
                synchronizeMailHeader()
            }
            onError { Log.d("logde----->", it.toString()) }
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
        synchroMailsHeaderUseCase.execute {
            onComplite {

                //TODO добавить оповещениие о проведенной синхронизации

                 Log.d("logde----->", "synchronizeMailHeader() Completed")
            }
            onError { Log.d("logde----->", it.toString()) }
        }
    }
}